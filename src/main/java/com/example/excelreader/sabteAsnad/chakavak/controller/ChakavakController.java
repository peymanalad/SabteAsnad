package com.example.excelreader.sabteAsnad.chakavak.controller;

import com.example.excelreader.sabteAsnad.chakavak.service.ChakavakService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/chakavak")
public record ChakavakController (ChakavakService chakavakService) {


    @PostMapping()
    public void chakavakExcelGenerator() throws SQLException, IOException {
        chakavakService.createChakavakExcel();
    }

}
