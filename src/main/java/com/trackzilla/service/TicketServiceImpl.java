package com.trackzilla.service;

import com.trackzilla.entity.Ticket;
import com.trackzilla.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Override
    public List<Ticket> listTickets() {
        return (List<Ticket>) ticketRepository.findAll();
    }

    @Override
    public Ticket save(Ticket tick) {
        return ticketRepository.save(tick);
    }

    @Override
    public void delete(long id) {
        ticketRepository.deleteById(id);
    }

}
