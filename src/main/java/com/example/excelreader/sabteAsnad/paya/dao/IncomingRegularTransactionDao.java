package com.example.excelreader.sabteAsnad.paya.dao;

import com.example.excelreader.sabteAsnad.paya.entity.IncomingRegularTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncomingRegularTransactionDao extends JpaRepository<IncomingRegularTransaction,String> {
}
