package com.reminder.reminder.repository;

import com.reminder.reminder.entity.Reminder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReminderRepository extends MongoRepository<Reminder, String> {
    List<Reminder> findByUserIdAndIsActiveTrue(Long userId);
    
    List<Reminder> findByUserIdOrderByReminderTimeAsc(Long userId);
    
    @Query("{ 'isActive': true, 'reminderTime': { $lte: ?0 } }")
    List<Reminder> findActiveRemindersBeforeTime(LocalDateTime time);
    
    @Query("{ 'isActive': true, 'frequency': 'DAILY', 'reminderTime': { $lte: ?0 } }")
    List<Reminder> findDailyRemindersBeforeTime(LocalDateTime time);
    
    @Query("{ 'isActive': true, 'frequency': 'WEEKLY', 'reminderTime': { $lte: ?0 } }")
    List<Reminder> findWeeklyRemindersBeforeTime(LocalDateTime time);
    
    @Query("{ 'isActive': true, 'frequency': 'MONTHLY', 'reminderTime': { $lte: ?0 } }")
    List<Reminder> findMonthlyRemindersBeforeTime(LocalDateTime time);
}