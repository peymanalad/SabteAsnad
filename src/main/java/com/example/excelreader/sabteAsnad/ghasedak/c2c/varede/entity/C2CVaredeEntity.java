package com.example.excelreader.sabteAsnad.ghasedak.c2c.varede.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class C2CVaredeEntity {

    @Id
    private String transactionId;
    private String row;
    private Long amount;
    private String senderFirstname;
    private String senderLastname;
    private String senderAccountNo;
    private String senderBank;
    private String senderBankBranchCode;
    private String receiverFirstname;
    private String receiverLastname;
    private String receiverAccountNo;
    private String receiverBank;
    private String depositId;
    private String trackingNo;


}
