package com.resumebuilder.util;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.resumebuilder.model.Resume;

public class PdfGenerator {

    public void generatePdf(Resume resume, String filePath) {
        try {
            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Set font styles
            PdfFont headerFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            PdfFont bodyFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

            // 1. Add header section (Name and Contact Information)
            Paragraph header = new Paragraph(resume.getFirstName() + " " + resume.getLastName())
                    .setFont(headerFont)
                    .setFontSize(22)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(5);
            document.add(header);

            Paragraph contactInfo = new Paragraph(resume.getEmail() + " | " + resume.getPhone() + " | " + resume.getCity() + ", " + resume.getCountry())
                    .setFont(bodyFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(contactInfo);

            // Add line separator
            document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(20));

            // 2. Job Title Section
            document.add(new Paragraph("Job Title: " + resume.getJobTitle())
                    .setFont(bodyFont)
                    .setFontSize(14)
                    .setMarginBottom(10));

            // 3. Professional Summary Section
            document.add(new Paragraph("Professional Summary")
                    .setFont(headerFont)
                    .setFontSize(16)
                    .setMarginBottom(10));
            document.add(new Paragraph(resume.getProfessionalSummary())
                    .setFont(bodyFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

            // Close the document
            document.close();
            System.out.println("PDF generated successfully!");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
