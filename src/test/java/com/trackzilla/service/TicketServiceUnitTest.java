package com.trackzilla.service;

import com.trackzilla.entity.Application;
import com.trackzilla.entity.Release;
import com.trackzilla.entity.Ticket;
import com.trackzilla.repository.ReleaseRepository;
import com.trackzilla.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class TicketServiceUnitTest {
    @Mock
    private TicketRepository ticketRepository;

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Test
    public void listTickets(){
        ArrayList<Ticket> listOfTickets = new ArrayList<Ticket>();

        Ticket ticket = new Ticket();

        Application application = new Application("application1", "Jim Smith", "first application");
        Release release = new Release("first release", "01/01/1900");

        ticket.setTitle("Sort Feature");
        ticket.setDescription("first release");
        ticket.setStatus("OPEN");
        ticket.setApplication(application);
        ticket.setRelease(release);

        listOfTickets.add(ticket);

        Mockito.when(ticketService.listTickets()).thenReturn(listOfTickets);

        assertThat(ticketService.listTickets().size()).isEqualTo(1);
    }

    @Test
    public void findTicket(){
        Long ticketId = new Long(1);

        Ticket ticket = new Ticket();

        Application application = new Application("application1", "Jim Smith", "first application");
        Release release = new Release("first release", "01/01/1900");

        ticket.setTitle("Sort Feature");
        ticket.setDescription("first release");
        ticket.setStatus("OPEN");
        ticket.setApplication(application);
        ticket.setRelease(release);

        Optional<Ticket> app = Optional.of( ticket );

        when(ticketRepository.findById(ticketId)).thenReturn(app);

        assertThat(ticketService.findTicket(ticketId)).isNotEmpty();
    }

    @Test
    public void deleteTicket(){

        Long ticketId = new Long(1);

        doNothing().when(ticketRepository).deleteById(ticketId);

        ticketService.delete(ticketId);

        verify(ticketRepository, times(1)).deleteById(ticketId);
    }


    @Test
    public void saveTicket(){
        Ticket ticket = new Ticket();

        Application application = new Application("application1", "Jim Smith", "first application");
        Release release = new Release("first release", "01/01/1900");

        ticket.setTitle("Sort Feature");
        ticket.setDescription("first release");
        ticket.setStatus("OPEN");
        ticket.setApplication(application);
        ticket.setRelease(release);

        when(ticketRepository.save(ticket)).thenReturn(ticket);

        ticket = ticketService.save(ticket);
        assertThat(ticket).isNotNull();
    }


}
