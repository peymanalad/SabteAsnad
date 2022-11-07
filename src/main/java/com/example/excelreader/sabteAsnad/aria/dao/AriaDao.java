package com.example.excelreader.sabteAsnad.aria.dao;

import com.example.excelreader.sabteAsnad.aria.entity.Aria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AriaDao extends JpaRepository<Aria, Long> {
}
