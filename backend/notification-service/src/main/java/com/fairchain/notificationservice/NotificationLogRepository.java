package com.fairchain.notificationservice;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {
    List<NotificationLog> findByStatusOrderByCreatedAtDesc(String status);
    List<NotificationLog> findAllByOrderByCreatedAtDesc();
}
