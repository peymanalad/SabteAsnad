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
public class IncomingReturnedTransaction {

    @Id
    private String returnedId;
    private String primitiveTrackingNumber;
    private String primitiveTransactionNumber;
    private Long primitiveAmount;
    @Column(name = "amount")
    private Long returnedAmount;
    private String returnReason;
    private String referenceAccount;
    private String remittingBankBranchCode;


}
