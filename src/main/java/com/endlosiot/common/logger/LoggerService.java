package com.endlosiot.common.logger;

/**
 * This class is used to log information base on their type. Logging level Hierarchy is as below
 * OFF, FATAL, ERROR, WARN, INFO, DEBUG, TRACE, ALL.
 *
 * <p>1. OFF - No logging will be done. 2. FATAL - Only fatal logs will be generated in log
 * appender(file/console). 3. ERROR - FATAL & ERROR logs will be generated. 4. WARN - WARN, ERROR &
 * FATAL logs will be generated. 5. INFO - INFO, WARN, ERROR & FATAL logs will be generated. 6.
 * DEBUG - DEBUG, INFO, WARN, ERROR & FATAL logs will be generated. 7. TRACE - TRACE, DEBUG, INFO,
 * WARN, ERROR & FATAL logs will be generated. 8. ALL - ALL logs will be generated.
 *
 * <p>Need to set this property in at run time to enable async logger.
 * -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector.
 *
 * <p>Log4j2 has a shutdown hook (for non-web applications) that takes care of waiting for the
 * background thread to process any events still in the queue. So the best thing to do is to not
 * stop the appenders when they are still being used. Let log4j2 take care of the cleanup.
 */

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class LoggerService {

    private LoggerService() {
    }

    // 1. Exception will be logged as FATAL logger Type.
    private static final Logger FATAL = LogManager.getLogger("exception");

    // 2. Error/custom generated exception will be logged here.
    private static final Logger ERROR = LogManager.getLogger("error");

    // 3. All performance related logs (Time taken by each request) will be logged here.
    private static final Logger INFO = LogManager.getLogger("access");

    // 4. Debug logs will stored in this file. This logs will be useful in development.
    private static final Logger DEBUG = LogManager.getLogger("debug");

    /**
     * This method is used to log exception.
     *
     * @param throwable
     */
    public static void exception(Throwable throwable) {
        FATAL.fatal(Notification.create(), throwable);
    }

    /**
     * This method is used to log error messages.
     *
     * @param className
     * @param transactionName
     * @param message
     */
    public static void error(String className, String transactionName, String message) {
        ERROR.error(Notification.create(className, transactionName, message));
    }

    /**
     * This method is used to log error messages.
     *
     * @param message
     */
    public static void error(String message) {
        ERROR.error(Notification.create(message));
    }

    /**
     * This method is used to log information messages.
     *
     * @param className
     * @param transactionName
     * @param message
     */
    public static void info(String className, String transactionName, String message) {
        INFO.info(Notification.create(className, transactionName, message));
    }

    /**
     * This method is used to log debug messages.
     *
     * @param className
     * @param transactionName
     * @param message
     */
    public static void debug(String className, String transactionName, String message) {
        DEBUG.debug(Notification.create(className, transactionName, message));
    }
}
