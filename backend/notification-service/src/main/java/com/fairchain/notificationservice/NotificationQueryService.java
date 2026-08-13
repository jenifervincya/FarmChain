package com.fairchain.notificationservice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationQueryService {

    private final NotificationLogRepository repository;

    public NotificationQueryService(NotificationLogRepository repository) {
        this.repository = repository;
    }

    public List<NotificationResponse> listNotifications(String status) {
        List<NotificationLog> logs = (status != null && !status.isBlank())
                ? repository.findByStatusOrderByCreatedAtDesc(status)
                : repository.findAllByOrderByCreatedAtDesc();

        return logs.stream().map(this::toResponse).toList();
    }

    public NotificationResponse markSent(String id) {
        NotificationLog log = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new NotificationNotFoundException(id));
        log.markSent();
        NotificationLog saved = repository.save(log);
        return toResponse(saved);
    }

    private NotificationResponse toResponse(NotificationLog log) {
        return new NotificationResponse(
                log.getId().toString(),
                log.getFarmerId(),
                log.getBatchId(),
                log.getEventType(),
                log.getMessage(),
                log.getStatus(),
                log.getCreatedAt()
        );
    }
}
