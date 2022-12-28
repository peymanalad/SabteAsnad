package com.example.excelreader.sabteAsnad.shetab.service;

import com.example.excelreader.init.Start;
import com.example.excelreader.sabteAsnad.Helper;
import com.example.excelreader.utility.FTPUtils;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.aspectj.weaver.tools.ISupportsMessageContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;

@Service
@Slf4j
public class ShetabService {

    @Value("${ftp.username}")
    private String username;
    @Value("${ftp.password}")
    private String password;
    @Value("${ftp.host}")
    private String host;
    @Value("${ftp.port}")
    private String port;
    @Value("${cfg.file.path}")
    private String ftpPath;
    @Value("${cfg.vsh.name}")
    private String vsh;
    @Value("${cfg.local}")
    private String localDirectory;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;
    private Start start;
    private String yesterday;
    private String today;

    public ShetabService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("شتاب");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;
        this.yesterday = Helper.getDateWithoutSlash(Helper.getYesterdayDate());
        this.today = Helper.getDateWithoutSlash(Helper.getTodayDate());

    }

    public Long accessFileFromFtp () throws Exception {
        boolean doneISCFTP = false;
        String prefix = "noor"+Helper.getYesterdayDate().replace("/","")+vsh;
        FTPUtils iscFTP = new FTPUtils();
        if (iscFTP.connect((String) start.cache.get(host), (Integer) start.cache.get(port))) {
            if (iscFTP.login((String) start.cache.get(username), (String) start.cache.get(password))) {
                if (iscFTP.downloadFile(ftpPath + prefix + vsh , localDirectory)) {
                    doneISCFTP = true;
                }
            }
            iscFTP.disconnect();
        }

        if (doneISCFTP) {
            String sourcePath = localDirectory + File.separator + prefix + vsh;
            return unzipFileAndCalculatevshAmount(sourcePath);
        }
        else {
            log.warn("File does not exist for download");
            throw new Exception();
        }

        }

    public Long unzipFileAndCalculatevshAmount(String source) throws IOException {
        ZipFile zipFile = new ZipFile(source);
        BufferedInputStream buffer;
        ZipInputStream zipInput;
        buffer = new BufferedInputStream(new FileInputStream(source));
        zipInput = new ZipInputStream(buffer);
        ZipEntry e;
        long sum = 0L;
        while ((e = zipInput.getNextEntry()) != null) {
            if (e.getName().equals(source)) {
                InputStream inputStream = zipFile.getInputStream(e);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] array = line.split("\\|");
                    sum += Long.parseLong(array[10]);
                }
            }
        }
        return sum;
    }

    public void createShetabExcel() throws Exception {

        String beyneBanki = "select amount from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHSHB' and debit_party = 'NOORIRTHXXX' " +
                "and trn like '%2234' and value_date = ?";

        String shetabBedehkar = "select amount from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHSHB' and debit_party = 'NOORIRTHXXX'" +
                "and value_date = ?";

        String shetabBestankar = "select amount from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHSHB' and credit_party = 'NOORIRTHXXX'" +
                "and value_date = ?";

        String shb2230 = "select amount from asnad.aria "
            + "where original_message_type = 'MLN' and message_status = 'Settled' "
            + "and submitter = 'BMJIIRTHSHB' and credit_party = 'NOORIRTHXXX' "
            + "and trn like '%2230' and value_date = ?";

        Long vshAmount = accessFileFromFtp();

        PreparedStatement beyneBankiStatement = Helper.getConnection().prepareStatement(beyneBanki);
        PreparedStatement bedehkarStatement = Helper.getConnection().prepareStatement(shetabBedehkar);
        PreparedStatement bestankarStatement = Helper.getConnection().prepareStatement(shetabBestankar);
        PreparedStatement shb2230Statement = Helper.getConnection().prepareStatement(shb2230);

        beyneBankiStatement.setString(1,Helper.getYesterdayDate());
        ResultSet beyneBankiResultSet = beyneBankiStatement.executeQuery();
        beyneBankiResultSet.next();
        Long beyneBankiAmount = beyneBankiResultSet.getLong("amount");

        bestankarStatement.setString(1,Helper.getYesterdayDate());
        ResultSet bestankarResultSet = bestankarStatement.executeQuery();

        bedehkarStatement.setString(1,Helper.getYesterdayDate());
        ResultSet bedehkarResultSet = bedehkarStatement.executeQuery();

        shb2230Statement.setString(1,Helper.getYesterdayDate());
        ResultSet shb2230ResultSet = shb2230Statement.executeQuery();
        beyneBankiResultSet.next();
        Long shb2230Amount = shb2230ResultSet.getLong("amount");

        List<Long> bestankarArray = new ArrayList<>();
        List<Long> bedehkarArray = new ArrayList<>();
        while (bestankarResultSet.next()) {
            bestankarArray.add(bestankarResultSet.getLong("amount"));
        }
        while (bedehkarResultSet.next()) {
            bedehkarArray.add(bedehkarResultSet.getLong("amount"));
        }

        //beyne banki
        if (beyneBankiAmount.equals(vshAmount)) {
            beyneBanki(beyneBankiAmount);
        }
        else {
            log.warn("There is Bank Reconciliation");
        }

        //karmozd
        karmozd(bestankarArray,bedehkarArray);
        //tasvie moghayerat
        tasvieMoghayerat(bestankarArray,shb2230Amount);

        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\Excel.xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }

    public void beyneBanki(Long value) {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند بین بانکی شتاب");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        Row row = sheet.createRow(this.rowCount++);
        row.createCell(column).setCellValue(j++);
        row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 1).setCellValue("8888");
        row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 2).setCellValue("02309001");
        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 3).setCellValue("حساب بین بانکها/از طریق مرکز .ش");
        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 4).setCellValue(yesterday + "ثبت سند بین بانکی شتاب مورخ ");
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue(String.valueOf(value));
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 6).setCellValue("");
        row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row2 = sheet.createRow(this.rowCount++);
        row2.createCell(column).setCellValue(j++);
        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 1).setCellValue("8888");
        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 2).setCellValue("12100202");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("هـ/متمرکز سیستم صادره");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue(yesterday + "ثبت سند بین بانکی شتاب مورخ ");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue("");
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 6).setCellValue(String.valueOf(value));
        row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row3 = sheet.createRow(this.rowCount++);
        row3.createCell(column).setCellValue(j++);
        row3.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 1).setCellValue("0650");
        row3.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 2).setCellValue("121000201");
        row3.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 3).setCellValue("هـ/متمرکز سیستم وارده");
        row3.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 4).setCellValue(today + "ثبت سند بین بانکی شتاب مورخ ");
        row3.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 5).setCellValue(String.valueOf(value));
        row3.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 6).setCellValue("");
        row3.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row4 = sheet.createRow(this.rowCount++);
        row4.createCell(column).setCellValue(j);
        row4.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 1).setCellValue("0650");
        row4.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 2).setCellValue("00800101");
        row4.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 3).setCellValue("حساب جاری نزد بانک 26850/50ش");
        row4.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 4).setCellValue(today + "ثبت سند بین بانکی شتاب مورخ ");
        row4.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 5).setCellValue("");
        row4.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 6).setCellValue(String.valueOf(value));
        row4.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row5 = sheet.createRow(this.rowCount++);
        row5.createCell(column + 4).setCellValue("جمع کل");
        row5.getCell(column + 4).setCellStyle(Helper.createHeaderStyle(headerStyle));
        row5.createCell(column + 5).setCellValue(String.valueOf(Long.parseLong(row.getCell(5).getStringCellValue()) + Long.parseLong(row3.getCell(5).getStringCellValue())));
        row5.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row5.createCell(column + 6).setCellValue(String.valueOf(Long.parseLong(row2.getCell(6).getStringCellValue()) + Long.parseLong(row4.getCell(6).getStringCellValue())));
        row5.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }

    public void karmozd(List<Long> bestankar,List<Long> bedehkkar) {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند بین بانکی شتاب");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        Row row = sheet.createRow(this.rowCount++);
        row.createCell(column).setCellValue(j++);
        row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 1).setCellValue("8888");
        row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 2).setCellValue("02309001");
        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 3).setCellValue("حساب بین بانکها/از طریق مرکز .ش");
        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 4).setCellValue(yesterday + "ثبت سند بین بانکی شتاب مورخ ");
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue(String.valueOf("value"));
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 6).setCellValue("");
        row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row2 = sheet.createRow(this.rowCount++);
        row2.createCell(column).setCellValue(j++);
        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 1).setCellValue("8888");
        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 2).setCellValue("12100202");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("هـ/متمرکز سیستم صادره");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue(yesterday + "ثبت سند بین بانکی شتاب مورخ ");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue("");
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 6).setCellValue(String.valueOf("value"));
        row2.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row3 = sheet.createRow(this.rowCount++);
        row3.createCell(column).setCellValue(j++);
        row3.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 1).setCellValue("0650");
        row3.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 2).setCellValue("121000201");
        row3.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 3).setCellValue("هـ/متمرکز سیستم وارده");
        row3.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 4).setCellValue(today + "ثبت سند بین بانکی شتاب مورخ ");
        row3.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 5).setCellValue(String.valueOf("value"));
        row3.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 6).setCellValue("");
        row3.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row4 = sheet.createRow(this.rowCount++);
        row4.createCell(column).setCellValue(j);
        row4.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 1).setCellValue("0650");
        row4.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 2).setCellValue("00800101");
        row4.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 3).setCellValue("حساب جاری نزد بانک 26850/50ش");
        row4.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 4).setCellValue(today + "ثبت سند بین بانکی شتاب مورخ ");
        row4.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 5).setCellValue("");
        row4.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 6).setCellValue(String.valueOf("value"));
        row4.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row5 = sheet.createRow(this.rowCount++);
        row5.createCell(column + 4).setCellValue("جمع کل");
        row5.getCell(column + 4).setCellStyle(Helper.createHeaderStyle(headerStyle));
        row5.createCell(column + 5).setCellValue(String.valueOf(Long.parseLong(row.getCell(5).getStringCellValue()) + Long.parseLong(row3.getCell(5).getStringCellValue())));
        row5.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row5.createCell(column + 6).setCellValue(String.valueOf(Long.parseLong(row2.getCell(6).getStringCellValue()) + Long.parseLong(row4.getCell(6).getStringCellValue())));
        row5.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }

    public void tasvieMoghayerat(List<Long> bestankar,Long shb) {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند تسویه مغایرت شتاب");
        sheet.getRow(this.rowCount - 1).getCell(0).setCellStyle(Helper.fontStyle(fontStyle,this.font));
        Helper.fillHeader(this.rowCount,this.sheet,this.headerStyle);
        rowCount++;
        int j = 1;
        int column = 0;
        Row row = sheet.createRow(this.rowCount++);
        row.createCell(column).setCellValue(1);
        row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 1).setCellValue("8888");
        row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 2).setCellValue("02309001");
        row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 3).setCellValue("حساب بین بانکها/از طریق مرکز .ش");
        row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 4).setCellValue(yesterday + "ثبت سند بین بانکی شتاب مورخ ");
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue(String.valueOf(shb));
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 6).setCellValue("");
        row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));

        fillRow(row,5,"8888",
            "11509005",
            "بدهکاران داخلی مغایرت شتاب"
            ,yesterday + "تسویه مغایرت شتابی مورخ ");

        fillRow(row,4,"8888",
            "02309001",
            "حساب بین بانکها/از طریق مرکز.ش"
            ,yesterday + "تسویه مغایرت شتابی مورخ ");

        sheet.getRow(31).getCell(6).setCellValue(String.valueOf(shb));
        for (int i = 0; i < 4; i++) {
            sheet.getRow(32 + i).getCell(5)
                .setCellValue(String.valueOf(bestankar.get(i)));
        }
        for (int i = 0; i < 4; i++) {
            sheet.getRow(36 + i).getCell(6)
                .setCellValue(String.valueOf(bestankar.get(i)));
        }
        row.createCell(column + 4).setCellValue("جمع کل");
        row.getCell(column + 4).setCellStyle(Helper.createHeaderStyle(headerStyle));
        row.createCell(column + 5).setCellValue(calculateSumOfTable(30,40,5));
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 6).setCellValue(calculateSumOfTable(30,40,6));
        row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }

    public void fillRow(Row row,Integer length,String branchCode,
        String accountCode,String accountDescription,String articleDescription) {
        int column = 0;
        for (int i = 2; i < length + 2; i++) {
            sheet.createRow(this.rowCount++);
            row.createCell(column).setCellValue(i++);
            row.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 1).setCellValue(branchCode);
            row.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 2).setCellValue(accountCode);
            row.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 3).setCellValue(accountDescription);
            row.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 4).setCellValue(articleDescription);
            row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 5).setCellValue("");
            row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
            row.createCell(column + 6).setCellValue("");
            row.getCell(column + 6).setCellStyle(Helper.createBodyStyle(bodyStyle));
        }
    }

    public Long calculateSumOfTable(Integer startRow,Integer endRow, Integer columnNumber) {
        long sum = 0L;
        for (int i = startRow; i < endRow; i++) {
            String cell = sheet.getRow(i).getCell(columnNumber).getStringCellValue();
            if (!cell.equals("")) {
                sum += Long.parseLong(cell);
            }
        }
        return sum;
    }
}
