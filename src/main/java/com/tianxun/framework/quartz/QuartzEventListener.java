package com.tianxun.framework.quartz;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * Quartz任务调度监控。
 * 
 * @author zhangweirui
 */
public class QuartzEventListener implements JobListener {
	private static final Logger log = Logger.getLogger(QuartzEventListener.class);

	@Override
	public String getName() {
		return QuartzEventListener.class.getName();
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		log.info(context.getJobDetail().getDescription() + "开始运行……");
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		log.info(context.getJobDetail().getDescription() + "被否决！");
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		if(jobException == null){
			log.info(context.getJobDetail().getDescription() + "运行完成。");
		}else{
			log.error(context.getJobDetail().getDescription() + "运行失败！", jobException);
		}
	}
	
}

