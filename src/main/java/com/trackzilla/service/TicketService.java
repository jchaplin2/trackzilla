package com.trackzilla.service;



import com.trackzilla.entity.Release;
import com.trackzilla.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<Ticket> listTickets();
    Ticket save(Ticket ticket);
    void delete(long id);
    Optional<Ticket> findTicket(long id);
}


