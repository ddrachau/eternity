//package com.prodyna.pac.eternity.server.notification.sms;
//
//import com.prodyna.pac.common.logging.Logging;
//import com.prodyna.pac.notification.NotificationSender;
//import org.slf4j.Logger;
//
//import javax.ejb.Asynchronous;
//import javax.inject.Inject;
//
//@Logging
//@SMS
//public class SMSNotificationSender implements NotificationSender {
//
//    @Inject
//    private Logger log;
//
//    @Override
//    @Asynchronous
//    public void sendNotification(String notification) {
//        log.info("Notification via SMS " + notification);
//    }
//}
