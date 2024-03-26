package orders.CSVHandler;


import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileReader;
import java.util.ArrayList;
import orders.OrderObjects.*;


import orders.Order;

public class CSVDataReader {
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

                                                    //Country, state,     State,    Post Code,  Region
                    Location location = new Location(order[8], order[9], order[10], order[11], order[12]);
                                                //productId, category, sub-cat,      name
                    Product product = new Product(order[13], order[14], order[15], order[16]);
                    //Commas were randomly interpolated in the data that weren't shown in Excel,
                    //so replace with blanks
                    int sales = replaceCommaAndParse(order[17], rowID);
                    int quantity = replaceCommaAndParse(order[18], rowID);
                    int discount = replaceCommaAndParse(order[19], rowID);
                    int profit = replaceCommaAndParse(order[20], rowID);
                    Order fullOrder = new Order(rowID, shipOrder, customer, location,
                            product, sales, quantity, discount, profit);
                    allOrders.add(fullOrder);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse data in Row ID: " + order[0]);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allOrders;
    }

    private static int replaceCommaAndParse(String orderItem, int rowID){
        try{
            return Integer.parseInt(orderItem.replace(",",""));
        }catch (NumberFormatException e){
            System.err.println("Failed to parse string: " + orderItem + ", in row with id: " + rowID);
            return 0;
        }
    }
}
