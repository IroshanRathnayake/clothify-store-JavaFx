package com.clothify.util;

import com.clothify.dto.Order;
import com.clothify.dto.OrderDetail;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PDFGenerator {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.DARK_GRAY);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font CONTENT_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL, BaseColor.BLACK);
    private static final BaseColor HEADER_COLOR = new BaseColor(48, 142, 223);
    private static final BaseColor ALTERNATE_ROW_COLOR = new BaseColor(242, 242, 242);

    public <T> void downloadPdf(Stage stage, ObservableList<T> data) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName(data.get(0) instanceof Order ? "OrderReport.pdf" : "OrderDetailReport.pdf");

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            if (data.get(0) instanceof Order) {
                generateOrderPdf(file.getAbsolutePath(), (ObservableList<Order>) data);
            } else if (data.get(0) instanceof OrderDetail) {
                generateOrderDetailsPdf(file.getAbsolutePath(), (ObservableList<OrderDetail>) data);
            }
        }
    }

    private void generateOrderPdf(String filePath, ObservableList<Order> orders) {
        Document document = new Document(PageSize.A4, 36f, 36f, 54f, 36f);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            addTitle(document, "Order Report");
            addTable(document, orders);
            addSummarySection(document, orders);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private void generateOrderDetailsPdf(String filePath, ObservableList<OrderDetail> orderDetails) {
        Document document = new Document(PageSize.A4, 36f, 36f, 54f, 36f);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();
            addTitle(document, "Order Details Report");
            addTable(document, orderDetails);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private void addTitle(Document document, String titleText) throws DocumentException {
        Paragraph title = new Paragraph(titleText, TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20f);
        document.add(title);

        Paragraph timestamp = new Paragraph(
                "Generated on: " + java.time.LocalDateTime.now().format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                ),
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY)
        );
        timestamp.setAlignment(Element.ALIGN_RIGHT);
        timestamp.setSpacingAfter(20f);
        document.add(timestamp);
    }

    private void addTable(Document document, ObservableList<?> data) throws DocumentException {
        PdfPTable table;
        if (data.get(0) instanceof Order) {
            table = createOrderTable((ObservableList<Order>) data);
        } else {
            table = createOrderDetailTable((ObservableList<OrderDetail>) data);
        }
        document.add(table);
    }

    private PdfPTable createOrderTable(ObservableList<Order> orders) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(20f);
        table.setWidths(new float[]{1.5f, 1.5f, 1f, 1.5f});

        addTableHeader(table, new String[]{"Order ID", "Customer ID", "Total", "Date"});

        boolean alternate = false;
        for (Order order : orders) {
            addOrderRow(table, order, alternate);
            alternate = !alternate;
        }
        return table;
    }

    private PdfPTable createOrderDetailTable(ObservableList<OrderDetail> orderDetails) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(20f);
        table.setWidths(new float[]{1.5f, 1f, 1f});

        addTableHeader(table, new String[]{"Order ID", "Product ID", "Quantity"});

        boolean alternate = false;
        for (OrderDetail detail : orderDetails) {
            addOrderDetailRow(table, detail, alternate);
            alternate = !alternate;
        }
        return table;
    }

    private void addTableHeader(PdfPTable table, String[] headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(HEADER_COLOR);
            cell.setPadding(8f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private void addOrderRow(PdfPTable table, Order order, boolean alternate) {
        BaseColor backgroundColor = alternate ? ALTERNATE_ROW_COLOR : BaseColor.WHITE;
        table.addCell(createCell(order.getId(), backgroundColor));
        table.addCell(createCell(order.getCustomerId(), backgroundColor));
        table.addCell(createCell(String.format("LKR %.2f", order.getTotal()), backgroundColor, Element.ALIGN_RIGHT));
        table.addCell(createCell(order.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), backgroundColor));
    }

    private void addOrderDetailRow(PdfPTable table, OrderDetail detail, boolean alternate) {
        BaseColor backgroundColor = alternate ? ALTERNATE_ROW_COLOR : BaseColor.WHITE;
        table.addCell(createCell(detail.getId(), backgroundColor));
        table.addCell(createCell(detail.getProductId(), backgroundColor));
        table.addCell(createCell(String.valueOf(detail.getQuantity()), backgroundColor, Element.ALIGN_CENTER));
    }

    private PdfPCell createCell(String content, BaseColor backgroundColor) {
        return createCell(content, backgroundColor, Element.ALIGN_CENTER);
    }

    private PdfPCell createCell(String content, BaseColor backgroundColor, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(content, CONTENT_FONT));
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(6f);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }

    private void addSummarySection(Document document, ObservableList<Order> orders) throws DocumentException {
        double totalAmount = orders.stream().mapToDouble(Order::getTotal).sum();

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(40);
        summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.setSpacingBefore(20f);

        addSummaryRow(summaryTable, "Total Orders:", String.valueOf(orders.size()));
        addSummaryRow(summaryTable, "Total Amount:", String.format("LKR %.2f", totalAmount));

        document.add(summaryTable);
    }

    private void addSummaryRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, HEADER_FONT));
        labelCell.setBackgroundColor(HEADER_COLOR);
        labelCell.setPadding(6f);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, CONTENT_FONT));
        valueCell.setPadding(6f);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(valueCell);
    }
}
