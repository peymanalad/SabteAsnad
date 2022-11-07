package com.example.excelreader.sabteAsnad.paya.controller;

import com.example.excelreader.sabteAsnad.paya.service.PayaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
@RequestMapping("/paya")
public record PayaController (PayaService payaService) {

    @PostMapping("/1")
    public void saveIncomingRegTransaction (@RequestParam("paya") MultipartFile multipartFile) {
        payaService.saveIncomingRegTransaction(multipartFile);
    }

    @PostMapping("/2")
    public void saveIncomingRetTransaction (@RequestParam("paya") MultipartFile multipartFile) {
        payaService.saveIncomingRetTransaction(multipartFile);
    }

    @PostMapping("/6")
    public void saveOutTransaction (@RequestParam("paya") MultipartFile multipartFile) {
        payaService.saveOutTransaction(multipartFile);
    }

    @PostMapping("/8")
    public void saveRejectTransaction (@RequestParam("paya") MultipartFile multipartFile) {
        payaService.saveRejectTransaction(multipartFile);
    }

    @PostMapping("/11")
    public void saveRetRegTransaction (@RequestParam("paya") MultipartFile multipartFile) {
        payaService.saveRetRegTransaction(multipartFile);
    }

    @PostMapping("/12")
    public void saveRetShaparakTransaction (@RequestParam("paya") MultipartFile multipartFile) {
        payaService.saveRetShaparakTransaction(multipartFile);
    }

    public void paya() throws SQLException {
        payaService.createPayaExcel();
    }




}
