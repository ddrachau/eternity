package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.exception.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.server.exception.InvalidPasswordException;
import com.prodyna.pac.eternity.server.exception.NoSuchElementRuntimeException;
import com.prodyna.pac.eternity.server.exception.UserNotAssignedToProjectException;
import com.prodyna.pac.eternity.server.model.Booking;
import com.prodyna.pac.eternity.server.model.Project;
import com.prodyna.pac.eternity.server.model.User;
import com.prodyna.pac.eternity.server.service.BookingService;
import com.prodyna.pac.eternity.server.service.CypherService;
import com.prodyna.pac.eternity.server.service.ProjectService;
import com.prodyna.pac.eternity.server.service.UserService;
import junit.framework.Assert;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.List;

import static com.prodyna.pac.eternity.server.common.PasswordHash.validatePassword;

@RunWith(Arquillian.class)
public class BookingServiceTest extends AbstractArquillianTest {

    @Inject
    private CypherService cypherService;

    @Inject
    private UserService userService;

    @Inject
    private ProjectService projectService;

    @Inject
    private BookingService bookingService;

    @Test
    @InSequence(1)
    public void createDemoData() {

        // clean DB from nodes and relations
        cypherService.query("MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r", null);

        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        User user2 = new User("aeich", "Alexander", null, "pw2");
        User user3 = new User("rvoeller", "Rudi", "Völler", null);
        User user4 = new User("bborg", "Björn", "Borg", "pw");
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);
        userService.create(user4);

    }

    @Test
    @InSequence(2)
    public void testCreate() {

        Assert.fail();

//    Booking create(@NotNull Booking booking, @NotNull User user, @NotNull Project project)
//            throws NoSuchElementRuntimeException, DuplicateTimeBookingException, UserNotAssignedToProjectException;
    }

    @Test
    @InSequence(3)
    public void testGet() {

        Assert.fail();

//    Booking get(String id);
    }

    @Test
    @InSequence(1)
    public void testFindByUser() {

        Assert.fail();

//    List<Booking> findByUser(@NotNull User user) throws;
    }

    @Test
    @InSequence(1)
    public void testFindByProject() {

        Assert.fail();

//    List<Booking> findByProject(@NotNull Project project) throws ;
    }

    @Test
    @InSequence(1)
    public void testFindByUserAndProject() {

        Assert.fail();

//    List<Booking> findByUserAndProject(@NotNull User user, @NotNull Project project) throws ;
    }

    @Test
    @InSequence(1)
    public void testUpdate() {

        Assert.fail();

//    Booking update(@NotNull Booking booking) throws NoSuchElementRuntimeException, DuplicateTimeBookingException;
    }

    @Test
    @InSequence(1)
    public void testDelete() {

        Assert.fail();

//    void delete(@NotNull String id) throws NoSuchElementRuntimeException;
    }

}


