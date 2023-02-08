package com.sepehrnet.surplusfund.enums;

public enum CalendarEnum {

  LOCALE_FA("fa_IR@calendar=persian"),
  LOCALE_EN("en_US@calendar=persian");

  private String value;

  CalendarEnum(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static CalendarEnum parse(String value) {
    CalendarEnum[] arr$ = values();
    for (CalendarEnum val : arr$) {
      if (val.getValue().equalsIgnoreCase(value)) {
        return val;
      }
    }
    return CalendarEnum.LOCALE_EN;
  }
}
