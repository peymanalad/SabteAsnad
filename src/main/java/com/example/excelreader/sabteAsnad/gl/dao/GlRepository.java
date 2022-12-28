package com.example.excelreader.sabteAsnad.gl.dao;

import com.example.excelreader.sabteAsnad.gl.entity.GlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlRepository extends JpaRepository<GlEntity,Long> {

}
