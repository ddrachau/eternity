//package com.prodyna.pac.eternity.server.notification;
//
//import com.prodyna.pac.common.logging.Logging;
//import com.prodyna.pac.notification.mail.Mail;
//import org.slf4j.Logger;
//
//import javax.enterprise.event.Observes;
//import javax.inject.Inject;
//
//@Logging
//public class AbteilungsEventObserver {
//
//    @Inject
//    private Logger log;
//
//    @Inject
//    @Mail
//    private NotificationSender notificationSender;
//
//    public void abteilungCreatedEvent(@Observes AbteilungCreatedEvent ace) {
//        notificationSender.sendNotification("Abteilung " + ace.getAbteilung().getName() + " created");
//    }
//
//}
