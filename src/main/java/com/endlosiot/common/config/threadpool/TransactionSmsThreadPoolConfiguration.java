package com.endlosiot.common.config.threadpool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * This class contains all sync method configurations.
 *
 * @author Nirav.Shah
 * @since 15/05/2019
 */
@ComponentScan
@Configuration
@EnableAsync(proxyTargetClass = true)
public class TransactionSmsThreadPoolConfiguration {
    @Value("${transaction.sms.executor.corepoolsize}")
    private int transactionSmsCorePoolSize;

    @Value("${transaction.sms.executor.maxpoolsize}")
    private int transactionSmsMaxPoolSize;

    @Value("${transaction.sms.executor.queuesize}")
    private int transactionSmsQueuesize;

    @Bean(name = "transactionSmsExecutor")
    public Executor transactionSmsExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(transactionSmsCorePoolSize);
        executor.setMaxPoolSize(transactionSmsMaxPoolSize);
        executor.setQueueCapacity(transactionSmsQueuesize);
        executor.setThreadNamePrefix("SmsThread-");
        executor.initialize();
        return executor;
    }

    public int getTransactionSmsCorePoolSize() {
        return transactionSmsCorePoolSize;
    }

    public void setTransactionSmsCorePoolSize(int transactionSmsCorePoolSize) {
        this.transactionSmsCorePoolSize = transactionSmsCorePoolSize;
    }

    public int getTransactionSmsMaxPoolSize() {
        return transactionSmsMaxPoolSize;
    }

    public void setTransactionSmsMaxPoolSize(int transactionSmsMaxPoolSize) {
        this.transactionSmsMaxPoolSize = transactionSmsMaxPoolSize;
    }

    public int getTransactionSmsQueuesize() {
        return transactionSmsQueuesize;
    }

    public void setTransactionSmsQueuesize(int transactionSmsQueuesize) {
        this.transactionSmsQueuesize = transactionSmsQueuesize;
    }
}
