package com.example.excelreader.sabteAsnad.karmozd.paya;

import com.example.excelreader.sabteAsnad.Helper;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

@Service
public class KarmozdPayaService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;

    public KarmozdPayaService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("کارمزد پایا");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;

    }

    @Scheduled(cron = "${sabt.create.output.file.cron.job}")
    public void createKarmozdPayaExcel() throws SQLException, IOException {

        String query = "select sum(amount) from asnad.aria "
            + "where original_message_type = 'MLN' "
            + "and submitter = 'BMJIIRTHACH' "
            + "and trn like '000921C0%'";

        String payaDebit = query + "and debit_party = 'NOORIRTHXXX' and value_date = ?";

        String payaCredit = query + "and credit_party = 'NOORIRTHXXX' and value_date = ?";

        PreparedStatement debitStatement = Helper.getConnection().prepareStatement(payaDebit);
        PreparedStatement creditStatement = Helper.getConnection().prepareStatement(payaCredit);
        debitStatement.setString(1,Helper.getYesterdayDate());
        creditStatement.setString(1,Helper.getYesterdayDate());
        ResultSet debitResultSet = debitStatement.executeQuery();
        ResultSet creditResultSet = creditStatement.executeQuery();
        debitResultSet.next();
        creditResultSet.next();
        Long karmozdAmount = debitResultSet.getLong(1) - creditResultSet.getLong(1);

        karmozd(karmozdAmount);

        FileOutputStream fos = new FileOutputStream(".xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }

    public void karmozd(Long karmozd) {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند کارمزد دریافتی پایا");
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
        row.createCell(column + 2).setCellValue("52809103");
        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 3).setCellValue("کارمزد پرداختی حواله پایا");
        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 4).setCellValue(String.valueOf(karmozd));
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue("");
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row2 = sheet.createRow(this.rowCount++);
        row2.createCell(column).setCellValue(j);
        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 1).setCellValue("0650");
        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 2).setCellValue("00800101");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("حساب جاری نزد بانک مرکزی");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue("");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue(String.valueOf(karmozd));
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }
}
