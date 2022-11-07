package com.example.excelreader.sabteAsnad.ghasedak.c2c.varede.dao;

import com.example.excelreader.sabteAsnad.ghasedak.c2c.varede.entity.C2CVaredeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface C2CVaredeRepository extends JpaRepository<C2CVaredeEntity,String> {
}
