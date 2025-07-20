import com.google.gson.Gson;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Configuration {
    //the current amount of tickets in the ticketPool
    private int totalTickets;

    //how often a vendor releases a ticket in milliseconds
    private int ticketReleaseRate;

    //how often a customer buys a ticket in  milliseconds
    private int customerRetrievalRate;

    //the maximum ticket capacity of the ticketPool
    private int maxTicketCapacity;

    //number of vendors in the ticketing system
    private int numberOfVendors;

    //number of customers in the ticketing system
    private int numberOfCustomers;


    //Setters
    public void setTotalTickets(int totalTickets) {
        this.totalTickets = totalTickets;
    }

    public void setTicketReleaseRate(int ticketReleaseRate) {
        this.ticketReleaseRate = ticketReleaseRate;
    }

    public void setCustomerRetrievalRate(int customerRetrievalRate) {
        this.customerRetrievalRate = customerRetrievalRate;
    }

    public void setMaxTicketCapacity(int maxTicketCapacity) {
        this.maxTicketCapacity = maxTicketCapacity;
    }

    public void setNumberOfVendors(int numberOfVendors) {
        this.numberOfVendors = numberOfVendors;
    }

    public void setNumberOfCustomers(int numberOfCustomers) {
        this.numberOfCustomers = numberOfCustomers;
    }

    //Getters
    public int getTotalTickets() {
        return totalTickets;
    }

    public int getTicketReleaseRate() {
        return ticketReleaseRate;
    }

    public int getCustomerRetrievalRate() {
        return customerRetrievalRate;
    }

    public int getMaxTicketCapacity() {
        return maxTicketCapacity;
    }

    @Override
    public String toString() {
        return "\nConfiguration : " +
                "\nTotal Tickets =" + totalTickets +
                "\nTicket Release Rate=" + ticketReleaseRate +
                "\nCustomer Retrieval Rate=" + customerRetrievalRate +
                "\nMaximum Number of Ticket Capacity=" + maxTicketCapacity +
                "\nNumber Of Vendors = " + numberOfVendors +
                "\nNumber Of Customers = " + numberOfCustomers +
                '\n';
    }

    // Method to save object as JSON using Gson
    public static void saveConfigurationJson(Configuration config, String filename) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Method to load object from JSON using Gson
    public static Configuration loadConfigurationJson(String filename) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filename)) {
            return gson.fromJson(reader, Configuration.class);
        }
    }
}