package com.example.excelreader.utility;

public enum LanguageEnum {

  ENGLISH("en"),
  PERSIAN("fa");

  private String value;

  LanguageEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static LanguageEnum parse(String value) {
    LanguageEnum[] arr$ = values();
    for (LanguageEnum val : arr$) {
      if (val.getValue().equalsIgnoreCase(value)) {
        return val;
      }
    }
    return LanguageEnum.ENGLISH;
  }

}
