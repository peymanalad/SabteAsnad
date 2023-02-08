package com.example.excelreader.sabteAsnad;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Slf4j
public class Helper {

    private static final String[] HEADERS = {"ردیف", "شعبه", "کدحساب", "شرح حساب", "شرح آرتیکل", "بدهکار", "بستانکار"};

    public static Boolean checkFormat(Path path){
        File file = path.toFile();
        String fileName = file.getName();
        return fileName.endsWith(".xlsx");
    }

    public static List<String> getAsList (Path path, Integer lineNumber)
        throws FileNotFoundException {
        log.info("Try to exchange excel file to list");
        XSSFWorkbook workbook = null;
        DataFormatter dataFormatter = new DataFormatter();
        List<String> list = new ArrayList<>();
        InputStream inputStream = new FileInputStream(path.toString());

        try {
            workbook = new XSSFWorkbook(inputStream);
        } catch (EncryptedDocumentException | IOException e) {
            e.printStackTrace();
        }
        int j = 0;
        Sheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if (j > lineNumber) {
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
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
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
        header.setHeight((short) 400);
        for (int i = 0; i < HEADERS.length; i++) {
            sheet.setColumnWidth(i,25 * 300);
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

    public static Connection sedanDataConnection() throws SQLException {
        return DriverManager
            .getConnection("jdbc:oracle:thin:@10.1.11.26:1521/club",
                "HAZINE",
                "1qaz2wsx");
    }

    public static String getYesterdayDate() {
        ULocale locale = new ULocale("fa");
        Calendar calendar = Calendar.getInstance(locale);
        String day = String.valueOf(calendar.get(Calendar.DATE));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        DateFormat df = DateFormat.getDateInstance(DateFormat.DATE_FIELD,locale);
        String date = df.format(calendar.getTime()).replace("/","");
        String newDecimal = String.valueOf(new BigDecimal(date));
        String month = newDecimal.replace(year, "").replace(day, "");
        int m = Integer.parseInt(month);
        if (Integer.parseInt(day) == 1) {
            if (2<=m && m<=7) {
                m -= 1;
                day = "31";
                return year + "/0" + m + "/" + day;
            }
            else if (8<=m && m<=12) {
                m -= 1;
                day = "30";
                if (m < 10) {
                    return year + "/0" + m + "/" + day;
                }
                else
                    return year + "/" + m + "/" + day;
            }
            else if (m == 1) {
                int y = Integer.parseInt(year);
                m = 12;
                y -= 1;
                if (((y % 4 == 0) && (y % 100 != 0)) || (y % 400 == 0)) {
                    day = "30";
                }
                else {
                    day = "29";
                }
                return y + "/" + m + "/" + day;
            }
        }
        if (month.length() == 1) {
            return year + "/0" + m + "/" + (Integer.parseInt(day)-1);
        }
        else return year + "/" + m + "/" + (Integer.parseInt(day)-1);
    }

    public static String getDateWithoutSlash(String date) {
        return date.replace("/","");
    }

    public static String getTodayDate() {
        ULocale locale = new ULocale("fa");
        Calendar calendar = Calendar.getInstance(locale);
        String day = String.valueOf(calendar.get(Calendar.DATE));
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        DateFormat df = DateFormat.getDateInstance(DateFormat.DATE_FIELD,locale);
        String date = df.format(calendar.getTime()).replace("/","");
        String newDecimal = String.valueOf(new BigDecimal(date));
        String month = newDecimal.replace(year, "").replace(day, "");
        if (month.length() == 1) {
            return year + "/0" + month + "/" + day;
        }
        else return year + "/" + month + "/" + day;
    }

    public static int getDayOfWeek() {
        ULocale locale = new ULocale("fa");
        Calendar calendar = Calendar.getInstance(locale);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

}
