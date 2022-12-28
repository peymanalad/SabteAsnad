package com.example.excelreader.sabteAsnad.beyneBanki.service;

import com.example.excelreader.sabteAsnad.Helper;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;


@Service
public class BeyneBankiService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;

    public BeyneBankiService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("بین بانکی");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;

    }

    public void createBeyneBankiExcel() throws SQLException, IOException {
        String varede = "select aria.amount from asnad.aria as aria inner join " +
                "asnad.b2bvarede_entity as varede on " +
                "aria.trn = varede.transaction_id and aria.amount = varede.amount" +
                "where aria.original_message_type = 'MQ202' "
            +   "and aria.message_status = 'Settled' " +
                "and aria.credit_party = 'NOORIRTHXXX' and aria.value_date = ?";

        String sadere = "select aria.amount from asnad.aria as aria inner join " +
                "asnad.b2bsadere_entity as sadere on " +
                "aria.trn = sadere.transaction_id and aria.amount = sadere.amount" +
                "where aria.original_message_type = 'MQ202'"
            +   "and aria.message_status = 'Settled' " +
                "and aria.debit_party = 'NOORIRTHXXX' and aria.value_date = ?";

        PreparedStatement sadereStatement = Helper.getConnection().prepareStatement(sadere);
        PreparedStatement varedeStatement = Helper.getConnection().prepareStatement(varede);
        sadereStatement.setString(1,Helper.getYesterdayDate());
        varedeStatement.setString(1,Helper.getYesterdayDate());
        ResultSet sadereResultSet = sadereStatement.executeQuery();
        ResultSet varedeResultSet = varedeStatement.executeQuery();
        List<Long> varedeAmount = new ArrayList<>();
        List<Long> sadereAmount = new ArrayList<>();

        while (varedeResultSet.next()) {
            varedeAmount.add(varedeResultSet.getLong("amount"));
        }
        while(sadereResultSet.next()) {
            sadereAmount.add(sadereResultSet.getLong("amount"));
        }
        receiveFacilities(varedeAmount);
        payOffFacilities(sadereAmount);
        payOffFacilitiesInterest(sadereResultSet);
        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\BeyneBanki.xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }

    public void receiveFacilities(List<Long> varede) throws SQLException {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند دریافت تسهیلات در سامانه نامی");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        Row row = sheet.createRow(this.rowCount++);
        row.createCell(column).setCellValue(j++);
        row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 1).setCellValue("0650");
        row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 2).setCellValue("");
        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 3).setCellValue("");
        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 4).setCellValue("");
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue("");
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 6).setCellValue("");
        row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row2 = sheet.createRow(this.rowCount++);
        row2.createCell(column).setCellValue(j++);
        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 1).setCellValue("");
        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 2).setCellValue("");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue("");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue("");
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 6).setCellValue("");
        row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }

    public void payOffFacilities(List<Long> sadere) throws SQLException {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند تسهیلات دریافتی در سامانه نامی");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        Row row = sheet.createRow(this.rowCount++);
        row.createCell(column).setCellValue(j++);
        row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 1).setCellValue("0650");
        row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 2).setCellValue("");
        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 3).setCellValue("");
        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 4).setCellValue("");
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
        row2.createCell(column + 2).setCellValue("");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue("");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue("");
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 6).setCellValue("");
        row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }

    public void payOffFacilitiesInterest(ResultSet sadere) {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند سود تسهیلات دریافتی در سامانه نامی");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
    }


}
