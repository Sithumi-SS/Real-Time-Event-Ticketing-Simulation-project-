import java.util.concurrent.atomic.AtomicBoolean;

class Customer implements Runnable {
    private final TicketPool ticketPool;

    // Rate at which the customer tries to buy tickets (in milliseconds)
    private final int customerRetrievalRate;

    // Flag to control when the customer thread should stop
    private final AtomicBoolean stopFlag;
    private final int customerId;

    public Customer(int customerId, TicketPool ticketPool, int customerRetrievalRate, AtomicBoolean stopFlag) {
        this.customerId = customerId;
        this.ticketPool = ticketPool;
        this.customerRetrievalRate = customerRetrievalRate;
        this.stopFlag = stopFlag;
    }

    // The run method is executed when the thread starts
    @Override
    public void run() {
        while (!stopFlag.get()) {
            // Attempt to buy a ticket from the TicketPool
            ticketPool.buyTicket(customerId);
            try {
                Thread.sleep(customerRetrievalRate);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}