package com.example.excelreader.sabteAsnad.shetab.service;

import com.example.excelreader.sabteAsnad.Helper;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

@Service
public class ShetabService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;

    public ShetabService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("شتاب");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;

    }

    public void createKarmozdPayaExcel() throws SQLException, IOException {

        String beyneBanki = "select * from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHSHB' and debit_party = 'NOORIRTHXXX' " +
                "and trn like '%2234'";

        String shetabBedehkar = "select * from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHSHB' and debit_party = 'NOORIRTHXXX'";

        String shetabBestankar = "select * from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHSHB' and credit_party = 'NOORIRTHXXX'";

        PreparedStatement beyneBankiStatement = Helper.getConnection().prepareStatement(beyneBanki);
        PreparedStatement bedehkarStatement = Helper.getConnection().prepareStatement(shetabBedehkar);
        PreparedStatement bestankarStatement = Helper.getConnection().prepareStatement(shetabBestankar);
        ResultSet beyneBankiResultSet = beyneBankiStatement.executeQuery();
        ResultSet bdehkarResultSet = bedehkarStatement.executeQuery();
        ResultSet bestankarResultSet = bestankarStatement.executeQuery();

        beyneBanki();
        karmozd();
        moghayerat();

        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\Excel.xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }

    public void beyneBanki() {

    }

    public void karmozd() {

    }

    public void moghayerat() {

    }

}
