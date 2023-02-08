package com.example.excelreader.sabteAsnad.ghasedak.model;

import com.example.excelreader.sabteAsnad.Helper;
import com.example.excelreader.sabteAsnad.ghasedak.b2b.sadere.entity.B2BSadereEntity;
import com.example.excelreader.sabteAsnad.ghasedak.b2b.varede.entity.B2BVaredeEntity;
import com.example.excelreader.sabteAsnad.ghasedak.b2b.sadere.dao.B2BSadereRepository;
import com.example.excelreader.sabteAsnad.ghasedak.b2b.varede.dao.B2BVaredeRepository;
import com.example.excelreader.utility.FTPUtils;
import com.example.excelreader.utility.ZipUtils;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class B2BService {

    private String varedeDirectory;
    private String sadereDirectory;


    private B2BSadereRepository b2BSadereRepository;
    private B2BVaredeRepository b2BVaredeRepository;

    public B2BService(
        B2BSadereRepository b2BSadereRepository,
        B2BVaredeRepository b2BVaredeRepository) {
        this.b2BSadereRepository = b2BSadereRepository;
        this.b2BVaredeRepository = b2BVaredeRepository;
    }

    //download from ftp
    //extract it

    @Scheduled(cron = "${sabt.read.file.cron.job}")
    public void saveB2BSadere(Path path) throws FileNotFoundException {

        List<String> list = Helper.getAsList(path,9);

        for (int i = 0; i < list.size(); i = i + 5) {
            B2BSadereEntity sadere = new B2BSadereEntity();
            sadere.setTransactionId(list.get(i + 1));
            sadere.setAmount(Long.parseLong(list.get(i + 2)));
            sadere.setCreditParty(list.get(i + 3));
            sadere.setDepositParty(list.get(i + 4));
            b2BSadereRepository.save(sadere);
        }
    }

    @Scheduled(cron = "${sabt.read.file.cron.job}")
    public void saveB2BVarede(Path path) throws FileNotFoundException {

        List<String> list = Helper.getAsList(path,9);

        for (int i = 0; i < list.size(); i = i + 5) {
            B2BVaredeEntity varede = new B2BVaredeEntity();
            varede.setTransactionId(list.get(i + 1));
            varede.setAmount(Long.parseLong(list.get(i + 2)));
            varede.setCreditPartyName(list.get(i + 3));
            varede.setDepositPartyName(list.get(i + 4));
            b2BVaredeRepository.save(varede);
        }
    }



}
