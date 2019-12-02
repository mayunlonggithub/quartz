package com.ealen.service;

import com.ealen.Entity.JobEntity;
import com.ealen.dao.JobDao;
import com.ealen.job.DynamicJob;
import com.ealen.util.Constant;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
/**
 * Created by Ma on 2019/12/1 21：22
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobDao jobDao;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;
    private static Logger logger = LoggerFactory.getLogger(JobService.class);

    //获取JobDataMap.(Job参数对象)
    @Override
    public JobDataMap getJobDataMap(JobEntity job) {
        JobDataMap map = new JobDataMap();
        map.put("id",job.getId());
        map.put("name", job.getName());
        map.put("jobGroup", job.getJobGroup());
        map.put("cron", job.getCron());
        map.put("parameter", job.getParameter());
        map.put("desc", job.getDesc());
        map.put("status", job.getStatus());
        return map;
    }

    //获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
    @Override
    public JobDetail getJobDetail(JobKey jobKey, String description, JobDataMap map) {
        return JobBuilder.newJob(DynamicJob.class)
                .withIdentity(jobKey)
                .withDescription(description)
                .setJobData(map)
                .storeDurably()
                .build();
    }

    //获取Trigger (Job的触发器,执行规则)
    @Override
    public Trigger getTrigger(JobEntity job) {
        return TriggerBuilder.newTrigger()
                .withIdentity(job.getName(), job.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                .startAt(job.getStartTime())
                .endAt(job.getEndTime())
                .build();
    }

    //获取JobKey,包含Name和Group
    @Override
    public JobKey getJobKey(JobEntity job) {
        return JobKey.jobKey(job.getName(), job.getJobGroup());
    }

    @Override
    public void restartAllJobs() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());
        for (JobKey jobKey : set) {
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobKey.getName(), jobKey.getGroup()));
            scheduler.deleteJob(jobKey);
        }
        List<JobEntity> list = jobDao.findByStatus(Constant.IMPLEMENT);
        for (JobEntity job : list) {
            logger.info("Job register name : {} , cron : {}", job.getName(),job.getJobGroup());
            JobDataMap map = getJobDataMap(job);
            JobKey jobKey = getJobKey(job);
            JobDetail jobDetail = getJobDetail(jobKey, job.getDesc(), map);
            scheduler.scheduleJob(jobDetail, getTrigger(job));
        }
    }

    @Override
    public void runJob(Integer jobId) {
        JobEntity job=jobDao.getOne(jobId);
        job.setStatus(Constant.IMPLEMENT);
        jobDao.save(job);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobDataMap map = getJobDataMap(job);
        JobKey jobKey =  getJobKey(job);

        JobDetail jobDetail = getJobDetail(jobKey,job.getDesc(), map);
        try {
            scheduler.scheduleJob(jobDetail, getTrigger(job));
        } catch (SchedulerException e) {
            logger.error(e.toString());
        }

    }
    @Override
    public void shutDownJob(Integer jobId) {
        JobEntity job =jobDao.getOne(jobId);
        job.setStatus(Constant.PAUSE);
        jobDao.save(job);
        TriggerKey triggerKey = new TriggerKey(job.getJobGroup() + job.getName());
        JobKey jobKey = getJobKey(job);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
        }catch (SchedulerException e) {
            logger.error(e.toString());
        }
    }

    @Override
    public void runAllJobs() {
        List<JobEntity> list = jobDao.findByStatus(Constant.PAUSE);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        for (JobEntity job : list) {
            JobDataMap map = getJobDataMap(job);
            JobKey jobKey = getJobKey(job);
            JobDetail jobDetail = getJobDetail(jobKey, job.getDesc(), map);
            job.setStatus(Constant.IMPLEMENT);
            jobDao.save(job);
            try {
                scheduler.scheduleJob(jobDetail, getTrigger(job));
            }catch (SchedulerException e) {
                logger.error(e.toString());
            }
            logger.info("Job register name : {} , cron : {}", job.getName(),job.getJobGroup());
        }
    }

    @Override
    public void shutDownAllJobs( ){
        List<JobEntity> list = jobDao.findByStatus(Constant.IMPLEMENT);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        for (JobEntity job : list) {
            TriggerKey triggerKey = new TriggerKey(job.getJobGroup() + job.getName());
            JobKey jobKey = getJobKey(job);
            job.setStatus(Constant.PAUSE);
            jobDao.save(job);
            try {
                scheduler.unscheduleJob(triggerKey);
                scheduler.deleteJob(jobKey);
            }catch (SchedulerException e) {
                logger.error(e.toString());
            }
            logger.info("Job Pause name : {} , cron : {}", job.getName(),job.getJobGroup());
        }
    }
}
