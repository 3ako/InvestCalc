package ru.mslotvi.exchange;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.*;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.*;

import java.util.List;

@UtilityClass
public class EfficientFrontierWorkbook {

    @SneakyThrows
    public Workbook createEfficientFrontierWorkbook(List<? extends DefaultPortfolio> portfolios) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Efficient Frontier");

        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Портфолио");
        headerRow.getCell(0).setCellStyle(headerStyle);
        headerRow.createCell(1).setCellValue("Ожидаемая доходность");
        headerRow.getCell(1).setCellStyle(headerStyle);
        headerRow.createCell(2).setCellValue("Риск");
        headerRow.getCell(2).setCellStyle(headerStyle);

        for (int i = 0; i < portfolios.size(); i++) {
            DefaultPortfolio portfolio = portfolios.get(i);
            Row row = sheet.createRow(i + 1);
            row.createCell(0).setCellValue("Портфолио " + (i + 1));
            row.createCell(1).setCellValue(portfolio.expectedReturn());
            row.createCell(2).setCellValue(portfolio.risk());
        }

        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }

        createScatterChart(sheet, portfolios.size());

        return workbook;
    }

    @SneakyThrows
    private void createScatterChart(Sheet sheet, int size) {
        Drawing<?> drawing = sheet.createDrawingPatriarch();
        XSSFSheet xssfSheet = (XSSFSheet) sheet;
        XSSFDrawing xssfDrawing = (XSSFDrawing) drawing;
        XSSFClientAnchor anchor = (XSSFClientAnchor) drawing.createAnchor(0, 0, 0, 0, 5, 1, 20, 25);
        XSSFChart chart = xssfDrawing.createChart(anchor);

        XDDFCategoryAxis xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
        xAxis.setTitle("Риск");
        XDDFValueAxis yAxis = chart.createValueAxis(AxisPosition.LEFT);
        yAxis.setTitle("Ожидаемая доходность");

        XDDFScatterChartData dataChart = (XDDFScatterChartData) chart.createData(ChartTypes.SCATTER, xAxis, yAxis);
        dataChart.setVaryColors(false);

        XDDFNumericalDataSource<Double> yDataSource = XDDFDataSourcesFactory.fromNumericCellRange(xssfSheet, new CellRangeAddress(1, size, 1, 1));
        XDDFNumericalDataSource<Double> xDataSource = XDDFDataSourcesFactory.fromNumericCellRange(xssfSheet, new CellRangeAddress(1, size, 2, 2));

        XDDFScatterChartData.Series series = (XDDFScatterChartData.Series) dataChart.addSeries(xDataSource, yDataSource);

        series.setMarkerStyle(MarkerStyle.CIRCLE);
        series.setSmooth(false);
        series.setTitle("Эффективная граница", null);

        // Убираем линии между точками
        XDDFShapeProperties shapeProperties = new XDDFShapeProperties();
        XDDFLineProperties lineProperties = new XDDFLineProperties();
        lineProperties.setFillProperties(new XDDFNoFillProperties());
        shapeProperties.setLineProperties(lineProperties);
        series.setShapeProperties(shapeProperties);

        // Настройка цвета точек (синий)
        XDDFSolidFillProperties fillProperties = new XDDFSolidFillProperties(XDDFColor.from(PresetColor.RED));
        shapeProperties.setFillProperties(fillProperties);

        chart.plot(dataChart);
    }
}