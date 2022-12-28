package com.example.excelreader.init;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class Start {

  public Map<String, Object> cache;

  @PostConstruct
  private void init() {
    cache = new LinkedHashMap<>();
  }

}
