package orders.CSVHandler;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileReader;
import java.util.ArrayList;
import orders.OrderObjects.*;


/**
 * A utility class for reading and parsing data into objects from a CSV file.
 */

public class CSVDataReader {

    /**
     * Creates a list of Order objects from a CSV file.
     * 
     * @param file Path to the CSV file
     * @return List of Order objects
     * @throws Exception If there is an error reading file or parsing data
     */
    public static ArrayList<Order> createOrders(String file) {
        ArrayList<Order> allOrders = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file))
                .withSkipLines(1)
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            csvReader.iterator().forEachRemaining(order -> {
                try {
                    int rowID = Integer.parseInt(order[0]);
                                                            //orderId, orderDate, shipDate, Ship Mode
                    ShippingOrder shipOrder = new ShippingOrder(order[1],order[2],order[3], order[4]);
                                                    //customerId, Name, Segment
                    Customer customer = new Customer(order[5], order[6], order[7]);
                                                    //Country, City,     State,    Post Code,  Region
                    Location location = new Location(order[8], order[9], order[10], order[11], order[12]);
                                                //productId, category, sub-category, name
                    Product product = new Product(order[13], order[14], order[15], order[16]);
                    int sales = replaceCommaAndParse(order[17], rowID);
                    int quantity = replaceCommaAndParse(order[18], rowID);
                    int discount = replaceCommaAndParse(order[19], rowID);
                    int profit = replaceCommaAndParse(order[20], rowID);
                    Order fullOrder = new Order(rowID, shipOrder, customer, location,
                            product, sales, quantity, discount, profit);
                    allOrders.add(fullOrder);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse data in Row ID: "
                                        + order[0]);
                }
            });
        } catch (Exception e) {
            System.err.println("Failed to read the CSV file.");
            e.printStackTrace();
        }
        return allOrders;
    }


    public static ArrayList<Order> createReturns(String file) {
        ArrayList<String> returnIDs = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(file))
        .withSkipLines(1)
        .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
        .build()) {
             csvReader.iterator().forEachRemaining(returnId -> {
            try {
                returnIDs.add(returnId[1]);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse data");
            }
         });
        } catch (Exception e) {
            System.err.println("Failed to read the Return CSV file.");
            e.printStackTrace();
        }

        ArrayList<Order> allReturns = new ArrayList<>();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader("data/SuperStoreOrders.csv"))
                .withSkipLines(1)
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .build()) {
            csvReader.iterator().forEachRemaining(order -> {
                try {
                    if (returnIDs.contains(order[1])) {
                        int rowID = Integer.parseInt(order[0]);
                                                                //orderId, orderDate, shipDate, Ship Mode
                        ShippingOrder shipOrder = new ShippingOrder(order[1],order[2],order[3], order[4]);
                                                        //customerId, Name, Segment
                        Customer customer = new Customer(order[5], order[6], order[7]);
                                        //Country, City,     State,    Post Code,  Region
                        Location location = new Location(order[8], order[9], order[10], order[11], order[12]);
                                    //productId, category, sub-category, name
                        Product product = new Product(order[13], order[14], order[15], order[16]);
                        int sales = replaceCommaAndParse(order[17], rowID);
                        int quantity = replaceCommaAndParse(order[18], rowID);
                        int discount = replaceCommaAndParse(order[19], rowID);
                        int profit = replaceCommaAndParse(order[20], rowID);
                        Order fullOrder = new Order(rowID, shipOrder, customer, location,
                        product, sales, quantity, discount, profit);
                        allReturns.add(fullOrder);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse data in Row ID: "
                                        + order[0]);
                }
            });
        } catch (Exception e) {
            System.err.println("Failed to read the Order CSV file.");
            e.printStackTrace();
        }
        return allReturns;
    }

    /**
     * Replaces all commas in a string with empty space and then parse
     * string as an integer.
     * If the parsing is successful, the integer value is returned.
     * If the parsing fails, an error message is printed and -69 is returned.
     * 
     * @param orderItem The string to be processed
     * @param rowID The ID of the row that the string is from
     * @return The integer value of the string after replacing commas,
     * or -69 if parsing fails
     */
    protected static int replaceCommaAndParse(String orderItem, int rowID){
        try{
            return Integer.parseInt(orderItem.replace(",",""));
        }catch (NumberFormatException e){
            System.err.println("Failed to parse string: " + orderItem
                                + ", in row with id: " + rowID);
            return -69;
        }
    }
}
