package com.example.excelreader.sabteAsnad.gl.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class GlEntity {

  @Id
  private Long id;
  private Integer headingId;
  private Integer headingCode;
  private Integer organizationId;
  private Integer organizationCode;
  @DateTimeFormat(pattern = "yyyy/mm/dd")
  private String ledgerDate;
  private Long debitFlowAmount;
  private Long creditFlowAmount;
  private Long initialRemaining;
  private Long finalRemaining;
  private Long totalDebitAmount;
  private Long totalCreditAmount;
  private Integer glId;
  private Integer complementNumber;
  private String rowId;
}
