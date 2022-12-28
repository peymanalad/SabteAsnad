package com.example.excelreader.sabteAsnad.chakavak.service;

import com.example.excelreader.sabteAsnad.Helper;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

@Service
public class ChakavakService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;

    public ChakavakService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("چکاوک");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;

    }

    public void createChakavakExcel() throws SQLException, IOException {

        String chakavak = "select debit_party,amount,value_date from asnad.aria as aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and aria.submitter = 'BMJIIRTHCIS' and aria.value_date in (?,?)";

        PreparedStatement chakavakStatement = Helper.getConnection().prepareStatement(chakavak);
        if (Helper.getDayOfWeek() == 0 || Helper.getDayOfWeek() == 6) {
            System.out.println("تراکنش چکاوک برای این روز ثبت نمیشود");
            return;
        }
        else if (Helper.getDayOfWeek() == 5) {
            chakavakStatement.setString(1,Helper.getYesterdayDate());
            chakavakStatement.setString(2,Helper.getTodayDate());
        }
        else {
            chakavakStatement.setString(1,Helper.getYesterdayDate());
            chakavakStatement.setString(2,Helper.getYesterdayDate());
        }
        ResultSet chakavakResultSet = chakavakStatement.executeQuery();

        chakavakLiquidation(chakavakResultSet);

        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\Excel.xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }

    public void chakavakLiquidation(ResultSet resultSet) throws SQLException {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند تسویه چکاوک در سامانه نامی");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        while (resultSet.next()) {
            Row row = sheet.createRow(this.rowCount++);
            row.createCell(column).setCellValue(j++);
            row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 1).setCellValue("0650");
            row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 2).setCellValue("00800101");
            row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 3).setCellValue("حساب جاری نزد بانک مرکزی");
            row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 4).setCellValue(Helper.getYesterdayDate()+"تسویه سند بین بانکی چکاوک ");
            row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 5).setCellValue("");
            row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 6).setCellValue("");
            row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
            Row row2 = sheet.createRow(this.rowCount++);
            row2.createCell(column).setCellValue(j++);
            row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 1).setCellValue("0650");
            row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 2).setCellValue("02309007");
            row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 3).setCellValue("بین بانکها(حساب ما)چکاوک");
            row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 4).setCellValue(Helper.getYesterdayDate()+"تسویه سند بین بانکی چکاوک ");
            row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 5).setCellValue("");
            row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row2.createCell(column + 6).setCellValue(String.valueOf(resultSet.getLong(2)));
            row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        }
    }


}
