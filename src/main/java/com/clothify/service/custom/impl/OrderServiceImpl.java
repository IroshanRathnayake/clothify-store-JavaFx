package com.clothify.service.custom.impl;

import com.clothify.db.DBConnection;
import com.clothify.dto.Customer;
import com.clothify.dto.Order;
import com.clothify.dto.OrderDetail;
import com.clothify.dto.Product;
import com.clothify.entity.OrderDetailEntity;
import com.clothify.entity.OrderEntity;
import com.clothify.repository.DaoFactory;
import com.clothify.repository.custom.OrderDao;
import com.clothify.service.custom.OrderService;
import com.clothify.util.DaoType;
import org.modelmapper.ModelMapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderServiceImpl implements OrderService {
    private final OrderDao orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public boolean placeOrder(Order order, List<OrderDetail> orderDetails) throws SQLException{
        Connection connection = DBConnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);

            List<OrderDetailEntity> orderDetailEntities = new ArrayList<>();
            for (OrderDetail orderDetail : orderDetails) {
                OrderDetailEntity entity = modelMapper.map(orderDetail, OrderDetailEntity.class);
                orderDetailEntities.add(entity);
            }

            //Save Order
            boolean isOrderSaved = orderDao.save(modelMapper.map(order, OrderEntity.class));
            if (!isOrderSaved) {
                connection.rollback();
                return false;
            }

            //Save Order Details
            boolean isOrderDetailSaved = orderDao.saveOrderDetail(orderDetailEntities);
            if(!isOrderDetailSaved) {
                connection.rollback();
                return false;
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            connection.rollback();
            throw new RuntimeException(e);
        }finally {
            connection.setAutoCommit(true);
        }
    }

    @Override
    public Customer getCustomerByPhone(String phone) {
        return null;
    }

    @Override
    public Product getProductById(String id) {
        return null;
    }

    @Override
    public String getLastOrderID() {
        String lastID = orderDao.findLastID();

        //Pattern to remove unwanted characters
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(lastID);

        return (matcher.find()) ? matcher.group() : null;
    }

}
