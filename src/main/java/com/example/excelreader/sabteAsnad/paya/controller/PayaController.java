package com.example.excelreader.sabteAsnad.paya.controller;

import com.example.excelreader.sabteAsnad.paya.service.PayaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/paya")
public record PayaController (PayaService payaService) {

    @PostMapping("/shaparak")
    public void shaparak(@RequestParam("shaparak") MultipartFile multipartFile) throws IOException {
        payaService.amountFromShaparak(multipartFile);
    }

    @PostMapping
    public void paya() throws SQLException, IOException {
        payaService.createPayaExcel();
    }




}
