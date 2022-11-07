package com.example.excelreader.sabteAsnad.satna.controller;

import com.example.excelreader.sabteAsnad.satna.service.SatnaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/satna")
public record SantaController(SatnaService satnaService) {

    @PostMapping
    public void satna() throws SQLException, IOException {
        satnaService.createSatnaExcel();
    }

}
