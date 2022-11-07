package com.example.excelreader.sabteAsnad;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    private static final String[] HEADERS = {"ردیف", "شعبه", "کدحساب", "شرح حساب", "شرح آرتیکل", "بدهکار", "بستانکار"};

    public static Boolean checkFormat(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        return fileName.endsWith(".xlsx");
    }

    public static List<String> getAsList (MultipartFile multipartFile) {
        XSSFWorkbook workbook = null;
        DataFormatter dataFormatter = new DataFormatter();
        List<String> list = new ArrayList<>();
        try {

            workbook = new XSSFWorkbook(multipartFile.getInputStream());
        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
        }
        int j = 0;
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (j != 0) {
                for (Cell cell : row) {
                    String cellValue = dataFormatter.formatCellValue(cell);
                    list.add(cellValue);
                }
            }
            j++;
        }
        return list;
    }

    public static CellStyle createHeaderStyle(CellStyle cellStyle) {
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    public static CellStyle createBodyStyle(CellStyle cellStyle) {
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        return cellStyle;
    }

    public static CellStyle fontStyle(CellStyle cellStyle,Font font) {
        font.setBold(true);
        font.setFontHeight((short) 250);
        font.setColor(HSSFColor.HSSFColorPredefined.DARK_BLUE.getIndex());
        cellStyle.setFont(font);
        return cellStyle;
    }

    public static void fillHeader(Integer rowCount, XSSFSheet sheet,CellStyle headerStyle) {
        XSSFRow header = sheet.createRow(rowCount);
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.setColumnWidth(i,25 * 200);
            Cell cell = header.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(Helper.createHeaderStyle(headerStyle));
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager
                .getConnection("jdbc:mysql://localhost:3306/sabt",
                        "root",
                        "Pp135642!#%^$@");
    }

}
