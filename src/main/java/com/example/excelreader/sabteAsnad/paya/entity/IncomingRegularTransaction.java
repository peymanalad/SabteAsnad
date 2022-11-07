package com.example.excelreader.sabteAsnad.paya.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class IncomingRegularTransaction {

    @Id
    private String transactionId;
    private String trackingNumber;
    private Long amount;
    private String senderFullName;
    private String senderId;
    private String debitPartyAccountNumber;
    private String remittingBank;
    private String receiverFullName;
    private String CreditPartyAccountNumber;
    private String depositId;
    private String transactionDescription;
    private String remittingBankBranchCode;
    private Integer circle;
}
