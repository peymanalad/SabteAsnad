package com.example.excelreader.sabteAsnad.aria.controller;

import com.example.excelreader.sabteAsnad.Helper;
import com.example.excelreader.sabteAsnad.aria.service.AriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping
public record AriaController(AriaService ariaService) {


    @PostMapping
    public ResponseEntity<?> uploadFileToDb(@RequestParam("Aria") MultipartFile multipartFile) throws ParseException {
        return Helper.checkFormat(multipartFile)
                ? ariaService.getExcelDataAsList(multipartFile)
                : ResponseEntity.badRequest().build();
    }


}
