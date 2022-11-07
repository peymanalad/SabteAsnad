package com.example.excelreader.sabteAsnad.shetab.controller;


import com.example.excelreader.sabteAsnad.shetab.service.ShetabService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping("/shetab")
public record ShetabController (ShetabService service){

    @PostMapping
    public void shetabBeyneBanki() throws SQLException, IOException {
        service.createShetabExcel();
    }


}
