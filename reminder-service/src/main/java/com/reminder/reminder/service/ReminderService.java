package com.reminder.reminder.service;

import com.reminder.reminder.entity.Reminder;
import com.reminder.reminder.repository.ReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReminderService {
    
    @Autowired
    private ReminderRepository reminderRepository;
    
    public Reminder createReminder(Reminder reminder) {
        reminder.setCreatedAt(LocalDateTime.now());
        reminder.setUpdatedAt(LocalDateTime.now());
        return reminderRepository.save(reminder);
    }
    
    public List<Reminder> getUserReminders(Long userId) {
        return reminderRepository.findByUserIdAndIsActiveTrue(userId);
    }
    
    public Optional<Reminder> getReminderById(String id) {
        return reminderRepository.findById(id);
    }
    
    public Reminder updateReminder(String id, Reminder reminderDetails) {
        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        if (optionalReminder.isPresent()) {
            Reminder reminder = optionalReminder.get();
            reminder.setTitle(reminderDetails.getTitle());
            reminder.setDescription(reminderDetails.getDescription());
            reminder.setReminderTime(reminderDetails.getReminderTime());
            reminder.setFrequency(reminderDetails.getFrequency());
            reminder.setDaysOfWeek(reminderDetails.getDaysOfWeek());
            reminder.setDayOfMonth(reminderDetails.getDayOfMonth());
            reminder.setIsActive(reminderDetails.getIsActive());
            reminder.setUpdatedAt(LocalDateTime.now());
            return reminderRepository.save(reminder);
        }
        throw new RuntimeException("Reminder not found with id: " + id);
    }
    
    public void deleteReminder(String id) {
        Optional<Reminder> optionalReminder = reminderRepository.findById(id);
        if (optionalReminder.isPresent()) {
            Reminder reminder = optionalReminder.get();
            reminder.setIsActive(false);
            reminder.setUpdatedAt(LocalDateTime.now());
            reminderRepository.save(reminder);
        } else {
            throw new RuntimeException("Reminder not found with id: " + id);
        }
    }
    
    public List<Reminder> getRemindersToSend() {
        LocalDateTime now = LocalDateTime.now();
        return reminderRepository.findActiveRemindersBeforeTime(now);
    }
    
    public void markReminderAsSent(String reminderId) {
        Optional<Reminder> optionalReminder = reminderRepository.findById(reminderId);
        if (optionalReminder.isPresent()) {
            Reminder reminder = optionalReminder.get();
            reminder.setLastSentAt(LocalDateTime.now());
            reminderRepository.save(reminder);
        }
    }
}