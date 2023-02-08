package com.example.excelreader.audit;

import com.example.excelreader.utility.CalendarUtils;
import com.example.excelreader.utility.GlobalUtils;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Setter
@Getter
public class AbstractAuditingEntity implements Serializable {

  @Column(name = "created_at", updatable = false, nullable = false)
  private String createdDateTime;

  @Column(name = "modified_at")
  private String lastModifiedDateTime;

  @PrePersist
  public void prePersist() {
    this.createdDateTime = CalendarUtils.getCurrentPersianDateTime(GlobalUtils.CALENDAR_DATE_TIME_FLAT_PATTERN);
  }

  @PreUpdate
  public void preUpdate() {
    this.lastModifiedDateTime = CalendarUtils.getCurrentPersianDateTime(GlobalUtils.CALENDAR_DATE_TIME_FLAT_PATTERN);
  }

}
