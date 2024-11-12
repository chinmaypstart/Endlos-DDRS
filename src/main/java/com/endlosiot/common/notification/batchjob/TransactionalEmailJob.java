package com.endlosiot.common.notification.batchjob;

import com.endlosiot.common.config.threadpool.TransactionEmailThreadPoolConfiguration;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.notification.model.EmailAccountModel;
import com.endlosiot.common.notification.model.TransactionalEmailModel;
import com.endlosiot.common.notification.service.EmailAccountService;
import com.endlosiot.common.notification.service.TransactionalEmailService;
import com.endlosiot.common.notification.thread.EmailThread;
import com.endlosiot.common.util.Constant;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;

@DisallowConcurrentExecution
@Component
public class TransactionalEmailJob implements Job {

    @Autowired
    TransactionalEmailService transactionalEmailService;

    @Autowired
    EmailAccountService emailAccountService;

    @Autowired
    EmailThread emailThread;

    @Autowired
    ApplicationContext applicationContext;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        long startTime = System.currentTimeMillis();

        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) applicationContext
                .getBean("transactionEmailExecutor");
        if (taskExecutor.getActiveCount() == taskExecutor.getMaxPoolSize() && taskExecutor.getThreadPoolExecutor()
                .getQueue().size() == new TransactionEmailThreadPoolConfiguration().getTransactionEmailQueuesize()) {
            LoggerService.error("TransactionEmailJob", "Quartz scheduler", "Queue and pool is full.");
            return;
        }
        List<TransactionalEmailModel> transactionalEmailModelList = transactionalEmailService.getEmailList(10);
        for (TransactionalEmailModel transactionalEmailModel : transactionalEmailModelList) {
            EmailAccountModel emailAccountModel = emailAccountService.get(transactionalEmailModel.getEmailAccountId());
            emailThread.sendTransactionEmail(transactionalEmailModel, emailAccountModel);
        }
        jobDataMap.put("Records", transactionalEmailModelList.size());
        jobDataMap.put(Constant.TOTAL_TIME_TAKEN_BY_JOB, System.currentTimeMillis() - startTime);
        return;
    }

}
