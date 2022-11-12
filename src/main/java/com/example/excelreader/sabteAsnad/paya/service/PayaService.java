package com.example.excelreader.sabteAsnad.paya.service;
import com.example.excelreader.sabteAsnad.Helper;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

@Service
public class PayaService {

    private final ImportPayaTablesService service;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;
    private Long shaparak;


    public PayaService(ImportPayaTablesService service) {
        this.service = service;
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("پایا");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;
        this.shaparak = 0L;
    }

    public void saveIncomingRegTransaction(MultipartFile multipartFile) {
        service.breakIncomingRegularTransaction(multipartFile);
    }

    public void saveIncomingRetTransaction(MultipartFile multipartFile) {
        service.saveIncomingReturnedTransaction(multipartFile);
    }

    public void saveOutTransaction(MultipartFile multipartFile) {
        service.breakOutgoingTransaction(multipartFile);
    }

    public void saveRejectTransaction(MultipartFile multipartFile) {
        service.saveRejectedTransaction(multipartFile);
    }

    public void saveRetRegTransaction(MultipartFile multipartFile) {
        service.breakReturnRegularTransaction(multipartFile);
    }

    public void saveRetShaparakTransaction(MultipartFile multipartFile) {
        service.breakReturnedShaparakTransaction(multipartFile);
    }

    public void createPayaExcel() throws SQLException, IOException {

        /*String sumOf128Query = "select sum(amount) from (select sum(amount) from asnad.incoming_regular_transaction " +
                "union all select sum(amount) from asnad.incoming_returned_transaction " +
                "union all select sum(amount) from asnad.rejected_transaction)";

        String sumOf61112Query = "select sum from (select sum(amount) from asnad.outgoing_transaction " +
                "union all select sum(amount) from asnad.returned_regular_transaction " +
                "union all select sum(amount) from asnad.returned_shaparak_transaction)";

        PreparedStatement firstSumStatement = Helper.getConnection().prepareStatement(sumOf128Query);
        PreparedStatement secondSumStatement = Helper.getConnection().prepareStatement(sumOf61112Query);
        ResultSet firstSumResultSet = firstSumStatement.executeQuery();
        ResultSet seconSumResultSet = secondSumStatement.executeQuery();
        Long firstSum = firstSumResultSet.getLong(1);
        Long secondSum = seconSumResultSet.getLong(1);
        if ((firstSum - secondSum) >= 0) {
            varedeMoreThanSadere(String.valueOf(firstSum));
        }
        else sadereMoreThanVarede(String.valueOf(secondSum));*/

        String payaDebit = "select sum(amount) from asnad.aria " +
                "where original_message_type = 'MLN' and submitter = 'BMJIIRTHACH' " +
                "and trn like '000921C0%' and debit_party = 'NOORIRTHXXX' and value_date = ?";

        String payaCredit = "select sum(amount) from asnad.aria " +
                "where original_message_type = 'MLN' and submitter = 'BMJIIRTHACH' " +
                "and trn like '000921C0%' and credit_party = 'NOORIRTHXXX' and value_date = ?";

        PreparedStatement payaDebitStatement = Helper.getConnection().prepareStatement(payaDebit);
        payaDebitStatement.setString(1,Helper.getYesterdayDate());
        ResultSet debitResultSet = payaDebitStatement.executeQuery();
        PreparedStatement payaCreditStatement = Helper.getConnection().prepareStatement(payaCredit);
        payaCreditStatement.setString(1,Helper.getYesterdayDate());
        ResultSet creditResultSet = payaCreditStatement.executeQuery();
        debitResultSet.next();
        creditResultSet.next();
        Long tasviePaya = debitResultSet.getLong(1) - creditResultSet.getLong(1);



        paya(tasviePaya + this.shaparak);

        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\Paya.xlsx");
        this.workbook.write(fos);
        this.workbook.close();

    }

    public void amountFromShaparak(MultipartFile multipartFile) throws IOException {
         this.shaparak = Long.parseLong(
                new XSSFWorkbook(multipartFile.getInputStream())
                        .getSheetAt(0).getRow(3)
                        .getCell(2).getStringCellValue());
    }

    public void paya (Long paya) {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند تسویه رقم پایا");
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
        row.createCell(column + 2).setCellValue("00800101");
        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 3).setCellValue("حساب جاری نزد بانک مرکزی");
        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 4).setCellValue("");
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue(paya);
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 6).setCellValue("");
        row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row2 = sheet.createRow(this.rowCount++);
        row2.createCell(column).setCellValue(j++);
        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 1).setCellValue("0650");
        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 2).setCellValue("52809103");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue("کارمزد دریافتی");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue("");
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 6).setCellValue(paya);
        row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        sheet.createRow(this.rowCount++);
        Row row3 = sheet.createRow(this.rowCount++);
        row3.createCell(column).setCellValue(j++);
        row3.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 1).setCellValue("8887");
        row3.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 2).setCellValue("121000202");
        row3.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 3).setCellValue("حساب مرکز اسناد صادره سیستم متمرکز");
        row3.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 4).setCellValue("");
        row3.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 5).setCellValue(paya);
        row3.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 6).setCellValue("");
        row3.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row4 = sheet.createRow(this.rowCount++);
        row4.createCell(column).setCellValue(j);
        row4.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 1).setCellValue("8887");
        row4.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 2).setCellValue("02309004");
        row4.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 3).setCellValue("حساب بین بانکی پایا پرداختی");
        row4.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 4).setCellValue("");
        row4.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 5).setCellValue("");
        row4.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 6).setCellValue(paya);
        row4.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }

//    public void sadereMoreThanVarede(String value) {
//        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند تسویه رقم پایا");
//        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
//        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
//        rowCount++;
//        int j = 1;
//        int column = 0;
//        Row row = sheet.createRow(this.rowCount++);
//        row.createCell(column).setCellValue(j++);
//        row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 1).setCellValue("8887");
//        row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 2).setCellValue("02309004");
//        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 3).setCellValue("حساب بین بانکی پایا پرداختی");
//        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 4).setCellValue("");
//        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 5).setCellValue(value);
//        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 6).setCellValue("");
//        row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        Row row2 = sheet.createRow(this.rowCount++);
//        row2.createCell(column).setCellValue(j++);
//        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 1).setCellValue("8887");
//        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 2).setCellValue("121000202");
//        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 3).setCellValue("حساب مرکز اسناد صادره سیستم متمرکز");
//        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 4).setCellValue("");
//        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 5).setCellValue("");
//        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 6).setCellValue(value);
//        row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        sheet.createRow(this.rowCount++);
//        Row row3 = sheet.createRow(this.rowCount++);
//        row3.createCell(column).setCellValue(j++);
//        row3.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 1).setCellValue("650");
//        row3.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 2).setCellValue("121000202");
//        row3.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 3).setCellValue("حساب مرکز اسناد صادره سیستم متمرکز");
//        row3.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 4).setCellValue("");
//        row3.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 5).setCellValue(value);
//        row3.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 6).setCellValue("");
//        row3.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        Row row4 = sheet.createRow(this.rowCount++);
//        row4.createCell(column).setCellValue(j);
//        row4.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 1).setCellValue("650");
//        row4.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 2).setCellValue("00800101");
//        row4.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 3).setCellValue("حساب جاری نزد بانک مرکزی");
//        row4.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 4).setCellValue("");
//        row4.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 5).setCellValue("");
//        row4.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 6).setCellValue(value);
//        row4.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));

//    }
//    public void varedeMoreThanSadere(String value) {
//        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند تسویه رقم پایا");
//        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
//        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
//        rowCount++;
//        int j = 1;
//        int column = 0;
//        Row row = sheet.createRow(this.rowCount++);
//        row.createCell(column).setCellValue(j++);
//        row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 1).setCellValue("0650");
//        row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 2).setCellValue("00800101");
//        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 3).setCellValue("حساب جاری نزد بانک مرکزی");
//        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 4).setCellValue("");
//        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 5).setCellValue(value);
//        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row.createCell(column + 6).setCellValue("");
//        row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        Row row2 = sheet.createRow(this.rowCount++);
//        row2.createCell(column).setCellValue(j++);
//        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 1).setCellValue("0650");
//        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 2).setCellValue("52809103");
//        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 3).setCellValue("");
//        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 4).setCellValue("کارمزد دریافتی");
//        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 5).setCellValue("");
//        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row2.createCell(column + 6).setCellValue(value);
//        row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        sheet.createRow(this.rowCount++);
//        Row row3 = sheet.createRow(this.rowCount++);
//        row3.createCell(column).setCellValue(j++);
//        row3.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 1).setCellValue("8887");
//        row3.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 2).setCellValue("121000202");
//        row3.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 3).setCellValue("حساب مرکز اسناد صادره سیستم متمرکز");
//        row3.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 4).setCellValue("");
//        row3.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 5).setCellValue(value);
//        row3.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row3.createCell(column + 6).setCellValue("");
//        row3.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        Row row4 = sheet.createRow(this.rowCount++);
//        row4.createCell(column).setCellValue(j++);
//        row4.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 1).setCellValue("8887");
//        row4.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 2).setCellValue("02309004");
//        row4.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 3).setCellValue("حساب بین بانکی پایا پرداختی");
//        row4.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 4).setCellValue("");
//        row4.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 5).setCellValue("");
//        row4.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
//        row4.createCell(column + 6).setCellValue(value);
//        row4.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));

//    }


}
