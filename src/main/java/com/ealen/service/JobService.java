package com.ealen.service;

import com.ealen.Entity.JobEntity;
import org.quartz.*;
/**
 * Created by Ma on 2019/12/1 21：22
 */
public interface JobService {
    public JobDataMap getJobDataMap(JobEntity job);
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map);
    public Trigger getTrigger(JobEntity job);
    public JobKey getJobKey(JobEntity job);
    public void restartAllJobs() throws SchedulerException;
    public void runJob(Integer jobId);
    public void shutDownJob(Integer jobId);
    public void runAllJobs();
    public void shutDownAllJobs( );
}
