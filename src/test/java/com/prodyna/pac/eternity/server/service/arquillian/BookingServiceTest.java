package com.prodyna.pac.eternity.server.service.arquillian;

import com.prodyna.pac.eternity.server.exception.functional.DuplicateTimeBookingException;
import com.prodyna.pac.eternity.server.exception.functional.InvalidBookingException;
import com.prodyna.pac.eternity.server.exception.functional.UserNotAssignedToProjectException;
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
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.List;

import static com.prodyna.pac.eternity.server.common.PasswordHash.validatePassword;
import static com.prodyna.pac.eternity.server.common.DateUtils.*;

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
    public void createDemoData() throws DuplicateTimeBookingException, UserNotAssignedToProjectException, InvalidBookingException {

        // clean DB from nodes and relations
        cypherService.query("MATCH(n) OPTIONAL MATCH (n)-[r]-() DELETE n,r", null);

        User user1 = new User("khansen", "Knut", "Hansen", "pw");
        User user2 = new User("aeich", "Alexander", null, "pw2");
        User user3 = new User("rvoeller", "Rudi", "Völler", null);
        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        Project project1 = new Project("P00754", "KiBucDu Final (Phase II)");
        Project project2 = new Project("P00843", "IT-/Prozessharmonisierung im Handel");
        Project project3 = new Project("P00998", "Bosch - ST-IPP");
        projectService.create(project1);
        projectService.create(project2);
        projectService.create(project3);

        userService.assignUserToProject(user1, project1);
        userService.assignUserToProject(user2, project1);
        userService.assignUserToProject(user2, project2);

        Booking booking1 = new Booking(getUTCDate(2015, 03, 02, 10, 0), getUTCDate(2015, 03, 02, 14, 0), 5);
        Booking booking2 = new Booking(getUTCDate(2015, 03, 03, 9, 0), getUTCDate(2015, 03, 02, 14, 30), 15);
        Booking booking3 = new Booking(getUTCDate(2015, 03, 05, 9, 0), getUTCDate(2015, 03, 05, 18, 10), 60);
        Booking booking4 = new Booking(getUTCDate(2015, 03, 06, 10, 0), getUTCDate(2015, 03, 06, 15, 0), 30);
        Booking booking5 = new Booking(getUTCDate(2015, 03, 07, 10, 0), getUTCDate(2015, 03, 07, 16, 0), 45);

        bookingService.create(booking1, user1, project1);
        bookingService.create(booking2, user1, project1);
        bookingService.create(booking3, user2, project1);
        bookingService.create(booking4, user2, project2);
        bookingService.create(booking5, user2, project2);

    }

    @Test
    @InSequence(2)
    public void testGet() {

        User user1 = userService.get("khansen");
        List<Booking> bookings = bookingService.findByUser(user1);

        Assert.assertEquals(2, bookings.size());

        Booking b = bookings.get(0);

        Assert.assertNotNull(b);
        Assert.assertNotNull(b.getId());
        Assert.assertEquals(b, bookingService.get(b.getId()));
        Assert.assertNull(bookingService.get("unknown"));

    }

    @Test
    @InSequence(3)
    public void testFindByUser() {

        User user1 = userService.get("khansen");
        User user2 = userService.get("aeich");
        User user3 = userService.get("rvoeller");

        Assert.assertEquals(2, bookingService.findByUser(user1).size());
        Assert.assertEquals(3, bookingService.findByUser(user2).size());
        Assert.assertEquals(0, bookingService.findByUser(user3).size());

    }

    @Test
    @InSequence(4)
    public void testFindByProject() {

        Project project1 = projectService.get("P00754");
        Project project2 = projectService.get("P00843");
        Project project3 = projectService.get("P00998");

        Assert.assertEquals(3, bookingService.findByProject(project1).size());
        Assert.assertEquals(2, bookingService.findByProject(project2).size());
        Assert.assertEquals(0, bookingService.findByProject(project3).size());

    }

    @Test
    @InSequence(5)
    public void testFindByUserAndProject() {

        Project project1 = projectService.get("P00754");
        Project project2 = projectService.get("P00843");
        Project project3 = projectService.get("P00998");
        Project projectU = new Project("unknonw",null);
        User user1 = userService.get("khansen");
        User user2 = userService.get("aeich");
        User user3 = userService.get("rvoeller");
        User userU = new User("unknown",null,null,null);

        Assert.assertEquals(2, bookingService.findByUserAndProject(user1, project1).size());
        Assert.assertEquals(1, bookingService.findByUserAndProject(user2, project1).size());
        Assert.assertEquals(0, bookingService.findByUserAndProject(user3, project1).size());

        Assert.assertEquals(0, bookingService.findByUserAndProject(user1, project2).size());
        Assert.assertEquals(2, bookingService.findByUserAndProject(user2, project2).size());
        Assert.assertEquals(0, bookingService.findByUserAndProject(user3, project2).size());

        Assert.assertEquals(0, bookingService.findByUserAndProject(user1, project3).size());

        Assert.assertEquals(0, bookingService.findByUserAndProject(userU, project1).size());
        Assert.assertEquals(0, bookingService.findByUserAndProject(user1, projectU).size());
        Assert.assertEquals(0, bookingService.findByUserAndProject(userU, projectU).size());

    }

    @Test
    @InSequence(6)
    public void testCreate() {

        Assert.fail();

//    Booking create(@NotNull Booking booking, @NotNull User user, @NotNull Project project)
//            throws NoSuchElementRuntimeException, DuplicateTimeBookingException, UserNotAssignedToProjectException;
    }

    @Test
    @InSequence(7)
    public void testUpdate() {

        Assert.fail();

//    Booking update(@NotNull Booking booking) throws NoSuchElementRuntimeException, DuplicateTimeBookingException;
    }

    @Test
    @InSequence(8)
    public void testDelete() {

        Assert.fail();

//    void delete(@NotNull String id) throws NoSuchElementRuntimeException;
    }

}


