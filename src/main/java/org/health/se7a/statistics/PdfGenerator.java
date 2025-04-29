package org.health.se7a.statistics;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

public class PdfGenerator {

    private static final BaseColor HEADER_COLOR = new BaseColor(13, 110, 253);
    private static final BaseColor ACCENT_COLOR = new BaseColor(111, 66, 193);
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 24, ACCENT_COLOR);
    private static final Font SECTION_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, ACCENT_COLOR);
    private static final Font TABLE_HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.WHITE);

    public static byte[] generateAdvancedReport(StatisticsResponseDTO dto, String logoPath) {
        try {
            Document document = new Document(PageSize.A4);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, out);

            writer.setPageEvent(new HeaderFooter(logoPath));
            document.open();

            addTitle(document, "HEALTHCARE STATISTICS REPORT");
            addGeneralStats(document, dto);
            addDemographicSection(document, dto);
            addClinicalSection(document, dto);
            addCombinedStatsSection(document, dto);
            addFooter(document);

            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF: " + e.getMessage());
        }
    }

    // الأقسام الرئيسية
    private static void addGeneralStats(Document doc, StatisticsResponseDTO dto) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        addTableHeader(table, "Metric", "Value");

        addTableRow(table, "Total Patients", dto.getTotalPatients().toString());
        addTableRow(table, "Total Smokers", dto.getTotalSmokers().toString());
        addTableRow(table, "With Chronic Diseases", dto.getTotalWithChronicDiseases().toString());

        doc.add(createSectionTitle("Overview Statistics"));
        doc.add(table);
    }

    private static void addDemographicSection(Document doc, StatisticsResponseDTO dto)
            throws DocumentException, IOException {

        if(dto.getPatientsByGender() != null) {
            doc.add(createSectionTitle("Gender Distribution"));
            doc.add(createTwoColumnTable(dto.getPatientsByGender()));

            JFreeChart genderChart = createPieChart("Gender Ratio", dto.getPatientsByGender());
            addChart(doc, genderChart, 400, 250);
        }

        if(dto.getPatientsBySmokingStatus() != null) {
            doc.add(createSectionTitle("Smoking Status"));
            doc.add(createTwoColumnTable(dto.getPatientsBySmokingStatus()));
        }
    }

    private static void addClinicalSection(Document doc, StatisticsResponseDTO dto)
            throws DocumentException, IOException {

        if(dto.getPatientsByDisease() != null) {
            doc.add(createSectionTitle("Chronic Diseases Analysis"));

            PdfPTable table = createTwoColumnTable(dto.getPatientsByDisease());
            table.setSpacingBefore(10f);
            doc.add(table);

            JFreeChart diseaseChart = createBarChart("Disease Prevalence", dto.getPatientsByDisease());
            addChart(doc, diseaseChart, 500, 300);
        }
    }

    private static void addCombinedStatsSection(Document doc, StatisticsResponseDTO dto)
            throws DocumentException {

        if(dto.getGenderSmokingCombination() != null) {
            doc.add(createSectionTitle("Gender-Smoking Combination"));

            PdfPTable table = new PdfPTable(3);
            addTableHeader(table, "Gender", "Smoking Status", "Count");

            dto.getGenderSmokingCombination().forEach((gender, smokingMap) ->
                    smokingMap.forEach((status, count) ->
                            addTableRow(table, gender.name(), status.name(), count.toString())
                    )
            );

            doc.add(table);
        }
    }

    // أدوات مساعدة
    private static Paragraph createSectionTitle(String title) {
        Paragraph p = new Paragraph(title, SECTION_FONT);
        p.setSpacingAfter(10f);
        return p;
    }

    private static <T> PdfPTable createTwoColumnTable(Map<T, Long> data) {
        PdfPTable table = new PdfPTable(2);
        addTableHeader(table, "Category", "Count");
        data.forEach((k, v) -> addTableRow(table, k.toString(), v.toString()));
        return table;
    }

    private static JFreeChart createPieChart(String title, Map<?, Long> data) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        data.forEach((k, v) -> dataset.setValue(k.toString(), v));
        return ChartFactory.createPieChart(title, dataset, true, true, false);
    }

    private static JFreeChart createBarChart(String title, Map<?, Long> data) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        data.forEach((k, v) -> dataset.addValue(v, "Count", k.toString()));
        return ChartFactory.createBarChart(title, "Category", "Count", dataset,
                PlotOrientation.VERTICAL, true, true, false);
    }

    private static void addChart(Document doc, JFreeChart chart, int width, int height)
            throws IOException, DocumentException {
        BufferedImage image = chart.createBufferedImage(width, height);
        Image pdfImage = Image.getInstance(image, null);
        pdfImage.setAlignment(Image.ALIGN_CENTER);
        doc.add(pdfImage);
    }

    private static void addTitle(Document doc, String title) throws DocumentException {
        Paragraph p = new Paragraph(title, TITLE_FONT);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(30f);
        doc.add(p);
    }

    private static void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, TABLE_HEADER_FONT));
            cell.setBackgroundColor(HEADER_COLOR);
            cell.setBorderWidth(2);
            table.addCell(cell);
        }
    }

    private static void addTableRow(PdfPTable table, String... values) {
        for (String value : values) {
            table.addCell(createCell(value));
        }
    }

    private static PdfPCell createCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(5);
        cell.setBorderColor(BaseColor.LIGHT_GRAY);
        return cell;
    }

    private static void addFooter(Document doc) throws DocumentException {
        Paragraph footer = new Paragraph("Generated by HealthSE7A System - Confidential",
                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20f);
        doc.add(footer);
    }

    static class HeaderFooter extends PdfPageEventHelper {
        private final Image logo;

        public HeaderFooter(String logoPath) throws IOException, BadElementException {
            this.logo = Image.getInstance(logoPath);
            logo.scaleToFit(100, 50);
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                // إضافة الشعار
                logo.setAbsolutePosition(40, document.top() + 20);
                writer.getDirectContent().addImage(logo);

                // إضافة رقم الصفحة
                ColumnText.showTextAligned(writer.getDirectContent(),
                        Element.ALIGN_CENTER,
                        new Phrase("Page " + writer.getPageNumber(),
                                FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY)),
                        297.5f, 30, 0);
            } catch (DocumentException e) {
                throw new RuntimeException(e);
            }
        }
    }
}