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
public class OutgoingTransaction {

    @Id
    private String transactionId;
    private String trackingNumber;
    private Long amount;
    private String senderFullName;
    private String senderId;
    private String debitPartyAccountNumber;
    private String receivingBank;
    private String receiverFullName;
    private String CreditPartyAccountNumber;
    private String depositId;
    private String transactionDescription;
    private String BankBranchCode;
    private String paymentSenderBankBranchCode;
    private String transactionClientId;
    private String stamp;
    private Integer circle;

}
