package com.trackzilla.service;



import com.trackzilla.entity.Ticket;

import java.util.List;

public interface TicketService {
    List<Ticket> listTickets();
    Ticket save(Ticket ticket);
    void delete(long id);
}


