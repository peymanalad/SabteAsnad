package com.example.excelreader.sabteAsnad.ghasedak.model.controller;

import com.example.excelreader.sabteAsnad.ghasedak.model.service.B2BService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ghasedak")
public record GhasedakController (B2BService b2BService) {

    @PostMapping("/b2b/sadere")
    public void saveB2BSadere(@RequestParam("Sadere") MultipartFile multipartFile) {
        b2BService.saveB2BSadere(multipartFile);
    }

    @PostMapping("/b2b/varede")
    public void saveB2BVarede(@RequestParam("Varede") MultipartFile multipartFile) {
        b2BService.saveB2BVarede(multipartFile);
    }


}
