package com.example.excelreader.sabteAsnad.aria.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Date;

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
