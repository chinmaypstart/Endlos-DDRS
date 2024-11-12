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
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

/**
 * This class configures quartz scheduler properties.
 *
 * @author nirav
 */
@Component
public class SchedulerShutdown implements ServletContextListener {

    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // do nothing
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            schedulerFactoryBean.getScheduler().shutdown(true);
            int ct = 0;
            // Try waiting for the scheduler to shutdown. Only wait 30 seconds.
            while (ct < 30) {
                ct++;
                Thread.sleep(1000);
                if (schedulerFactoryBean.getScheduler().isShutdown()) {
                    break;
                }
            }
        } catch (SchedulerException | InterruptedException e) {
            LoggerService.exception(e);
        }
    }

}
