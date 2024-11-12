/*******************************************************************************
 * Copyright -2021 @Alumnly
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
package com.endlosiot.common.threadlocal;

/**
 * This class is used to store uuid in thread local which will be used in
 * logging every messagae.
 *
 * @author Nirav.Shah
 * @since 03/08/2018
 */
public class Uuid {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    private Uuid() {
    }

    /**
     * To set UUID in thread local.
     *
     * @return User
     */
    public static String getUuid() {
        return threadLocal.get();
    }

    /**
     * To get UUID in thread local.
     *
     * @param user
     */
    public static void setUuid(String uuid) {
        threadLocal.set(uuid);
    }

    public static void removeUuid() {
        threadLocal.remove();
    }
}
