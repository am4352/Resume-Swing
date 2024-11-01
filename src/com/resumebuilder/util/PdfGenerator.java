package com.resumebuilder.util;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.element.LineSeparator;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.resumebuilder.model.Resume;

import java.io.FileNotFoundException;
import java.io.IOException;

public class PdfGenerator {

    public void generatePdf(Resume resume, String filePath) {
        try {
            // Initialize PDF writer and document
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4);

            // Set font styles
            PdfFont headerFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA_BOLD);
            PdfFont bodyFont = PdfFontFactory.createFont(com.itextpdf.io.font.constants.StandardFonts.HELVETICA);

            // 1. Add header section (Name, Contact Information, and Image)
            addHeader(document, resume, "C:\\Users\\DELL\\Downloads\\image.png", headerFont, bodyFont);

            // Add extra spacing before adding the line
            document.add(new Paragraph("\n\n"));  // Adds spacing to avoid overlap with the image

            // Add line separator
            document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(20));

            // 2. Professional Summary Section
            document.add(new Paragraph("Professional Summary")
                    .setFont(headerFont)
                    .setFontSize(16)
                    .setMarginBottom(10));
            document.add(new Paragraph(safeString(resume.getProfessionalSummary()))
                    .setFont(bodyFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

            // Add line separator
            document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

            // 3. Skills Section
            document.add(new Paragraph("Skills")
                    .setFont(headerFont)
                    .setFontSize(16)
                    .setMarginBottom(10));
            document.add(new Paragraph(safeString(resume.getSkills()))
                    .setFont(bodyFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

            // Add line separator
            document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

            // 4. Professional Experience Section
            document.add(new Paragraph("Professional Experience")
                    .setFont(headerFont)
                    .setFontSize(16)
                    .setMarginBottom(10));
            document.add(new Paragraph(safeString(resume.getJobRole()) + " | " + safeString(resume.getCompany()))
                    .setFont(headerFont)
                    .setFontSize(14)
                    .setMarginBottom(5));
            document.add(new Paragraph(safeString(resume.getStartDate()) + " - " + safeString(resume.getEndDate()))
                    .setFont(bodyFont)
                    .setFontSize(12)
                    .setItalic()
                    .setMarginBottom(10));
            document.add(new Paragraph("Responsibilities:")
                    .setFont(headerFont)
                    .setFontSize(12)
                    .setMarginBottom(5));
            document.add(new Paragraph(safeString(resume.getResponsibilities()))
                    .setFont(bodyFont)
                    .setFontSize(12)
                    .setMarginBottom(20));

            // Add line separator
            document.add(new LineSeparator(new SolidLine()).setMarginTop(10).setMarginBottom(10));

            // 5. Education Section
            document.add(new Paragraph("Education")
                    .setFont(headerFont)
                    .setFontSize(16)
                    .setMarginBottom(10));
            document.add(new Paragraph(safeString(resume.getDegree()))
                    .setFont(headerFont)
                    .setFontSize(14)
                    .setMarginBottom(5));
            document.add(new Paragraph(safeString(resume.getUniversity()) + " | Graduated: " + safeString(resume.getGraduationYear()))
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

    // Helper method to handle null strings
    private String safeString(String value) {
        return value == null ? "" : value;
    }

    // Method to add the header (name, contact info, and image) with proper alignment
    private void addHeader(Document document, Resume resume, String imagePath, PdfFont headerFont, PdfFont bodyFont) {
        try {
            // Load and scale the image
            Image img = new Image(ImageDataFactory.create(imagePath));
            float maxWidth = 100;
            float maxHeight = 100;
            img.scaleToFit(maxWidth, maxHeight);
            img.setFixedPosition(PageSize.A4.getWidth() - img.getImageScaledWidth() - 30, PageSize.A4.getHeight() - img.getImageScaledHeight() - 40);

            // Add the image to the document
            document.add(img);

            // Add the name and contact information aligned under the image
            Paragraph header = new Paragraph(safeString(resume.getFirstName()) + " " + safeString(resume.getLastName()))
                    .setFont(headerFont)
                    .setFontSize(22)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginBottom(5);
            document.add(header);

            Paragraph contactInfo = new Paragraph(safeString(resume.getEmail()) + " | " + safeString(resume.getPhone()) + " | " +
                    safeString(resume.getCity()) + ", " + safeString(resume.getCountry()))
                    .setFont(bodyFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMarginBottom(10);
            document.add(contactInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}