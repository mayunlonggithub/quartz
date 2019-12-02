package com.ealen.Controller;

import com.ealen.service.JobService;
import com.ealen.util.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

/**
 * Created by Ma on 2019/12/1 21：22
 */
@RestController
@Slf4j
public class JobController {

    @Autowired
    private JobService jobService;
    //初始化启动所有的Job
    private static Logger logger = LoggerFactory.getLogger(JobController.class);
    @PostConstruct
    public void initialize() {
        try {
            jobService.restartAllJobs();
            logger.info("INIT SUCCESS");
        } catch (SchedulerException e) {
            logger.info("INIT EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 根据服务id启动task
     *
     * @return
     */
    @GetMapping("/runJob/{jobId}")
    public ResponseResult<Void> runTask(@PathVariable("jobId") Integer taskId) {
       jobService.runJob(taskId);
        return new ResponseResult(true,"请求成功");
    }

    /**
     * 根据ID关闭某个Job
     *
     * @param id
     * @return
     * @throws SchedulerException
     */
    @GetMapping("/shutDownJob/{jobId}")
    public ResponseResult<Void> shutDownTask(@PathVariable("jobId") Integer jobId) {
        jobService.shutDownJob(jobId);
        return new ResponseResult(true,"请求成功");
    }
    /**
     * 执行全部任务
     * @throws SchedulerException
     */
    @GetMapping("/runAllJobs")
    public ResponseResult<Void> startAllJobs( ) {
        jobService.runAllJobs();
        return new ResponseResult(true,"请求成功");
    }

    /**
     *停止全部任务
     *
     */
    @GetMapping("/shutDownAllJobs")
    public ResponseResult<Void> shutDownAllTasks( ) {
        jobService.shutDownAllJobs();
        return new ResponseResult(true,"请求成功");
    }
}
