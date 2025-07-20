import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Class representing the ticket pool where tickets are managed
class TicketPool {
    private final int maximumTicketCapacity;

    // List to hold the tickets, which is thread-safe using synchronizedList
    private final List<Integer> tickets = Collections.synchronizedList(new ArrayList<>());

    public TicketPool(int maximumTicketCapacity, int totalTickets) {
        this.maximumTicketCapacity = maximumTicketCapacity;
        // Initialize with totalTickets
        for (int i = 1; i <= totalTickets; i++) {
            tickets.add(i);
        }
    }

    // Synchronized method to add a new ticket to the pool by a vendor
    public synchronized void addTicket(int ticketNumber, int vendorId) {
        while (tickets.size() >= maximumTicketCapacity) {
            try {
                // Wait until that space has become available
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        tickets.add(ticketNumber);
        // Log the vendor adding ticket
        Logger.log("Vendor " + vendorId + " added ticket: " + ticketNumber + " | TicketPool size: " + tickets.size());
        notifyAll();
    }

    // Synchronized method for a customer to buy a ticket from the pool
    public synchronized int buyTicket(int customerId) {
        while (tickets.isEmpty()) {
            try {
                //wait until a ticket becomes available
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // Remove and return the ticket from the pool
        int ticket = tickets.remove(0);
        // Log the customer buying ticket
        Logger.log("Customer " + customerId + " bought ticket: " + ticket + " | TicketPool size: " + tickets.size());
        notifyAll();
        return ticket;
    }

}