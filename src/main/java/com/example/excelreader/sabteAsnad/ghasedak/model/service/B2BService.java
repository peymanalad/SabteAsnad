package com.example.excelreader.sabteAsnad.ghasedak.model.service;

import com.example.excelreader.sabteAsnad.Helper;
import com.example.excelreader.sabteAsnad.ghasedak.b2b.sadere.entity.B2BSadereEntity;
import com.example.excelreader.sabteAsnad.ghasedak.b2b.varede.entity.B2BVaredeEntity;
import com.example.excelreader.sabteAsnad.ghasedak.b2b.sadere.dao.B2BSadereRepository;
import com.example.excelreader.sabteAsnad.ghasedak.b2b.varede.dao.B2BVaredeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public record B2BService (B2BSadereRepository b2BSadereRepository,
                          B2BVaredeRepository b2BVaredeRepository){

    public ResponseEntity<?> saveB2BSadere(MultipartFile multipartFile) {

        List<String> list = Helper.getAsList(multipartFile,9);

        List<B2BSadereEntity> b2bList = new ArrayList<>();
        for (int i = 0; i < list.size(); i = i + 5) {
            B2BSadereEntity sadere = new B2BSadereEntity();
            sadere.setTransactionId(list.get(i + 1));
            sadere.setAmount(Long.parseLong(list.get(i + 2)));
            sadere.setCreditParty(list.get(i + 3));
            sadere.setDepositParty(list.get(i + 4));
            b2BSadereRepository.save(sadere);
            b2bList.add(sadere);
        }
        return ResponseEntity.ok().body(b2bList);
    }

    public ResponseEntity<?> saveB2BVarede(MultipartFile multipartFile) {

        List<String> list = Helper.getAsList(multipartFile,9);

        List<B2BVaredeEntity> b2bList = new ArrayList<>();
        for (int i = 0; i < list.size(); i = i + 5) {
            B2BVaredeEntity varede = new B2BVaredeEntity();
            varede.setTransactionId(list.get(i + 1));
            varede.setAmount(Long.parseLong(list.get(i + 2)));
            varede.setCreditPartyName(list.get(i + 3));
            varede.setDepositPartyName(list.get(i + 4));
            b2BVaredeRepository.save(varede);
            b2bList.add(varede);
        }
        return ResponseEntity.ok().body(b2bList);
    }



}
