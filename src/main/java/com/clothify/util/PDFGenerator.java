package com.clothify.util;

import com.clothify.dto.Order;
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

    public void downloadOrderDetailsPdf(Stage stage, ObservableList<Order> orderDetails) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        fileChooser.setInitialFileName("OrderDetails.pdf");

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            generateOrderDetailsPdf(file.getAbsolutePath(), orderDetails);
        }
    }

    private void generateOrderDetailsPdf(String filePath, ObservableList<Order> orderDetails) {
        Document document = new Document(PageSize.A4, 36f, 36f, 54f, 36f);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();


//            Image logo = Image.getInstance("https://drive.google.com/uc?export=download&id=1rei3KOA_PDSjN14IWoG71pJH5ztPKc2z");
//             logo.setAlignment(Element.ALIGN_CENTER);
//             document.add(logo);

            // Title
            Paragraph title = new Paragraph("Order Details Report", TITLE_FONT);
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

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(20f);

            float[] columnWidths = {1.5f, 1.5f, 1f, 1.5f};
            table.setWidths(columnWidths);
            addTableHeader(table);

            boolean alternate = false;
            for (Order detail : orderDetails) {
                addRows(table, detail, alternate);
                alternate = !alternate;
            }

            document.add(table);

            addSummarySection(document, orderDetails);

            Phrase footer = new Phrase("Thank you!",
                    new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY));
            document.add(new Paragraph(footer));

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    private void addTableHeader(PdfPTable table) {
        String[] headers = {"Order ID", "Customer ID", "Total", "Date"};
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, HEADER_FONT));
            cell.setBackgroundColor(HEADER_COLOR);
            cell.setPadding(8f);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
        }
    }

    private void addRows(PdfPTable table, Order detail, boolean alternate) {
        PdfPCell cell;
        BaseColor backgroundColor = alternate ? ALTERNATE_ROW_COLOR : BaseColor.WHITE;

        // Order ID
        cell = createCell(detail.getId(), backgroundColor);
        table.addCell(cell);

        // Customer ID
        cell = createCell(detail.getCustomerId(), backgroundColor);
        table.addCell(cell);

        // Total (formatted as currency)
        cell = createCell(String.format("LKR %.2f", detail.getTotal()), backgroundColor);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cell);

        // Date
        cell = createCell(detail.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), backgroundColor);
        table.addCell(cell);
    }

    private PdfPCell createCell(String content, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, CONTENT_FONT));
        cell.setBackgroundColor(backgroundColor);
        cell.setPadding(6f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private void addSummarySection(Document document, ObservableList<Order> orderDetails) throws DocumentException {
        double totalAmount = orderDetails.stream()
                .mapToDouble(Order::getTotal)
                .sum();

        PdfPTable summaryTable = new PdfPTable(2);
        summaryTable.setWidthPercentage(40);
        summaryTable.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.setSpacingBefore(20f);

        PdfPCell labelCell = new PdfPCell(new Phrase("Total Orders:", HEADER_FONT));
        labelCell.setBackgroundColor(HEADER_COLOR);
        labelCell.setPadding(6f);
        summaryTable.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(String.valueOf(orderDetails.size()), CONTENT_FONT));
        valueCell.setPadding(6f);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.addCell(valueCell);

        labelCell = new PdfPCell(new Phrase("Total Amount:", HEADER_FONT));
        labelCell.setBackgroundColor(HEADER_COLOR);
        labelCell.setPadding(6f);
        summaryTable.addCell(labelCell);

        valueCell = new PdfPCell(new Phrase(String.format("LKR %.2f", totalAmount), CONTENT_FONT));
        valueCell.setPadding(6f);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summaryTable.addCell(valueCell);

        document.add(summaryTable);
    }
}