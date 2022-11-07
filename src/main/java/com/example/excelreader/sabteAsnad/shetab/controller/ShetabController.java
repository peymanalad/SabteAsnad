package com.example.excelreader.sabteAsnad.shetab.controller;


import com.example.excelreader.sabteAsnad.shetab.service.ShetabService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Shetab")
public record ShetabController (ShetabService service){




}
