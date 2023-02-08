package com.example.excelreader.sabteAsnad.paya;
import com.example.excelreader.cache.Start;
import com.example.excelreader.sabteAsnad.Helper;
import com.example.excelreader.utility.FTPUtils;
import com.example.excelreader.utility.ZipUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

@Service
@Slf4j
public class PayaService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;
    private Start start;

    private String shaparakDirectory;


    public PayaService(Start start) {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("پایا");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;
        this.start = start;
    }

    @Scheduled(cron = "${sabt.create.output.file.cron.job}")
    public void createPayaExcel() throws SQLException, IOException {

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

        long sum = tasviePaya + shaparakFromFtp();

        if ((sum) < 0) {
            payaManfi(sum);
        }
        else {
            payaMosbat(sum);
        }
        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\Paya.xlsx");
        this.workbook.write(fos);
        this.workbook.close();

    }

    public Long shaparakFromFtp() throws IOException {
        boolean doneISCFTP = false;
        log.info("Try connect to ISC FTP.");
        FTPUtils iscFTP = new FTPUtils();
        if (iscFTP.connect((String) start.cache.get("isc.ip"), (Integer) start.cache.get("isc.port"))) {
            if (iscFTP.login((String) start.cache.get("isc.username"), (String) start.cache.get("isc.password"))) {
                if (iscFTP.downloadFile("iscHostPath", "D:\\Aria_files\\aria_download_files")) {
                    doneISCFTP = true;
                }
            }
            iscFTP.disconnect();
        }

        if (doneISCFTP) {
            Path source = Paths.get("D:\\Aria_files\\aria_download_files");
            Path target = Paths.get(shaparakDirectory);
            ZipUtils.extractFile(source,target,"fileName");
            return shaparakAmount(target);
        }
        else {
            throw new RemoteException("Error in incomming shaparak transaction file");
        }
    }

    public Long shaparakAmount(Path directory) throws IOException {
        InputStream inputStream = new FileInputStream(directory.toString());
        return Long.parseLong(
                new XSSFWorkbook(inputStream)
                        .getSheetAt(0).getRow(3)
                        .getCell(2).getStringCellValue());
    }

    public void payaManfi (Long paya) {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند تسویه رقم پایا");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        Row row = sheet.createRow(this.rowCount++);
        row.createCell(column).setCellValue(j++);
        row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 1).setCellValue("8887");
        row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 2).setCellValue("02309004");
        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 3).setCellValue("حساب بین بانکی پایا پرداختی");
        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 4).setCellValue(String.valueOf(paya));
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue("");
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row2 = sheet.createRow(this.rowCount++);
        row2.createCell(column).setCellValue(j++);
        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 1).setCellValue("8887");
        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 2).setCellValue("12100202");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("حساب مرکز اسناد صادره سیستم متمرکز");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue("");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue(String.valueOf(paya));
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        sheet.createRow(this.rowCount++);
        Row row3 = sheet.createRow(this.rowCount++);
        row3.createCell(column).setCellValue(j++);
        row3.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 1).setCellValue("0650");
        row3.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 2).setCellValue("121000201");
        row3.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 3).setCellValue("حساب مرکز اسناد وارده سیستم متمرکز");
        row3.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 4).setCellValue(String.valueOf(paya));
        row3.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 5).setCellValue("");
        row3.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row4 = sheet.createRow(this.rowCount++);
        row4.createCell(column).setCellValue(j);
        row4.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 1).setCellValue("0650");
        row4.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 2).setCellValue("00800101");
        row4.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 3).setCellValue("حساب جاری نزد بانک مرکزی");
        row4.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 4).setCellValue("");
        row4.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 5).setCellValue(String.valueOf(paya));
        row4.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }

    public void payaMosbat (Long paya) {
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
        row.createCell(column + 4).setCellValue(paya);
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue("");
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row2 = sheet.createRow(this.rowCount++);
        row2.createCell(column).setCellValue(j++);
        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 1).setCellValue("0650");
        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 2).setCellValue("12100202");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("حساب مرکز اسناد صادره سیستم متمرکز");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue("");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue(paya);
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        sheet.createRow(this.rowCount++);
        Row row3 = sheet.createRow(this.rowCount++);
        row3.createCell(column).setCellValue(j++);
        row3.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 1).setCellValue("8887");
        row3.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 2).setCellValue("121000202");
        row3.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 3).setCellValue("حساب مرکز اسناد وارده سیستم متمرکز");
        row3.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 4).setCellValue(paya);
        row3.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 5).setCellValue("");
        row3.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
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
        row4.createCell(column + 5).setCellValue(paya);
        row4.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }


}
