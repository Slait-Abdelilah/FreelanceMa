package org.example.backfreelance.seecuriity;

import org.example.backfreelance.entity.AuditLog;
import org.example.backfreelance.repository.AuditLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogger {

    private static final Logger AUDIT = LoggerFactory.getLogger("AUDIT");

    private final AuditLogRepository auditLogRepository;

    public AuditLogger(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // REQUIRES_NEW : l'audit est committé même si la transaction appelante est rollback
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String action, String email) {
        AUDIT.info("[AUDIT] action={} email={}", action, email);
        auditLogRepository.save(AuditLog.builder()
                .action(action)
                .email(email)
                .success(true)
                .build());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFailure(String action, String email, String reason) {
        AUDIT.warn("[AUDIT] FAILED action={} email={} reason={}", action, email, reason);
        auditLogRepository.save(AuditLog.builder()
                .action(action)
                .email(email)
                .reason(reason)
                .success(false)
                .build());
    }
}