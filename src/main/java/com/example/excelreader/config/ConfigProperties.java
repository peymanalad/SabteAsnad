package com.example.excelreader.config;

import com.example.excelreader.init.Start;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Getter
@Setter
@ConfigurationProperties(prefix = "ftp")
public class ConfigProperties {

  private String username;
  private String password;
  private String host;
  private int port;
  private final Start start;

  public ConfigProperties(Start start) {
    this.start = start;
  }

  @Bean
  public void dev() {
    ftpConnection();
  }

  private void ftpConnection() {
    start.cache.put("isc.username", username);
    start.cache.put("isc.password", password);
    start.cache.put("isc.ip", host);
    start.cache.put("isc.port", port);
  }

}
