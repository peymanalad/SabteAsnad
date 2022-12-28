package com.example.excelreader.sabteAsnad.karmozd.satna;

import com.example.excelreader.sabteAsnad.Helper;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class KarmozdSatnaService {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private CellStyle bodyStyle;
    private CellStyle headerStyle;
    private CellStyle fontStyle;
    private Font font;
    private Integer rowCount;

    public KarmozdSatnaService() {
        this.workbook = new XSSFWorkbook();
        this.headerStyle = workbook.createCellStyle();
        this.bodyStyle = workbook.createCellStyle();
        this.fontStyle = workbook.createCellStyle();
        this.font = workbook.createFont();
        this.sheet = workbook.createSheet("کارمزد ساتنا");
        sheet.getCTWorksheet().getSheetViews().getSheetViewArray(0).setRightToLeft(true);
        this.rowCount = 1;

    }

    public void createKarmozdSatnaExcel() throws SQLException, IOException {

        String karmozd = "select amount from asnad.aria " +
                "where original_message_type = 'MLN' and message_status = 'Settled' " +
                "and submitter = 'BMJIIRTHRTG' and value_date = ?";

        PreparedStatement karmozdStatement = Helper.getConnection().prepareStatement(karmozd);
        karmozdStatement.setString(1,Helper.getYesterdayDate());
        ResultSet karmozdResultSet = karmozdStatement.executeQuery();
        karmozdResultSet.next();
        Long karmozdAmount = karmozdResultSet.getLong("amount");
        karmozdSatna(karmozdAmount);

        FileOutputStream fos = new FileOutputStream("C:\\Users\\p.alad\\Desktop\\KarmozdSatna.xlsx");
        this.workbook.write(fos);
        this.workbook.close();
    }


    public void karmozdSatna(Long karmozd) throws SQLException {
        sheet.createRow(this.rowCount++).createCell(0)
            .setCellValue("ثبت سند کارمزد دریافتی ساتنا وارده");
        sheet.getRow(this.rowCount - 1).getCell(0)
            .setCellStyle(Helper.fontStyle(fontStyle, this.font));
        Helper.fillHeader(this.rowCount, this.sheet, this.headerStyle);
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
        row.createCell(column + 4).setCellValue(String.valueOf(karmozd));
        row.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row.createCell(column + 5).setCellValue("");
        row.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));
        Row row2 = sheet.createRow(this.rowCount++);
        row2.createCell(column).setCellValue(j);
        row2.getCell(column).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 1).setCellValue("0650");
        row2.getCell(column + 1).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 2).setCellValue("48009102");
        row2.getCell(column + 2).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 3).setCellValue("کارمزد دریافتی");
        row2.getCell(column + 3).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 4).setCellValue("");
        row2.getCell(column + 4).setCellStyle(Helper.createBodyStyle(bodyStyle));
        row2.createCell(column + 5).setCellValue(String.valueOf(karmozd));
        row2.getCell(column + 5).setCellStyle(Helper.createBodyStyle(bodyStyle));

    }

}
