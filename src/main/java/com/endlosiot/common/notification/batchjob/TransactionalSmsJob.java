package com.endlosiot.common.notification.batchjob;

import com.endlosiot.common.config.threadpool.TransactionSmsThreadPoolConfiguration;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.notification.model.TransactionalSmsModel;
import com.endlosiot.common.notification.service.SmsAccountService;
import com.endlosiot.common.notification.service.TransactionalSmsService;
import com.endlosiot.common.notification.thread.SmsThread;
import com.endlosiot.common.util.Constant;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@DisallowConcurrentExecution
@Component
public class TransactionalSmsJob implements Job {
    @Autowired
    TransactionalSmsService transactionalSmsService;

    @Autowired
    SmsAccountService smsAccountService;

    @Autowired
    SmsThread smsThread;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        long startTime = System.currentTimeMillis();

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) applicationContext
                .getBean("transactionSmsExecutor");
        if (taskExecutor.getActiveCount() == taskExecutor.getMaxPoolSize() && taskExecutor.getThreadPoolExecutor()
                .getQueue().size() == new TransactionSmsThreadPoolConfiguration().getTransactionSmsQueuesize()) {
            LoggerService.error("TransactionSmsJob", "Quartz scheduler", "Queue and pool is full.");
            return;
        }
        List<TransactionalSmsModel> transactionalSmsModelList = transactionalSmsService.getSmsList(10);
        for (TransactionalSmsModel transactionalSmsModel : transactionalSmsModelList) {
            smsThread.sendTransactionSms(transactionalSmsModel, transactionalSmsModel.getSmsAccountModel());
        }
        jobDataMap.put("Records", transactionalSmsModelList.size());
        jobDataMap.put(Constant.TOTAL_TIME_TAKEN_BY_JOB, System.currentTimeMillis() - startTime);
        return;
    }
}
