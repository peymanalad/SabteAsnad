package com.example.excelreader.sabteAsnad.aria.service;

import com.example.excelreader.audit.aria.AriaAuditService;
import com.example.excelreader.audit.aria.AriaFilesAuditing;
import com.example.excelreader.cache.Start;
import com.example.excelreader.sabteAsnad.Helper;
import com.example.excelreader.sabteAsnad.aria.dao.AriaDao;
import com.example.excelreader.sabteAsnad.aria.entity.Aria;
import com.example.excelreader.utility.CalendarUtils;
import com.example.excelreader.utility.FTPUtils;
import com.example.excelreader.utility.GlobalUtils;
import com.example.excelreader.utility.ZipUtils;
import com.ibm.icu.util.Calendar;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AriaService {

    @Value("${sabt.file.path}")
    private String filePath;
    @Value("${sabt.isc.file.name}")
    private String iscZipFileName;
    @Value("${sabt.isc.file.postfix}")
    private String iscFilePostfix;
    @Value("${sabt.ftp.isc.host.input.path}")
    private String iscHostPath;
    @Value("${sabt.isc.content.file.name}")
    private String iscContentFileName;
    @Value("${sabt.isc.content.file.postfix}")
    private String iscContentFilePostfix;

    private final AriaDao ariaDao;
    private final Start start;
    private final AriaAuditService ariaAuditing;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

    public AriaService(AriaDao ariaDao, Start start,
        AriaAuditService ariaAuditing) {
        this.ariaDao = ariaDao;
        this.start = start;
        this.ariaAuditing = ariaAuditing;
    }

    @Scheduled(cron = "${sabt.read.file.cron.job}")
    public void process() {
        log.info("Aria Service is running... ");

        String fileSeparator = File.separator;
        Optional<AriaFilesAuditing> config = ariaAuditing.getData();

        if (true) {
            String lastInputFileDate = config.get().getLastInputFileDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, -1);
            String currentPersianDate = CalendarUtils.getCurrentPersianDateTime(
                GlobalUtils.CALENDAR_DATE_FLAT_PATTERN, calendar);
            if (Long.parseLong(lastInputFileDate) < Long.parseLong(currentPersianDate)) {
                log.info("Start calculate dates between two date.");
                Date startDate = CalendarUtils.convertPersianStringToDate(lastInputFileDate);
                Date endDate = CalendarUtils.convertPersianStringToDate(currentPersianDate);
                List<Date> dateList = GlobalUtils.datesBetweenTwoDate(startDate, endDate);

                dateList.forEach(date -> {
                    boolean doneISCFTP = false;

                    Calendar fileCalendar = Calendar.getInstance();
                    fileCalendar.setTime(date);
                    String currentDate = CalendarUtils.getCurrentPersianDateTime(
                        GlobalUtils.CALENDAR_DATE_FLAT_PATTERN, fileCalendar);
                    String currentDirectory = filePath + currentDate + fileSeparator;
                    GlobalUtils.createDirectory(currentDirectory);

                    String zipFileName = iscZipFileName + currentDate + iscFilePostfix;
                    String zipContentFileName =
                        iscContentFileName + currentDate + iscContentFilePostfix;

                    log.info("Try connect to ISC FTP.");
                    FTPUtils iscFTP = new FTPUtils();
                    if (iscFTP.connect((String) start.cache.get("isc.ip"),
                        (Integer) start.cache.get("isc.port"))) {
                        if (iscFTP.login((String) start.cache.get("isc.username"),
                            (String) start.cache.get("isc.password"))) {
                            if (iscFTP.downloadFile(iscHostPath + zipFileName, currentDirectory + zipFileName)) {
                                doneISCFTP = true;
                            }
                        }
                        iscFTP.disconnect();
                    }

                    if (doneISCFTP) {
                        log.info("Try to extract file from ISC zip file.");
                        Path source = Paths.get(currentDirectory + zipFileName);
                        Path target = Paths.get(currentDirectory);
                        ZipUtils.extractFile(source, target, zipContentFileName);
                        try {
                            getExcelDataAsList(Path.of(target + File.separator + ""));
                        } catch (ParseException | FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                    String iscTxtFileFullPath = currentDirectory + zipContentFileName;
                    try {
                        getExcelDataAsList(Paths.get(iscTxtFileFullPath));
                    } catch (ParseException | FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (Files.exists(Paths.get(iscTxtFileFullPath))) {
                        config.get().setLastInputFileDate(currentDate);
                        ariaAuditing.saveOrUpdate(config.get());
                    }
                });
            }
        }
        else {
            log.warn("AriaAuditing not found.");
        }
    }

    public void getExcelDataAsList(Path directory) throws ParseException, FileNotFoundException {
        List<String> list = Helper.getAsList(directory,3);

        for (int i = 0; i < list.size(); i = i + 12) {
            Aria aria = new Aria();
            aria.setTrn(list.get(i));
            aria.setValueDate(list.get(i + 1));
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
        }
    }

}
