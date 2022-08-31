package com.vikash.expense.controller;

import com.vikash.expense.entity.Task;
import com.vikash.expense.service.TaskBean;
import com.vikash.expense.service.TaskSchedulingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class JobSchedulingController {

    @Autowired
    private TaskSchedulingService taskSchedulingService;

    @Autowired
    private TaskBean taskBean;

    @PostMapping(path="/schedule", consumes = "application/json", produces="application/json")
    public void scheduleATask(@RequestBody Task task) {
        taskBean.setTaskDefinition(task);
        taskSchedulingService.scheduleATask(UUID.randomUUID().toString(), taskBean, task.getCronExpression());
    }

    @GetMapping(path="/schedule/remove/{jobid}")
    public void removeJob(@PathVariable String jobid) {
        taskSchedulingService.removeScheduledTask(jobid);
    }
}