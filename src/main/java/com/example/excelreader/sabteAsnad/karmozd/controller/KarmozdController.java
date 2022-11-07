package com.example.excelreader.sabteAsnad.karmozd.controller;

import com.example.excelreader.sabteAsnad.karmozd.paya.KarmozdPayaService;
import com.example.excelreader.sabteAsnad.karmozd.satna.KarmozdSatnaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("karmozd")
public record KarmozdController(KarmozdPayaService karmozdPayaService,
                                KarmozdSatnaService karmozdSatnaService) {

    @PostMapping("/paya")
    public void karmozdPaya() throws SQLException, IOException {
        karmozdPayaService().createKarmozdPayaExcel();
    }

    @PostMapping("/satna")
    public void karmozdSatna() throws SQLException, IOException {
        karmozdSatnaService.createKarmozdPayaExcel();
    }


}
