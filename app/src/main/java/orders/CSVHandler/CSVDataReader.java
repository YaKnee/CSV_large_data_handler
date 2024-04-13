package orders.CSVHandler;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Label;
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
    public static ArrayList<Order> createOrders(String file, Label error) {
        ArrayList<Order> allOrders = new ArrayList<>();
        try (CSVReader csvReader = createCSVReader(file, error)) {
            csvReader.iterator().forEachRemaining(order -> {
                try {
                    parseOrderObjects(allOrders, order);
                } catch (NumberFormatException e) {
                    setErrorLabel(error, "Failed to parse data in Row ID: "
                                        + order[0]);
                }
            });
        } catch (Exception e) {
            setErrorLabel(error, "Failed to read the CSV file: " + file);
            e.printStackTrace();
        }
        return allOrders;
    }


    public static ArrayList<Order> createReturns(String file, Label error) {
        ArrayList<String> returnIDs = new ArrayList<>();
        try (CSVReader csvReader = createCSVReader(file, error)) {
             csvReader.iterator().forEachRemaining(returnId -> {
            try {
                returnIDs.add(returnId[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                setErrorLabel(error,
                    "Failed to parse data: index out of bounds.");
            }
         });
        } catch (IOException e) {
            setErrorLabel(error, "Failed to read the Return CSV file.");
            e.printStackTrace();
        }

        ArrayList<Order> allReturns = new ArrayList<>();
        try (CSVReader csvReader =
        createCSVReader("data/SuperStoreOrders.csv", error)) {
            csvReader.iterator().forEachRemaining(order -> {
                try {
                    if (returnIDs.contains(order[1])) {
                        parseOrderObjects(allReturns, order);
                    }
                } catch (NumberFormatException e) {
                    setErrorLabel(error, e.getMessage());
                } catch (ArrayIndexOutOfBoundsException e) {
                    setErrorLabel(error,
                        "Failed to parse data: index out of bounds.");
                }
            });
        } catch (Exception e) {
            setErrorLabel(error, "Failed to read the Order CSV file.");
            e.printStackTrace();
        }
        return allReturns;
    }

    /**
     * Creates a CSVReader instance for the specified file.
     * 
     * @param file   path to the CSV file to be read
     * @param error  label to display the error message if an exception occurs
     * @return       CSVReader instance
     * @throws RuntimeException if exception occurs during creation of CSVReader
     */
    protected static CSVReader createCSVReader(String file, Label error) {
        try {
            return new CSVReaderBuilder(new FileReader(file))
                    .withSkipLines(1)
                    .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                    .build();
        } catch (FileNotFoundException e) {
            setErrorLabel(error, "Failed to create CSV reader as\n\""
                + file + "\"\n was not found.");
            throw new RuntimeException(
                "Failed to create CSV reader for file: " + file, e);
        }
    }

    /**
     * Parses array of strings into an Order object and adds it to list.
     * 
     * @param list  the list to add the parsed Order object to
     * @param order the array of strings to parse
     */
    protected static void parseOrderObjects(ArrayList<Order> list, String[] order) {
        int rowID = Integer.parseInt(order[0]);
        ShippingOrder shipOrder = //orderId, orderDate, shipDate, Ship Mode
            new ShippingOrder(order[1],order[2],order[3], order[4]);
        Customer customer = //customerId, Name, Segment
            new Customer(order[5], order[6], order[7]);
        Location location = //Country, City,  State,    Post Code,  Region
            new Location(order[8], order[9], order[10], order[11], order[12]);
        Product product = //productId, category, sub-category, name
            new Product(order[13], order[14], order[15], order[16]);
        int sales = replaceCommaAndParse(order[17], rowID);
        int quantity = replaceCommaAndParse(order[18], rowID);
        int discount = replaceCommaAndParse(order[19], rowID);
        int profit = replaceCommaAndParse(order[20], rowID);
        Order fullOrder = new Order(rowID, shipOrder, customer, location,
        product, sales, quantity, discount, profit);
        list.add(fullOrder);
    }

    /**
     * Sets text of the Label to the provided message and makes Label visible.
     * 
     * @param error        the Label to display the error message
     * @param errorMessage the error message to display
     */
    private static void setErrorLabel(Label error, String errorMessage) {
        error.setText(errorMessage);
        error.setVisible(true);
    }

    /**
     * Replaces all commas in a string with empty space and then parse
     * string as an integer.
     * If the parsing is successful, the integer value is returned
     * If the parsing fails, exception is thrown
     * 
     * @param orderItem The string to be processed
     * @param rowID The ID of the row that the string is from
     * @return The integer value of the string after replacing commas,
     * @throws NumberFormatException if parsing fails
     */
    protected static int replaceCommaAndParse(String orderItem, int rowID){
        try{
            return Integer.parseInt(orderItem.replace(",",""));
        }catch (NumberFormatException e){
            throw new NumberFormatException("Failed to parse string: "
                                    + orderItem + ", in row with id: " + rowID);
        }
    }
}
