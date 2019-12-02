package com.ealen.job;

import com.ealen.Entity.JobEntity;
import com.ealen.dao.JobDao;
import com.ealen.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * Created by EalenXie on 2018/6/4 14:29
 * :@DisallowConcurrentExecution : 此标记用在实现Job的类上面,意思是不允许并发执行.
 * :注意org.quartz.threadPool.threadCount线程池中线程的数量至少要多个,否则@DisallowConcurrentExecution不生效
 * :假如Job的设置时间间隔为3秒,但Job执行时间是5秒,设置@DisallowConcurrentExecution以后程序会等任务执行完毕以后再去执行,否则会在3秒时再启用新的线程执行
 */
@DisallowConcurrentExecution
@Component
@Slf4j
public class DynamicJob implements Job {

    @Autowired
    private JobDao jobDao;
    /**
     * 核心方法,Quartz Job真正的执行逻辑.
     *
     * @param executorContext executorContext JobExecutionContext中封装有Quartz运行所需要的所有信息
     * @throws JobExecutionException execute()方法只允许抛出JobExecutionException异常
     */
    private Logger logger = LoggerFactory.getLogger(DynamicJob.class);
    public void execute(JobExecutionContext executorContext) throws JobExecutionException {
        //JobDetail中的JobDataMap是共用的,从getMergedJobDataMap获取的JobDataMap是全新的对象
        long ExeStartTime = System.currentTimeMillis();
        JobDataMap map = executorContext.getMergedJobDataMap();
        Integer jobId = map.getInt("id");
        String name = map.getString("name");
        String jobGroup = map.getString("jobGroup");
        String cron = map.getString("cron");
        String parameter = map.getString("parameter");
        String desc = map.getString("desc");
        logger.info("Running JobId: {}", jobId);
        logger.info("Running Job Name: {}", name);
        logger.info("Running Job  Group : {}",jobGroup);
        logger.info("Running Cron : {} ",cron);
        logger.info("Running  Parameter: {} ", parameter);
        logger.info("Running Desc: {} ", desc);
        try {
            JobEntity job= jobDao.getOne(jobId);
            Trigger trigger = executorContext.getTrigger();
            Scheduler scheduler = executorContext.getScheduler();
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            if (triggerState.equals(Trigger.TriggerState.COMPLETE)) {
                job.setStatus(Constant.COMPLETION);
                jobDao.save(job);
                log.info(">>>>>>>>>>>>>Trigger has been completed>>>>>>>>>>>>>");
            }

            System.out.println("任务执行中");
            /*

         此处填写具体执行任务逻辑


             */
        }catch(Exception e){
            e.printStackTrace();
            logger.error("调起任务有错误", e);
        }
        long ExeEndTime = System.currentTimeMillis();
        logger.info(">>>>>>>>>>>>> Running Job has been completed , cost time :  " + (ExeEndTime -ExeStartTime) + "ms\n");
    }

}
