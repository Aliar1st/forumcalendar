package ru.forumcalendar.forumcalendar.Quartz;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class NotificationExecutor {

    public NotificationExecutor(int eventId) {
        this.eventId = eventId;
    }

    private int eventId;

    private Scheduler scheduler;

    public void executeAt(NotificationJob.Job jobToDone, LocalDateTime runTime) throws SchedulerException {

        this.scheduler = new StdSchedulerFactory().getScheduler();

        String uniqueIdentifier = UUID.randomUUID().toString();

        JobDetail job = newJob(NotificationJob.class)
                .withIdentity("job-" + uniqueIdentifier, "main")
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("trigger-" + uniqueIdentifier, "main")
                .startAt(convertLocalDate(runTime))
                .build();

        job.getJobDataMap().put("job", jobToDone);

        scheduler.scheduleJob(job, trigger);
        scheduler.start();
    }

    public void clear() throws SchedulerException {
        if (scheduler != null) {
            scheduler.clear();
        }
        scheduler = null;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    private Date convertLocalDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
