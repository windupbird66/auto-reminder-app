package com.reminder.reminder.controller;

import com.reminder.reminder.entity.Reminder;
import com.reminder.reminder.service.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reminders")
@CrossOrigin(origins = "*")
public class ReminderController {
    
    @Autowired
    private ReminderService reminderService;
    
    @PostMapping
    public ResponseEntity<?> createReminder(@RequestBody Reminder reminder) {
        try {
            Reminder createdReminder = reminderService.createReminder(reminder);
            return ResponseEntity.ok(createdReminder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reminder>> getUserReminders(@PathVariable Long userId) {
        List<Reminder> reminders = reminderService.getUserReminders(userId);
        return ResponseEntity.ok(reminders);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getReminderById(@PathVariable String id) {
        Optional<Reminder> reminder = reminderService.getReminderById(id);
        if (reminder.isPresent()) {
            return ResponseEntity.ok(reminder.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReminder(@PathVariable String id, @RequestBody Reminder reminderDetails) {
        try {
            Reminder updatedReminder = reminderService.updateReminder(id, reminderDetails);
            return ResponseEntity.ok(updatedReminder);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReminder(@PathVariable String id) {
        try {
            reminderService.deleteReminder(id);
            return ResponseEntity.ok(Map.of("message", "Reminder deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/pending")
    public ResponseEntity<List<Reminder>> getPendingReminders() {
        List<Reminder> reminders = reminderService.getRemindersToSend();
        return ResponseEntity.ok(reminders);
    }
    
    @PostMapping("/{id}/sent")
    public ResponseEntity<?> markAsSent(@PathVariable String id) {
        try {
            reminderService.markReminderAsSent(id);
            return ResponseEntity.ok(Map.of("message", "Reminder marked as sent"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}