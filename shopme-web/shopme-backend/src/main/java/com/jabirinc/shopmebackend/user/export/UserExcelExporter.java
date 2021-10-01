package com.jabirinc.shopmebackend.user.export;

import com.jabirinc.shopmecommon.entity.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Getinet on 9/22/21
 */
public class UserExcelExporter extends AbstractExporter<User> {

    public static final int TITLE_ROW_HEIGHT = 45;
    public static final int TITLE_CELL_FONT_SIZE = 18;
    public static final int HEADER_CELL_FONT_SIZE = 16;
    public static final int DATA_CELL_FONT_SIZE = 14;
    public static final String WORKBOOK_SHEET_TITLE = "List of Users";
    public static final String WORKBOOK_SHEET_NAME = "Users";

    public static final String TITLE_CELL_STYLE = "titleCellStyle";
    public static final String HEADER_CELL_STYLE = "headerCellStyle";
    public static final String DATA_CELL_STYLE = "dataCellStyle";

    public Map<String, XSSFCellStyle> styles = null;

    @Override
    public void export(List<User> listOfUsers, HttpServletResponse response) {

        String fileName = null;

        try(XSSFWorkbook workbook = new XSSFWorkbook(); ServletOutputStream outputStream = response.getOutputStream()) {

            fileName = super.createFileName(AbstractExporter.EXCEL_FILE_EXT);
            super.setResponseHeader(response, MediaType.APPLICATION_OCTET_STREAM_VALUE, fileName);

            styles = createStyles(workbook);

            XSSFSheet usersSheet = workbook.createSheet(WORKBOOK_SHEET_NAME);
            writeTitleLine(usersSheet);
            writeHeaderLine(usersSheet);
            writeDataLine(usersSheet, listOfUsers);
            autoSizeColumns(usersSheet);

            workbook.write(outputStream);

        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to generate report: " + fileName, ex);
        }
    }

    private void writeTitleLine(XSSFSheet sheet) {
        //title row
        Row titleRow = sheet.createRow(0);
        titleRow.setHeightInPoints(TITLE_ROW_HEIGHT);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue(WORKBOOK_SHEET_TITLE);
        titleCell.setCellStyle(styles.get(TITLE_CELL_STYLE));
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$1:$F$1"));
    }

    private void writeHeaderLine(XSSFSheet sheet) {

        XSSFRow row = sheet.createRow(1);

        XSSFCellStyle headerCellStyle = styles.get(HEADER_CELL_STYLE);

        for (int col = 0; col < AbstractExporter.USERS_EXPORT_LABELS.length; col++) {
            createCell(row, headerCellStyle, col, AbstractExporter.USERS_EXPORT_LABELS[col]);
        }
    }

    private void writeDataLine(XSSFSheet usersSheet, List<User> listOfUsers) {

        XSSFCellStyle dataCellStyle = styles.get(DATA_CELL_STYLE);

        // since title row=0 and header row index = 1
        int rowIndex = 2;

        for (User user : listOfUsers) {

            int columnIndex = 0;
            XSSFRow row = usersSheet.createRow(rowIndex++);
            createCell(row, dataCellStyle, columnIndex++, user.getId());
            createCell(row, dataCellStyle, columnIndex++, user.getEmail());
            createCell(row, dataCellStyle, columnIndex++, user.getFirstName());
            createCell(row, dataCellStyle, columnIndex++, user.getLastName());
            createCell(row, dataCellStyle, columnIndex++, user.getRoles().toString());
            createCell(row, dataCellStyle, columnIndex, user.isEnabled());
        }
    }

    private void createCell(XSSFRow row, XSSFCellStyle cellStyle, int columnIndex, Object cellValue) {

        XSSFCell cell = row.createCell(columnIndex);

        if (cellValue instanceof Integer) {
            cell.setCellValue((Integer)cellValue);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean)cellValue);
        } else {
            cell.setCellValue((String) cellValue);
        }
        cell.setCellStyle(cellStyle);
    }

    private void autoSizeColumns(XSSFSheet sheet) {

        for (int col = 0; col < AbstractExporter.USERS_EXPORT_LABELS.length; col++) {
            sheet.autoSizeColumn(col);
        }
    }

    /**
     * Create a library of cell styles
     */
    private static Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb){

        Map<String, XSSFCellStyle> styles = new HashMap<>();
        XSSFCellStyle style;

        XSSFFont titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short) TITLE_CELL_FONT_SIZE);
        titleFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put(TITLE_CELL_STYLE, style);

        XSSFFont monthFont = wb.createFont();
        //monthFont.setFontHeightInPoints((short)HEADER_CELL_FONT_SIZE);
        monthFont.setFontHeight(HEADER_CELL_FONT_SIZE);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        style.setWrapText(true);
        styles.put(HEADER_CELL_STYLE, style);

        XSSFFont dataFont = wb.createFont();
        //dataFont.setFontHeightInPoints((short)DATA_CELL_FONT_SIZE);
        dataFont.setFontHeight(DATA_CELL_FONT_SIZE);
        dataFont.setColor(IndexedColors.BLACK.getIndex());
        style = wb.createCellStyle();
        style.setFont(dataFont);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        styles.put(DATA_CELL_STYLE, style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
        styles.put("formula_2", style);

        return styles;
    }

}
