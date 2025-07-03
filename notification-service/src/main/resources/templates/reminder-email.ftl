<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>提醒通知</title>
    <style>
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
            line-height: 1.6;
            color: #333;
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            background-color: white;
            border-radius: 10px;
            padding: 30px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.1);
        }
        .header {
            text-align: center;
            border-bottom: 3px solid #4CAF50;
            padding-bottom: 20px;
            margin-bottom: 30px;
        }
        .header h1 {
            color: #4CAF50;
            margin: 0;
            font-size: 28px;
        }
        .content {
            margin-bottom: 30px;
        }
        .reminder-title {
            color: #2196F3;
            font-size: 24px;
            margin-bottom: 15px;
            border-left: 4px solid #2196F3;
            padding-left: 15px;
        }
        .reminder-description {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            border-left: 4px solid #17a2b8;
        }
        .reminder-time {
            background-color: #fff3cd;
            padding: 15px;
            border-radius: 8px;
            border: 1px solid #ffeaa7;
            text-align: center;
            font-weight: bold;
            color: #856404;
        }
        .footer {
            text-align: center;
            border-top: 1px solid #eee;
            padding-top: 20px;
            color: #666;
            font-size: 14px;
        }
        .emoji {
            font-size: 24px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1><span class="emoji">📅</span> 提醒通知</h1>
        </div>
        
        <div class="content">
            <h2 class="reminder-title">${title}</h2>
            
            <#if description?? && description?length gt 0>
            <div class="reminder-description">
                <strong>详细说明：</strong><br>
                ${description}
            </div>
            </#if>
            
            <div class="reminder-time">
                <span class="emoji">⏰</span> 
                提醒时间: ${reminderTime?string("yyyy年MM月dd日 HH:mm")}
            </div>
        </div>
        
        <div class="footer">
            <p>这是来自<strong>自动提醒系统</strong>的通知</p>
            <p>© ${year} Auto Reminder App. 祝您工作顺利！</p>
        </div>
    </div>
</body>
</html>