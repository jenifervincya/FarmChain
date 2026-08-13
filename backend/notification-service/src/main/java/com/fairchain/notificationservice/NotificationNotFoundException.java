package com.fairchain.notificationservice;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String id) {
        super("No notification found with id: " + id);
    }
}
