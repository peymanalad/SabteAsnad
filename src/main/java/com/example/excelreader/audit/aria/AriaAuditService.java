package com.example.excelreader.audit.aria;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AriaAuditService {

  private AriaAuditRepository repository;

  public AriaAuditService(AriaAuditRepository repository) {
    this.repository = repository;
  }

  public AriaFilesAuditing saveOrUpdate(AriaFilesAuditing ariaFilesAuditing) {
    return repository.save(ariaFilesAuditing);
  }

  public Optional<AriaFilesAuditing> getData() {
    return repository.findById(1L);
  }



}
