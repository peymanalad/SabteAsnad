package com.example.excelreader.sabteAsnad.aria.service;

import com.example.excelreader.sabteAsnad.Helper;
import com.example.excelreader.sabteAsnad.aria.dao.AriaDao;
import com.example.excelreader.sabteAsnad.aria.entity.Aria;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class AriaService {

    private final AriaDao ariaDao;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
    private final DateTimeFormatter dateOnly = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    public AriaService(AriaDao ariaDao) {
        this.ariaDao = ariaDao;
    }

    public ResponseEntity<?> getExcelDataAsList(MultipartFile multipartFile) throws ParseException {

        List<String> list = Helper.getAsList(multipartFile);

        List<Aria> ariaList = new ArrayList<>();
        for (int i = 0; i < list.size(); i = i + 12) {
            Aria aria = new Aria();
            aria.setTrn(list.get(i));
            aria.setValueDate(LocalDate.parse(list.get(i + 1),dateOnly));
            aria.setAmount(Long.parseLong(list.get(i + 2)));
            aria.setCurrency(list.get(i + 3));
            aria.setOriginalMessageType(list.get(i + 4));
            aria.setPriority(Integer.valueOf(list.get(i + 5)));
            aria.setDebitParty(list.get(i + 6));
            aria.setCreditParty(list.get(i + 7));
            aria.setMessageStatus(list.get(i + 8));
            aria.setSubmitter(list.get(i + 9));
            aria.setReceiptTime(formatter.parse(list.get(i + 10)));
            aria.setStatusChangeTime(formatter.parse(list.get(i + 11)));
            ariaDao.save(aria);
            ariaList.add(aria);
        }
        return ResponseEntity.ok().body(ariaList);
    }

}
