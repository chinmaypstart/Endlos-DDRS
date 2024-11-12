package com.endlosiot.common.logger;

/**
 * This class prepares data which needs to be logged using log4j2.
 *
 * @author chetanporwal
 * @since 25/08/2023
 */
public class Notification {

    private String message;
    private String className;
    private String transactionName;

    private Notification(String className, String transcationName, String message) {
        this.className = className;
        this.transactionName = transcationName;
        this.message = message;
    }

    private Notification(String message) {
        this.message = message;
    }

    private Notification() {
    }

    /**
     * To Create object.
     *
     * @param className
     * @param transactionName
     * @param message
     * @return
     */
    public static Notification create(String className, String transactionName, String message) {
        return new Notification(className, transactionName, message);
    }

    /**
     * To Create object.
     *
     * @param message
     * @return
     */
    public static Notification create(String message) {
        return new Notification(message);
    }

    public static Notification create() {
        return new Notification();
    }

    public String getClassName() {
        return className;
    }

    public String getMessage() {
        return message;
    }

    public String getTransactionName() {
        return transactionName;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Uuid.getUuid()).append(", ");
        if (className != null) {
            stringBuilder.append(className).append(", ");
        }
        if (transactionName != null) {
            stringBuilder.append(transactionName).append(", ");
        }
        if (message != null) {
            stringBuilder.append(message);
        }
        return stringBuilder.toString();
    }
}
