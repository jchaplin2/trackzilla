package com.trackzilla.repository;

import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;
import com.trackzilla.entity.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class TicketRepositoryUnitTest {

    @Autowired
    private TicketRepository ticketRepository;

    Application application;
    Release release;

    @BeforeEach
    public void setup(){
        application = new Application("application1", "Jim Smith", "first application");
        release = new Release("first release", "01/01/1900");
        Ticket ticket = new Ticket("Sort Feature", "Add the ability to sort tickets by severity", application, release,"OPEN");

        ticketRepository.save(ticket);
    }

    @Test
    public void findByIdTest(){
        assertThat(ticketRepository.findById(Long.parseLong("1"))).isNotNull();
    }

}
