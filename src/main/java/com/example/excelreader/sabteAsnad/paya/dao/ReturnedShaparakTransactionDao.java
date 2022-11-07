package com.example.excelreader.sabteAsnad.paya.dao;

import com.example.excelreader.sabteAsnad.paya.entity.ReturnedShaparakTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnedShaparakTransactionDao extends JpaRepository<ReturnedShaparakTransaction,String> {
}
