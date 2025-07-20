import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        // Initialize a configuration object to store system settings
        Configuration config = new Configuration();

        Scanner input = new Scanner(System.in);

        System.out.println("\nWelcome to the Real-Time Event Ticketing System\n");

        // Prompt the user to input the total number of tickets in the system
        int totalTickets;
        while (true) {
            try {
                System.out.print("The total tickets available in the system: ");
                totalTickets = input.nextInt();
                if (totalTickets >= 0) {
                    break;
                }
                System.out.println("Invalid input. Please enter a positive number.");
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }
        config.setTotalTickets(totalTickets);

        // Prompt the user for how frequently vendors add tickets (in milliseconds)
        int ticketReleaseRate;
        while (true) {
            try {
                System.out.print("How frequently vendors add tickets(in milliseconds): ");
                ticketReleaseRate = input.nextInt();
                if (ticketReleaseRate >= 0) {
                    break;
                }
                System.out.println("Invalid input. Please enter a positive number.");
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }
        config.setTicketReleaseRate(ticketReleaseRate);

        // Prompt the user for how often customers purchase tickets (in milliseconds)
        int customerRetrievalRate;
        while (true) {
            try {
                System.out.print("How often customers purchase tickets(in milliseconds): ");
                customerRetrievalRate = input.nextInt();
                if (customerRetrievalRate >= 0) {
                    break;
                }
                System.out.println("Invalid input. Please enter a positive number.");
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }
        config.setCustomerRetrievalRate(customerRetrievalRate);

        // Prompt the user for the maximum ticket capacity the system can hold
        int maxTicketCapacity;
        while (true) {
            try {
                System.out.print("Maximum capacity of tickets the system can hold: ");
                maxTicketCapacity = input.nextInt();
                if (maxTicketCapacity >= 0 && maxTicketCapacity >= totalTickets) {
                    break;
                }
                System.out.println("Invalid input. Please enter a positive number which is higher than or the same as the current total number of tickets in the system.");
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }
        config.setMaxTicketCapacity(maxTicketCapacity);

        // Prompt the user for the number of vendors
        int numberOfVendors;
        while (true) {
            try {
                System.out.print("How many vendors: ");
                numberOfVendors = input.nextInt();
                if (numberOfVendors >= 0) {
                    break;
                }
                System.out.println("Invalid input. Please enter a positive number.");
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }
        config.setNumberOfVendors(numberOfVendors);

        // Prompt the user for the number of customers
        int numberOfCustomers;
        while (true) {
            try {
                System.out.print("How many customers: ");
                numberOfCustomers = input.nextInt();
                if (numberOfCustomers >= 0) {
                    break;
                }
                System.out.println("Invalid input. Please enter a positive number.");
            }
            catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                input.nextLine();
            }
        }
        config.setNumberOfCustomers(numberOfCustomers);

        // Display the configuration settings
        System.out.println(config);
        // Save the configuration settings to a JSON file
        Configuration.saveConfigurationJson(config, "configuration.json");

        // Initialize the TicketPool with the maximum capacity and total tickets
        TicketPool ticketPool = new TicketPool(maxTicketCapacity, totalTickets);

        // AtomicBoolean used to control stopping the threads safely
        AtomicBoolean stopFlag = new AtomicBoolean(false);

        // List to hold all the threads
        List<Thread> threads = new ArrayList<>();

        System.out.println("Commands: START / STOP");
        while (true) {
            System.out.print("Enter Command: ");
            String command = input.nextLine().toUpperCase();

            switch (command) {
                case "START":
                    System.out.println("Ticket handling operations started.");
                    // Start Vendors
                    int startingTicketNumber = totalTickets + 1;
                    for (int i = 1; i <= numberOfVendors; i++) {
                        Vendor vendor = new Vendor(i, startingTicketNumber, ticketReleaseRate, ticketPool, stopFlag);
                        Thread vendorThread = new Thread(vendor, "Vendor-" + i);
                        threads.add(vendorThread);
                        vendorThread.start();
                        startingTicketNumber += maxTicketCapacity; // Ensure unique ticket numbers
                    }

                    // Start Customers
                    for (int i = 1; i <= numberOfCustomers; i++) {
                        Customer customer = new Customer(i, ticketPool, customerRetrievalRate, stopFlag);
                        Thread customerThread = new Thread(customer, "Customer-" + i);
                        threads.add(customerThread);
                        customerThread.start();
                    }

                    break;

                case "STOP":
                    // Stop the ticket handling operations by setting the stopFlag to true
                    stopFlag.set(true);
                    // Wait for all threads (vendors and customers) to finish
                    threads.forEach(t -> {
                        try {
                            t.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    System.out.println("All ticket handling operations stopped.");
                    return;

                default:
                    System.out.println("Invalid command. Use START / STOP");
                    break;
            }
        }
    }
}