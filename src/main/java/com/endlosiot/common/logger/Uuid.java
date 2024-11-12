package com.endlosiot.common.logger;

/**
 * This class is used to store uuid in thread local which will be used in logging every messagae.
 *
 * @author chetanporwal
 * @since 25/08/2018
 */
public class Uuid {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

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
