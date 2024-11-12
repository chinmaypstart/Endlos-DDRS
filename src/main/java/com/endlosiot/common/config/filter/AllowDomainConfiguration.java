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
package com.endlosiot.common.config.filter;

import com.endlosiot.common.config.property.AppUrlProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * This is a allow domain configuration which specify which domain is allowed or
 * not.
 *
 * @author Nirav.Shah
 * @since 15/05/2019
 */

@ComponentScan
@Configuration
public class AllowDomainConfiguration {

    @Autowired
    private AppUrlProperty appUrlProperty;

    public boolean isAllowDomain(String domain) {
        String[] allowDomainsArray = appUrlProperty.getAllowDomains().split(",");
        return Arrays.stream(allowDomainsArray).anyMatch(
                allowDomain -> (("http://" + allowDomain).equals(domain) || ("https://" + allowDomain).equals(domain)));
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            if (StringUtils.isEmpty(appUrlProperty.getAllowDomains())) {
                int exitCode = SpringApplication.exit(ctx, (ExitCodeGenerator) () -> 0);
                System.exit(exitCode);
            }
        };
    }
}