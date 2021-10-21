package com.trackzilla.entity;

import org.junit.Test;

public class TicketUnitTest {

    @Test
    public void testGetters(){
        Application application = new Application("application1", "Jim Smith", "first application");
        Release release = new Release("first release", "01/01/1900");
        Ticket ticket = new Ticket("Sort Feature", "Add the ability to sort tickets by severity", application, release,"OPEN");

        assert(ticket.getTitle()).equals("Sort Feature");
        assert(ticket.getDescription()).equals("Add the ability to sort tickets by severity");
        assert(ticket.getApplication()).equals(application);
        assert(ticket.getRelease()).equals(release);
    }

    @Test
    public void testGettersAndSetters(){
        Ticket ticket = new Ticket();

        Application application = new Application("application1", "Jim Smith", "first application");
        Release release = new Release("first release", "01/01/1900");

        ticket.setTitle("Sort Feature");
        ticket.setDescription("first release");
        ticket.setStatus("OPEN");
        ticket.setApplication(application);
        ticket.setRelease(release);

        assert(ticket.getTitle()).equals("Sort Feature");
        assert(ticket.getDescription()).equals("first release");
        assert(ticket.getApplication()).equals(application);
        assert(ticket.getRelease()).equals(release);
    }

}
