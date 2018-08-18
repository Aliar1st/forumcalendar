package ru.forumcalendar.forumcalendar.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class NotificationJob implements Job {

    public interface Job {
        void execute();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        ((Job) jobExecutionContext.getMergedJobDataMap().get("job")).execute();
    }
}
