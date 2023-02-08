package com.example.excelreader.utility;

import java.nio.file.Path;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

@Slf4j
public class ZipUtils {

  public static void extractFile(Path source, Path target, String fileName) {
    try {
      new ZipFile(source.toFile()).extractFile(fileName, target.toString());
    } catch (ZipException e) {
      log.info("Extract file exception occurred: {}", e.getMessage());
      e.printStackTrace();
    }
  }

}
