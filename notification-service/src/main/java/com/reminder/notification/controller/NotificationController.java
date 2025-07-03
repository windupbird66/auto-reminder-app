package com.reminder.notification.controller;

import com.reminder.notification.entity.NotificationLog;
import com.reminder.notification.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    
    @Autowired
    private EmailService emailService;
    
    @PostMapping("/send-reminder")
    public ResponseEntity<?> sendReminderNotification(@RequestBody Map<String, Object> request) {
        try {
            String reminderId = (String) request.get("reminderId");
            Long userId = Long.valueOf(request.get("userId").toString());
            String userEmail = (String) request.get("userEmail");
            String title = (String) request.get("title");
            String description = (String) request.get("description");
            
            LocalDateTime reminderTime;
            if (request.get("reminderTime") != null) {
                reminderTime = LocalDateTime.parse(
                    request.get("reminderTime").toString(),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME
                );
            } else {
                reminderTime = LocalDateTime.now();
            }
            
            String logId = emailService.sendReminderEmail(
                reminderId, userId, userEmail, title, description, reminderTime
            );
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "logId", logId,
                "message", "Notification sent successfully"
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/log/{logId}")
    public ResponseEntity<?> getNotificationLog(@PathVariable String logId) {
        try {
            NotificationLog log = emailService.getNotificationLog(logId);
            if (log != null) {
                return ResponseEntity.ok(log);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/retry/{logId}")
    public ResponseEntity<?> retryNotification(@PathVariable String logId) {
        try {
            emailService.retryFailedNotification(logId);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Retry initiated"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage()
            ));
        }
    }
    
    @GetMapping("/health")
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "timestamp", LocalDateTime.now(),
            "service", "notification-service"
        ));
    }
}