package com.example.excelreader.audit.aria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AriaAuditRepository extends JpaRepository<AriaFilesAuditing,Long> {

}
