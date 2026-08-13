package com.fairchain.notificationservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * NOT in Section 4.1 — added at Archana's request so Frontend can read
 * and update notification_log without touching the DB directly (Section
 * 2.1: Frontend never queries Postgres directly). Flagged: needs adding
 * to Section 4.1 officially, same as the farmers endpoint earlier.
 */
@RestController
@RequestMapping("/api/v1/notifications")
public class NotificationController {

    private final NotificationQueryService queryService;

    public NotificationController(NotificationQueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> listNotifications(
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(queryService.listNotifications(status));
    }

    @PostMapping("/{id}/mark-sent")
    public ResponseEntity<NotificationResponse> markSent(@PathVariable String id) {
        return ResponseEntity.ok(queryService.markSent(id));
    }
}
