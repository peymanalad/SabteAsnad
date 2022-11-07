package com.example.excelreader.sabteAsnad.ghasedak.b2b.varede.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class B2BVaredeEntity {


    @Id
    private String transactionId;
    private Long amount;
    private String creditPartyName;
    private String depositPartyName;
}
