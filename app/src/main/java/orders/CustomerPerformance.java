package orders;

import javafx.beans.property.SimpleStringProperty;

public class CustomerPerformance{
    public final SimpleStringProperty name;
    public final SimpleStringProperty id;
    public final SimpleStringProperty orders;
    public final SimpleStringProperty sales;
    public final SimpleStringProperty profits;

    public CustomerPerformance(String name, String id, int orders, long sales, long profits) {
        this.name = new SimpleStringProperty(name);
        this.id = new SimpleStringProperty(id);
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
