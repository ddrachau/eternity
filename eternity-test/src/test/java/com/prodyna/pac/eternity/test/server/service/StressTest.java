package com.prodyna.pac.eternity.test.server.service;

import com.prodyna.pac.eternity.authentication.service.AuthenticationService;
import com.prodyna.pac.eternity.authentication.service.SessionService;
import com.prodyna.pac.eternity.booking.service.BookingService;
import com.prodyna.pac.eternity.common.helper.CalendarBuilder;
import com.prodyna.pac.eternity.common.helper.RememberMeAccessor;
import com.prodyna.pac.eternity.common.model.booking.Booking;
import com.prodyna.pac.eternity.common.model.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.common.model.exception.functional.ElementAlreadyExistsException;
import com.prodyna.pac.eternity.common.model.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.common.model.exception.functional.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.common.model.project.Project;
import com.prodyna.pac.eternity.common.model.user.User;
import com.prodyna.pac.eternity.common.profiling.MethodProfiling;
import com.prodyna.pac.eternity.common.profiling.ProfilingMXBean;
import com.prodyna.pac.eternity.project.service.ProjectService;
import com.prodyna.pac.eternity.test.helper.AbstractArquillianTest;
import com.prodyna.pac.eternity.test.helper.DatabaseCleaner;
import com.prodyna.pac.eternity.user.service.UserService;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;

//@RunWith(Arquillian.class)
public class StressTest extends AbstractArquillianTest {

    @Inject
    private Logger logger;

    @Inject
    private DatabaseCleaner databaseCleaner;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Inject
    private BookingService bookingService;

    @Inject
    private AuthenticationService authenticationService;

    @Inject
    private SessionService sessionService;

    @Inject
    private CalendarBuilder calendarBuilder;

    @Inject
    private RememberMeAccessor rememberMeAccessor;

    @Inject
    private ProfilingMXBean profilingMXBean;

//    @Test
    @InSequence(1)
    public void createDemoData() throws InvalidBookingException, DuplicateTimeBookingException, UserNotAssignedToProjectException, ElementAlreadyExistsException {

        databaseCleaner.deleteAllData();

        ArrayList<Long> avgBookingCreation = new ArrayList<>();
        ArrayList<Long> maxBookingCreation = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            User user = new User("id" + i, "User " + i, "Test", "pw");
            Project project = new Project("P" + i, "Test Project " + i);

            user = userService.create(user);
            project = projectService.create(project);
            userService.assignUserToProject(user, project);

            for (int y = 2000; y < 2025; y++) {
                for (int m = 1; m < 12; m++) {
                    for (int d = 1; d < 25; d++) {
                        Booking booking = new Booking(calendarBuilder.getCalendar(y, m, d, 10, 0), calendarBuilder.getCalendar(y, m, d, 14, 0), 5);
                        bookingService.create(booking, user, project);
                    }
                }
                avgBookingCreation.add(profilingMXBean.getMethodExecutionDurationAverage("com.prodyna.pac.eternity.booking.service.impl.BookingServiceImpl.create"));
                maxBookingCreation.add(profilingMXBean.getMethodExecutionDurationMaximum("com.prodyna.pac.eternity.booking.service.impl.BookingServiceImpl.create"));
            }


        }

        for (MethodProfiling mp : profilingMXBean.getAllProfilings()) {
            logger.info(mp.toString());
        }

        logger.info(avgBookingCreation.toString());
        logger.info(maxBookingCreation.toString());

    }

}
