package com.endlosiot.common.notification.thread;

import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.notification.enums.TransactionStatusEnum;
import com.endlosiot.common.notification.model.EmailAccountModel;
import com.endlosiot.common.notification.model.TransactionalEmailModel;
import com.endlosiot.common.notification.provider.EmailProvider;
import com.endlosiot.common.notification.provider.EmailProviderFactory;
import com.endlosiot.common.notification.service.TransactionalEmailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;


@Component
public class EmailThread {

    @Autowired
    private TransactionalEmailService transactionalEmailService;

    @Async("transactionEmailExecutor")
    public void sendTransactionEmail(final TransactionalEmailModel transactionalEmailModel,
                                     final EmailAccountModel emailAccountModel) {
        try {
            transactionEmail(transactionalEmailModel, emailAccountModel);
        } catch (MessagingException e) {
            LoggerService.exception(e);
        }
    }

    // @Async("transactionEmailRetry")
    public void retryTeEmail(final TransactionalEmailModel transactionalEmailModel,
                             final EmailAccountModel emailAccountModel) {
        try {
            transactionEmail(transactionalEmailModel, emailAccountModel);
        } catch (MessagingException e) {
            LoggerService.exception(e);
        }
    }

    private void transactionEmail(final TransactionalEmailModel transactionalEmailModel,
                                  final EmailAccountModel emailAccountModel) throws MessagingException {
        String error = "";
        boolean isSuccessful = false;
        Properties properties = setEmailProperties(emailAccountModel);
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailAccountModel.getUsername(), emailAccountModel.getPassword());
            }
        });
        MimeMessage mimeMessage = setCommonMimeMessage(properties, emailAccountModel, session);
        setTeMimeMessage(mimeMessage, transactionalEmailModel);
        // JavaMailSenderImpl javaMailSenderImpl = setJavaMailSender(emailAccountModel,
        // properties);
        long startTime = System.currentTimeMillis();
        Transport transport = null;
        try {
            transport = session.getTransport();
            transport.connect();
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            isSuccessful = true;
        } catch (Exception e) {
            LoggerService.exception(e);
            error = e.getMessage();
            updateTransactionEmail(isSuccessful, error, transactionalEmailModel.getId(), startTime,
                    System.currentTimeMillis());
        } finally {
            if (transport != null) {
                transport.close();
            }
        }
    }

    private MimeMessage setCommonMimeMessage(Properties properties, EmailAccountModel emailAccountModel,
                                             Session session) throws MessagingException {
        Address[] cc = new Address[]{new InternetAddress(emailAccountModel.getReplyToEmail())};
        MimeMessage mimeMessage = new MimeMessage(session);
        mimeMessage.setReplyTo(cc);
        mimeMessage.setHeader("Content-type", "text/html");
        // mimeMessage.setContent(email.getBody(), "text/html; charset=utf-8");
        mimeMessage.setFrom(new InternetAddress(emailAccountModel.getEmailFrom()));
        return mimeMessage;
    }

    /**
     * This method sets Mine Message property.
     *
     * @return
     * @throws MessagingException
     */
    private MimeMessage setTeMimeMessage(MimeMessage mimeMessage, TransactionalEmailModel transactionalEmailModel)
            throws MessagingException {
        mimeMessage.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(transactionalEmailModel.getEmailTo()));
        if (transactionalEmailModel.getEmailCc() != null) {
            mimeMessage.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(transactionalEmailModel.getEmailCc()));
        }
        if (transactionalEmailModel.getEmailBcc() != null) {
            mimeMessage.setRecipients(Message.RecipientType.BCC,
                    InternetAddress.parse(transactionalEmailModel.getEmailBcc()));
        }
        String htmltext = transactionalEmailModel.getBody();
        mimeMessage.setSubject(transactionalEmailModel.getSubject());
        Multipart multipart = new MimeMultipart();

        BodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmltext, "text/html; charset=utf-8");
        htmlPart.setDisposition(BodyPart.INLINE);
        multipart.addBodyPart(htmlPart);

        if (!StringUtils.isBlank(transactionalEmailModel.getAttachmentPath())) {
            htmlPart = new MimeBodyPart();
            DataSource source = new FileDataSource(transactionalEmailModel.getAttachmentPath());
            htmlPart.setDataHandler(new DataHandler(source));
            htmlPart.setFileName(source.getName());
            multipart.addBodyPart(htmlPart);
        }
        mimeMessage.setContent(multipart);
        return mimeMessage;
    }

    /**
     * This method set email properties base on provider type.
     *
     * @return
     */
    private Properties setEmailProperties(EmailAccountModel emailAccountModel) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", emailAccountModel.getHost());
        properties.put("mail.smtp.port", emailAccountModel.getPort());
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.debug", "false");
        properties.setProperty("mail.smtp.timeout", String.valueOf(emailAccountModel.getTimeOut()));
        if (emailAccountModel.getAuthenticationMethod() != null) {
            properties.put("mail.smtp.auth.mechanisms", emailAccountModel.getAuthenticationMethod().getName());
        }
        EmailProvider provider = EmailProviderFactory.loadEmailProvider(emailAccountModel.getProviderType());
        if (provider != null) {
            provider.setProperties(properties, Integer.parseInt(String.valueOf(emailAccountModel.getPort())));
        }
        return properties;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateTransactionEmail(boolean isSuccessful, String error, long emailDetalisId, long startTime,
                                       long endTime) {
        try {
            /*
             * TransactionalEmailModel emailDetails =
             * transactionalEmailService.get(emailDetalisId);
             */
            if (isSuccessful) {
                /*
                 * emailDetails.setStatus(Status.SENT.getId()); emailDetails.setDateSent(new
                 * Date()); if(StringUtils.isBlank(emailDetails.getTxtError())){
                 * emailDetails.setTxtError(null); }
                 */
            } else {
                TransactionalEmailModel emailDetails = transactionalEmailService.get(emailDetalisId);
                emailDetails.setStatus((TransactionStatusEnum.FAILED.getId()));
                emailDetails.setTxtError(error);
                transactionalEmailService.update(emailDetails);
            }
            /* transactionalEmailService.update(emailDetails); */
        } catch (Exception e) {
            LoggerService.exception(e);
        }
    }
}
