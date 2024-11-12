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

import com.endlosiot.common.logger.LoggerService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

/**
 * This class is used to handle the job listner event. Bascially, You can use
 * this class to handle each job's event and generate a log for the same.
 *
 * @author nirav
 */

public class SchedulerJobListener implements JobListener {

    public static final String SCHEDULER_JOB_LISTENER = "SchedulerJobListener";

    @Override
    public String getName() {
        return SCHEDULER_JOB_LISTENER;
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {

    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {
        String jobName = jobExecutionContext.getJobDetail().getKey().toString();
        LoggerService.debug(SCHEDULER_JOB_LISTENER, "Job To Be Executed", jobName);
    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException jobException) {
        if (jobException != null) {
            LoggerService.exception(jobException);
            return;
        }
        String jobName = jobExecutionContext.getJobDetail().getKey().toString();

        LoggerService.debug(SCHEDULER_JOB_LISTENER, "Job Completed", jobName);
    }
}
