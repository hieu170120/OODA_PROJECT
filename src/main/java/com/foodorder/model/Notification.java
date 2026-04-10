package com.foodorder.model;

import com.foodorder.model.enums.NotificationType;

import java.time.LocalDateTime;

public class Notification {
    private String notificationId;
    private Order order;
    private NotificationType notificationType;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;

    public Notification() {
    }

    public Notification(String notificationId, Order order, NotificationType notificationType, String message, LocalDateTime createdAt, boolean read) {
        this.notificationId = notificationId;
        this.order = order;
        this.notificationType = notificationType;
        this.message = message;
        this.createdAt = createdAt;
        this.read = read;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}
