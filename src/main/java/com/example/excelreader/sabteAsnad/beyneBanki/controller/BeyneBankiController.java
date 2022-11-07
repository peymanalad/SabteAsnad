package com.example.excelreader.sabteAsnad.beyneBanki.controller;

import com.example.excelreader.sabteAsnad.beyneBanki.service.BeyneBankiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

@RestController
@RequestMapping("/beynebanki")
public record BeyneBankiController (BeyneBankiService beyneBankiService) {


    @PostMapping
    public void beyneBankiExcelGenerator() throws SQLException, IOException {
        beyneBankiService.createBeyneBankiExcel();
    }


}
