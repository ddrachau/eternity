//package com.prodyna.pac.eternity.server.notification;
//
//import com.prodyna.pac.Abteilung;
//import com.prodyna.pac.AbteilungService;
//import org.slf4j.Logger;
//
//import javax.decorator.Decorator;
//import javax.decorator.Delegate;
//import javax.enterprise.event.Event;
//import javax.inject.Inject;
//
//@Decorator
//public abstract class NotifyingAbteilungServiceDecorator implements AbteilungService {
//
//    @Inject
//    private Logger log;
//
//    @Inject
//    @Delegate
//    private AbteilungService delegate;
//
//    @Inject
//    private Event<AbteilungCreatedEvent> event;
//
//    @Override
//    public Abteilung create(Abteilung abteilung) {
//        Abteilung a = delegate.create(abteilung);
//        event.fire( new AbteilungCreatedEvent( abteilung ) );
//        return a;
//    }
//}
