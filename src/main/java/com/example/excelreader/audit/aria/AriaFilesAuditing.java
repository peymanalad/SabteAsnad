package com.example.excelreader.audit.aria;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "TBL_ARIA_AUDIT")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class AriaFilesAuditing {

  @Id
  private Long id;
  @Column
  private String lastInputFileDate;
  @Column
  private long lastOutputFileCounter;

}
