/*******************************************************************************
 * Copyright -2019 @intentlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
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
public class TransactionEmailThreadPoolConfiguration {

    @Value("${transaction.email.executor.corepoolsize}")
    private int transactionEmailCorePoolSize;

    @Value("${transaction.email.executor.maxpoolsize}")
    private int transactionEmailMaxPoolSize;

    @Value("${transaction.email.executor.queuesize}")
    private int transactionEmailQueuesize;

    @Bean(name = "transactionEmailExecutor")
    public Executor transactionEmailExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(transactionEmailCorePoolSize);
        executor.setMaxPoolSize(transactionEmailMaxPoolSize);
        executor.setQueueCapacity(transactionEmailQueuesize);
        executor.setThreadNamePrefix("TransactionEmailThread-");
        executor.initialize();
        return executor;
    }

    public int getTransactionEmailCorePoolSize() {
        return transactionEmailCorePoolSize;
    }

    public void setTransactionEmailCorePoolSize(int transactionEmailCorePoolSize) {
        this.transactionEmailCorePoolSize = transactionEmailCorePoolSize;
    }

    public int getTransactionEmailMaxPoolSize() {
        return transactionEmailMaxPoolSize;
    }

    public void setTransactionEmailMaxPoolSize(int transactionEmailMaxPoolSize) {
        this.transactionEmailMaxPoolSize = transactionEmailMaxPoolSize;
    }

    public int getTransactionEmailQueuesize() {
        return transactionEmailQueuesize;
    }

    public void setTransactionEmailQueuesize(int transactionEmailQueuesize) {
        this.transactionEmailQueuesize = transactionEmailQueuesize;
    }
}