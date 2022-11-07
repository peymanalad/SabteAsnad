package com.example.excelreader.sabteAsnad.paya.dao;

import com.example.excelreader.sabteAsnad.paya.entity.RejectedTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RejectedTransactionDao extends JpaRepository<RejectedTransaction,String> {
}
