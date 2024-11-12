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
package com.endlosiot.common.config.quartz;

import com.endlosiot.common.notification.batchjob.TransactionalEmailJob;
import com.endlosiot.common.notification.batchjob.TransactionalSmsJob;
import com.endlosiot.devicediagnosis.batchjob.AlarmHistoryJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * This class configures quartz scheduler properties.
 *
 * @author nirav
 */
@Component
@Configuration
public class SchedulerRegister implements ApplicationRunner {

    @Value("${send.communication}")
    private boolean sendCommunication;
    @Value("${send.sms}")
    private boolean sendSMS;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    public JobDetail transactionEmailJob() {
        JobKey jobKey = new JobKey("TransactionEmail", "live-unique-life-api");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "TransactionEmail");
        return JobBuilder.newJob(TransactionalEmailJob.class).withIdentity(jobKey).setJobData(jobDataMap).build();
    }

    public Trigger transactionEmailCronTrigger() {
        return TriggerBuilder.newTrigger().withIdentity("TransactionEmail", "live-unique-life-api")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
    }

    public JobDetail transactionSmsJob() {
        JobKey jobKey = new JobKey("TransactionSms", "live-unique-life-api");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "TransactionSms");
        return JobBuilder.newJob(TransactionalSmsJob.class).withIdentity(jobKey).setJobData(jobDataMap).build();
    }

    public JobDetail alarmHistoryJob() {
        JobKey jobKey = new JobKey("AlarmHistoryJob", "endlos-iot-api");
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("jobName", "AlarmHistoryJob");
        return JobBuilder.newJob(AlarmHistoryJob.class).withIdentity(jobKey).setJobData(jobDataMap).build();
    }

    public Trigger transactionSmsCronTrigger() {
        return TriggerBuilder.newTrigger().withIdentity("TransactionSms", "live-unique-life-api")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
    }

    public Trigger alarmHistoryJobCronTrigger() {
        return TriggerBuilder.newTrigger().withIdentity("AlarmHistory", "endlos-iot-api")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
    }

//	public JobDetail userMigrationJob() {
//		JobKey jobKey = new JobKey("userMigrationJob", "live-unique-life-api");
//		JobDataMap jobDataMap = new JobDataMap();
//		jobDataMap.put("jobName", "userMigrationJob");
//		return JobBuilder.newJob(UserMigrationJob.class).withIdentity(jobKey).setJobData(jobDataMap).build();
//	}

//	public Trigger userMigrationCronTrigger() {
//		return TriggerBuilder.newTrigger().withIdentity("userMigrationJob", "live-unique-life-api")
//				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0,12 * * ?")).build();
//	}

//	public JobDetail productMigrationJob() {
//		JobKey jobKey = new JobKey("productMigrationJob", "live-unique-life-api");
//		JobDataMap jobDataMap = new JobDataMap();
//		jobDataMap.put("jobName", "productMigrationJob");
//		return JobBuilder.newJob(ProductMigrationJob.class).withIdentity(jobKey).setJobData(jobDataMap).build();
//	}

//	public Trigger productMigrationCronTrigger() {
//		return TriggerBuilder.newTrigger().withIdentity("productMigrationJob", "live-unique-life-api")
//				.withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever()).build();
//	}

//	public JobDetail wareHouseJob() {
//		JobKey jobKey = new JobKey("wareHouseJob", "live-unique-life-api");
//		JobDataMap jobDataMap = new JobDataMap();
//		jobDataMap.put("jobName", "wareHouseJob");
//		return JobBuilder.newJob(WareHouseJob.class).withIdentity(jobKey).setJobData(jobDataMap).build();
//	}
//
//	public Trigger wareHouseCronTrigger() {
//		return TriggerBuilder.newTrigger().withIdentity("wareHouseJob", "live-unique-life-api")
//				.withSchedule(CronScheduleBuilder.cronSchedule("0 0 0,12 * * ?")).build();
//	}

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        scheduler.getListenerManager().addJobListener(new SchedulerJobListener());
        if (sendCommunication) {
            JobDetail transactionEmailJobDetail = transactionEmailJob();
            Trigger transactionEmailTrigger = transactionEmailCronTrigger();
            scheduler.scheduleJob(transactionEmailJobDetail, transactionEmailTrigger);
        }
        if (sendSMS) {
            JobDetail transactionSMSJobDetail = transactionSmsJob();
            Trigger transactionSMSTrigger = transactionSmsCronTrigger();
            scheduler.scheduleJob(transactionSMSJobDetail, transactionSMSTrigger);
        }
        JobDetail AlarmHistoryJobJobDetail = alarmHistoryJob();
        Trigger alarmHistoryJobTrigger = alarmHistoryJobCronTrigger();
        scheduler.scheduleJob(AlarmHistoryJobJobDetail, alarmHistoryJobTrigger);

//		scheduler.scheduleJob(userMigrationJob(), userMigrationCronTrigger());
//		scheduler.scheduleJob(wareHouseJob(), wareHouseCronTrigger());
//		JobDetail productMigrationDetail = productMigrationJob();
//		Trigger productMigrationReportTrigger = productMigrationCronTrigger();
//		scheduler.scheduleJob(productMigrationDetail, productMigrationReportTrigger);
    }
}