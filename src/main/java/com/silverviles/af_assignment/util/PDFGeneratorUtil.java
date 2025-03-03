package com.silverviles.af_assignment.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.silverviles.af_assignment.dao.Expense;
import com.silverviles.af_assignment.dao.Income;
import com.silverviles.af_assignment.dto.ExpenseReport;
import com.silverviles.af_assignment.dto.IncomeReport;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PDFGeneratorUtil {
    public static byte[] generateIncomeReportPdf(IncomeReport incomeReport) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Income Report"));
        document.add(new Paragraph("Username: " + incomeReport.getUsername()));
        document.add(new Paragraph("Total Income: " + incomeReport.getTotalIncome()));

        // Create a table with 3 columns
        Table table = new Table(3);
        table.addCell(new Cell().add(new Paragraph("Date")));
        table.addCell(new Cell().add(new Paragraph("Description")));
        table.addCell(new Cell().add(new Paragraph("Amount")));

        // Add income details to the table
        for (Income income : incomeReport.getIncomes()) {
            table.addCell(new Cell().add(new Paragraph(income.getDate().toString())));
            table.addCell(new Cell().add(new Paragraph(income.getDescription())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(income.getAmount()))));
        }

        document.add(table);
        document.close();
        return baos.toByteArray();
    }

    public static byte[] generateExpenseReportPdf(ExpenseReport expenseReport) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Expense Report"));
        document.add(new Paragraph("Username: " + expenseReport.getUsername()));
        document.add(new Paragraph("Total Expense: " + expenseReport.getTotalExpenses()));

        // Create a table with 3 columns
        Table table = new Table(3);
        table.addCell(new Cell().add(new Paragraph("Date")));
        table.addCell(new Cell().add(new Paragraph("Description")));
        table.addCell(new Cell().add(new Paragraph("Amount")));

        // Add expense details to the table
        for (Expense expense : expenseReport.getExpenses()) {
            table.addCell(new Cell().add(new Paragraph(expense.getDate().toString())));
            table.addCell(new Cell().add(new Paragraph(expense.getDescription())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(expense.getAmount()))));
        }

        document.add(table);
        document.close();
        return baos.toByteArray();
    }

    public static byte[] generateIncomeVsExpenseChart(IncomeReport incomeReport, ExpenseReport expenseReport) throws IOException {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        // Add income data
        dataset.addValue(incomeReport.getTotalIncome(), "Income", "Total");

        // Add expense data
        dataset.addValue(expenseReport.getTotalExpenses(), "Expense", "Total");

        JFreeChart barChart = ChartFactory.createBarChart(
                "Income vs Expense",
                "Category",
                "Amount",
                dataset
        );

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ChartUtils.writeChartAsPNG(baos, barChart, 800, 600);
        return baos.toByteArray();
    }
}
