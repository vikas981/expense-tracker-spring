package com.vikash.expense.service;

import com.vikash.expense.entity.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
@Slf4j
public class TaskBean implements Runnable {

    private Task task;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void run() {
        log.info("Running action: " + task.getActionType());
        log.info("With Data: " + task.getData());
        try {
            sendMessageWithAttachment();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Task getTaskDefinition() {
        return task;
    }

    public void setTaskDefinition(Task task) {
        this.task = task;
    }

    public void sendMessageWithAttachment() throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("noreply@vikash.com");
        helper.setTo("vs98990@gmail.com");
        helper.setSubject("Expense Report");
        helper.setText("Please find the attached expense reports");
        File file = new File("/home/vikash/Downloads/expense/reports/pdf");
        File [] files=file.listFiles();
        for (File f : files){
            FileSystemResource fileSystemResource
                    = new FileSystemResource(new File(f.getAbsolutePath()));
            helper.addAttachment(f.getName(), fileSystemResource);
        }
        javaMailSender.send(message);

    }


}
