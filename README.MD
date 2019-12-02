### 1. 基本的实现效果: 对定时任务的自定义逻辑，实现单个任务的启动和停止，批量任务的启动和停止。

### 2.本例子中的测试url:
 #### 访问localhost:8080/runAllJobs      开始所有正在执行的任务
 #### 访问localhost:8080/shutDownAllJObs 停止所有正在执行的任务
 #### 访问localhost:8080/runJob/{jobId}  开始特定的任务
 #### 访问localhost:8080/shutDownJob/{jobId} 停止特定的任务
 
### 3.在你启动该应用之前:
 #### 请准备JoB的原数据表：job_entity.sql
 #### 请准备Quartz的元数据表 : quartz_innodb.sql
             
          
