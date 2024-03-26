package orders;

import orders.OrderObjects.*;

public record Order(int rowID, ShippingOrder shipOrder, Customer customer, Location location,
                    Product product, int sales, int quantity, int discount, int profit){ }
