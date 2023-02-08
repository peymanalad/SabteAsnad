package com.example.excelreader.sabteAsnad.aria.entity;

import java.util.Date;
import javax.persistence.Column;
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
public class Aria {

    @Id
    @Column(name = "transaction_id")
    private String trn;
    @DateTimeFormat(pattern = "yyyy/mm/dd")
    private String valueDate;
    private Long amount;
    private String currency;
    private String originalMessageType;
    private Integer priority;
    private String debitParty;
    private String creditParty;
    private String messageStatus;
    private String submitter;
    @DateTimeFormat(pattern = "yyyy/mm/dd hh:MM:ss")
    private Date receiptTime;
    @DateTimeFormat(pattern = "yyyy/mm/dd hh:MM:ss")
    private Date statusChangeTime;

}
