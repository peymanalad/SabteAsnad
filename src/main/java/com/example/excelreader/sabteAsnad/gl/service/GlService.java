package com.example.excelreader.sabteAsnad.gl.service;

import com.example.excelreader.sabteAsnad.gl.dao.GlRepository;
import java.text.SimpleDateFormat;
import org.springframework.stereotype.Service;

@Service
public class GlService {

  private final GlRepository glRepository;
  private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");


  public GlService(GlRepository glRepository) {
    this.glRepository = glRepository;
  }
}
