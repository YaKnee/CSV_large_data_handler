package orders;

import javafx.beans.property.SimpleStringProperty;

/**
 * Represents a customer's performance.
 */
public class CustomerPerformance{
    private final SimpleStringProperty name;
    private final SimpleStringProperty id;
    private final SimpleStringProperty segment;
    private final SimpleStringProperty orders;
    private final SimpleStringProperty sales;
    private final SimpleStringProperty profits;

    /**
     * Creates a new CustomerPerformance object.
     * 
     * @param name    the customer's name
     * @param id      the customer's ID
     * @param segment the customer's segment
     * @param orders  the number of orders made by the customer
     * @param sales   the total sales made by the customer
     * @param profits the total profits made by the customer
     */
    public CustomerPerformance(String name, String id, String segment,
    int orders, long sales, long profits) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.segment = new SimpleStringProperty(segment);
        this.orders = new SimpleStringProperty(String.valueOf(orders));
        this.sales = new SimpleStringProperty(String.valueOf(sales));
        this.profits = new SimpleStringProperty(String.valueOf(profits));
    }

    /**
     * Returns the customer's name.
     * 
     * @return name
     */
    public String getName() {
        return name.get();
    }

    /**
     * Returns the customer's ID.
     * 
     * @return ID
     */
    public String getId() {
        return id.get();
    }

    /**
     * Returns the customer's segment.
     * 
     * @return segment
     */
    public String getSegment() {
        return segment.get();
    }

    /**
     * Returns the number of orders made by the customer.
     * 
     * @return total number of orders made by customer
     */
    public String getOrders() {
        return orders.get();
    }

    /**
     * Returns the sales made by the customer.
     * 
     * @return total sales made by customer
     */
    public String getSales() {
        return sales.get();
    }

    /**
     * Returns the profits made by the customer.
     * 
     * @return total profits made by customer
     */
    public String getProfits() {
        return profits.get();
    }
}
