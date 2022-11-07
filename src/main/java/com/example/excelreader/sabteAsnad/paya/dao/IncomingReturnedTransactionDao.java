package com.example.excelreader.sabteAsnad.paya.dao;

import com.example.excelreader.sabteAsnad.paya.entity.IncomingReturnedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingReturnedTransactionDao extends JpaRepository<IncomingReturnedTransaction,String> {
}
