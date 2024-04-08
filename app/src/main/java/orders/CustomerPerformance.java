package orders;

import javafx.beans.property.SimpleStringProperty;

public class CustomerPerformance{
    private final SimpleStringProperty name;
    private final SimpleStringProperty id;
    private final SimpleStringProperty segment;
    private final SimpleStringProperty orders;
    private final SimpleStringProperty sales;
    private final SimpleStringProperty profits;

    public CustomerPerformance(String name, String id, String segment, int orders, long sales, long profits) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
        this.segment = new SimpleStringProperty(segment);
        this.orders = new SimpleStringProperty(String.valueOf(orders));
        this.sales = new SimpleStringProperty(String.valueOf(sales));
        this.profits = new SimpleStringProperty(String.valueOf(profits));
    }

    public String getName() {
        return name.get();
    }
    
    public String getId() {
        return id.get();
    }

    public String getSegment() {
        return segment.get();
    }
    
    public String getOrders() {
        return orders.get();
    }
    
    public String getSales() {
        return sales.get();
    }
    
    public String getProfits() {
        return profits.get();
    }

}
