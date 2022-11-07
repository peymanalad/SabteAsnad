package com.example.excelreader.sabteAsnad.paya.dao;

import com.example.excelreader.sabteAsnad.paya.entity.OutgoingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OutgoingTransactionDao extends JpaRepository<OutgoingTransaction,String> {
}
