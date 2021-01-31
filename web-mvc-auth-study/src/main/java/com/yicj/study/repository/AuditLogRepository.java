package com.yicj.study.repository;

import com.yicj.study.model.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditLogRepository extends JpaSpecificationExecutor<AuditLog>, CrudRepository<AuditLog, Integer> {
}
