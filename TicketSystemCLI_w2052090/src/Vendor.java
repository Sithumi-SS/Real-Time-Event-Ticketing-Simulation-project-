import java.util.concurrent.atomic.AtomicBoolean;

class Vendor implements Runnable {
    // Start from totalTickets + 1
    private int ticketNumber;

    //how many millisecond does the vendor take to add a ticket
    private final int ticketReleaseRate;

    private final TicketPool ticketPool;

    //boolean to start and stop vendor threads
    private final AtomicBoolean stopFlag;
    private final int vendorId;

    public Vendor(int vendorId, int startingTicketNumber, int ticketReleaseRate, TicketPool ticketPool, AtomicBoolean stopFlag) {
        this.vendorId = vendorId;
        this.ticketNumber = startingTicketNumber;
        this.ticketReleaseRate = ticketReleaseRate;
        this.ticketPool = ticketPool;
        this.stopFlag = stopFlag;
    }

    // The run method is executed when the thread starts
    @Override
    public void run() {
        while (!stopFlag.get()) {
            ticketPool.addTicket(ticketNumber++, vendorId);
            try {
                // Sleep for the milliseconds(ticketReleaseRate) before adding the next ticket
                Thread.sleep(ticketReleaseRate);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}