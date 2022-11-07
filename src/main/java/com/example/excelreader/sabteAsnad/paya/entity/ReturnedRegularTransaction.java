package com.example.excelreader.sabteAsnad.paya.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ReturnedRegularTransaction {

    @Id
    private String returnedId;
    private String senderFullName;
    private String senderId;
    private String senderAccountNumber;
    private String remittingBank;
    private String receivingBank;
    private String receiverFullName;
    private String receiverBankAccount;
    private String primitiveTrackingNumber;
    private String primitiveTransactionId;
    private Long primitiveAmount;
    @Column(name = "amount")
    private Long returnedAmount;
    private String bankBranchCode;
    private Integer circle;

}
