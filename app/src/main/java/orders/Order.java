package orders;

public record Order(Customer customer, String orderDate, String shippingMode, Location location,
                    Product product, int sales, int quantity, int discount, int profit){
//    private Customer customer;
//    private String orderDate;
//    private String shippingMode;
//    private Location location;
//    private Product product;
//    private int sales;
//    private int quantity;
//    private int discount;
//    private int profit;

//    public Order(Customer customer, String orderDate, String shippingMode, Location location, Product product, int sales, int quantity, int discount, int profit) {
//        this.customer = customer;
//        this.orderDate = orderDate;
//        this.shippingMode = shippingMode;
//        this.location = location;
//        this.product = product;
//        this.sales = sales;
//        this.quantity = quantity;
//        this.discount = discount;
//        this.profit = profit;
//    }

}
