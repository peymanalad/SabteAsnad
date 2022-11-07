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
public class RejectedTransaction {

    @Id
    private String transactionId;
    private String trackingNumber;
    private Long amount;
    private String senderFullName;
    private String senderId;
    private String senderAccountNumber;
    private String receivingBank;
    private String receiverFullName;
    private String receiverAccountNumber;
    private String depositId;
    private String transactionDescription;
    private String returnReason;
    private String bankBranchCode;
}
