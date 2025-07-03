package com.reminder.notification.service;

import com.reminder.notification.entity.NotificationLog;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private Configuration freemarkerConfig;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public String sendReminderEmail(String reminderId, Long userId, String userEmail, 
                                  String title, String description, LocalDateTime reminderTime) {
        NotificationLog log = new NotificationLog();
        log.setId(UUID.randomUUID().toString());
        log.setReminderId(reminderId);
        log.setUserId(userId);
        log.setUserEmail(userEmail);
        log.setSubject("提醒: " + title);
        
        try {
            String content = generateEmailContent(title, description, reminderTime);
            log.setContent(content);
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(userEmail);
            helper.setSubject(log.getSubject());
            helper.setText(content, true);
            
            mailSender.send(message);
            
            log.setStatus("SENT");
            log.setSentAt(LocalDateTime.now());
            
        } catch (Exception e) {
            log.setStatus("FAILED");
            log.setFailureReason(e.getMessage());
            log.setRetryCount(log.getRetryCount() + 1);
        }
        
        // 保存到Redis
        redisTemplate.opsForHash().put("notification_logs", log.getId(), log);
        
        return log.getId();
    }
    
    private String generateEmailContent(String title, String description, LocalDateTime reminderTime) {
        try {
            Template template = freemarkerConfig.getTemplate("reminder-email.ftl");
            
            Map<String, Object> model = new HashMap<>();
            model.put("title", title);
            model.put("description", description);
            model.put("reminderTime", reminderTime);
            model.put("year", LocalDateTime.now().getYear());
            
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            
            return writer.toString();
        } catch (Exception e) {
            // 如果模板处理失败，返回简单的HTML内容
            return String.format(
                "<html><body>" +
                "<h2>📅 提醒通知</h2>" +
                "<h3>%s</h3>" +
                "<p>%s</p>" +
                "<p><strong>提醒时间:</strong> %s</p>" +
                "<hr>" +
                "<p><small>自动提醒系统发送</small></p>" +
                "</body></html>", 
                title, description, reminderTime
            );
        }
    }
    
    public NotificationLog getNotificationLog(String logId) {
        return (NotificationLog) redisTemplate.opsForHash().get("notification_logs", logId);
    }
    
    public void retryFailedNotification(String logId) {
        NotificationLog log = getNotificationLog(logId);
        if (log != null && "FAILED".equals(log.getStatus()) && log.getRetryCount() < 3) {
            sendReminderEmail(log.getReminderId(), log.getUserId(), log.getUserEmail(), 
                           extractTitleFromSubject(log.getSubject()), "", LocalDateTime.now());
        }
    }
    
    private String extractTitleFromSubject(String subject) {
        return subject.startsWith("提醒: ") ? subject.substring(3) : subject;
    }
}