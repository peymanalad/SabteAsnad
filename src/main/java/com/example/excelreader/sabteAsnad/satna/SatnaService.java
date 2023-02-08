package com.example.excelreader.sabteAsnad.satna;

import com.example.excelreader.sabteAsnad.Helper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class SatnaService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;

    public SatnaService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("ساتنا");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;
    }

    @Scheduled(cron = "${sabt.create.output.file.cron.job}")
    public void createSatnaExcel() throws SQLException, IOException {

        String satnaVarede = "select sum (amount) from asnad.aria as aria " +
                "where aria.original_message_type = 'MQ103' " +
                "and aria.credit_party = 'NOORIRTHXXX' " +
                "and aria.message_status = 'Settled'" +
                "and aria.value_date = ?";

        String gl = "select total_credit_amount from gl as gl "
            + "where gl.organization_code = '8890' and gl.heading_code = '11500815'"
            + "and ledger_date = ?";

        PreparedStatement varedeStatement = Helper.getConnection().prepareStatement(satnaVarede);
        varedeStatement.setString(1,Helper.getYesterdayDate());
        PreparedStatement glStatement = Helper.sedanDataConnection().prepareStatement(gl);
        glStatement.setString(1,Helper.getYesterdayDate().replace("/",""));
        ResultSet varedeResultSet = varedeStatement.executeQuery();
        ResultSet glResultSet = glStatement.executeQuery();
        varedeResultSet.next();
        glResultSet.next();
        Long ariaAmount = varedeResultSet.getLong(1);
        Long glAmount = glResultSet.getLong(1);

        if (ariaAmount.equals(glAmount)) {
            satnaVarede(ariaAmount);
        }
        else {
            log.warn("مغایرت مقدار درج شده در فایل آریا و GL");

        }
        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\Satna.xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }

    public void satnaVarede(Long ariaAmount) {
        sheet.createRow(this.rowCount++).createCell(0).setCellValue("ثبت سند حسابداری تسویه رقم تراکنش های وارده ساتنا");
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
        row.createCell(column + 4).setCellValue(String.valueOf(ariaAmount));
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
        row2.createCell(column + 5).setCellValue(String.valueOf(ariaAmount));
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        sheet.createRow(this.rowCount++);
        Row row3 = sheet.createRow(this.rowCount++);
        row3.createCell(column).setCellValue(j++);
        row3.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 1).setCellValue("8890");
        row3.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 2).setCellValue("121000202");
        row3.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 3).setCellValue("حساب مرکز اسناد وارده سیستم متمرکز");
        row3.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 4).setCellValue(String.valueOf(ariaAmount));
        row3.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row3.createCell(column + 5).setCellValue("");
        row3.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row4 = sheet.createRow(this.rowCount++);
        row4.createCell(column).setCellValue(j);
        row4.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 1).setCellValue("8890");
        row4.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 2).setCellValue("11500815");
        row4.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 3).setCellValue("بدهکاران داخلی ساتنا");
        row4.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 4).setCellValue("");
        row4.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row4.createCell(column + 5).setCellValue(String.valueOf(ariaAmount));
        row4.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
    }






}
