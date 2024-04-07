package orders.GUI;

// import com.github.javafx.chart.zooming.ZoomManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.NumberFormat;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import orders.CustomerPerformance;
import orders.OrderObjects.Order;

/**
 * Class for custom GUI component creation.
 */
public class Components {

    private final static NumberFormat numberFormat =
    NumberFormat.getNumberInstance(Locale.US);


    /**
     * Creates a Hyperlink to the GitHub repository of a project.
     *
     * @return A Hyperlink with an image representing the GitHub logo, linked to
     *         the repository URL.
     */
    public static Hyperlink createGitLink() {
        Hyperlink link = new Hyperlink();
        Image git = new Image("github-mark.png");
        ImageView view = new ImageView(git);
        view.setFitHeight(30);
        view.setFitWidth(30);
        link.setGraphic(view);
        link.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI(
                    "https://github.com/se-5G00DL97/final-assignment-YaKnee"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace(); /////////add failbox with link C+V
            }
        });
        return link;
    }

    /**
     * Generates a table of customer performances based on a list of orders.
     *
     * @param orders ArrayList of Order objects representing orders to analyze.
     * @return A TableView containing customer performance metrics including:
     *         name, ID, total orders, total sales, and total profits.
     *
     * @SuppressWarnings({"unchecked", "deprecation"})
     *      Used to suppress unchecked and deprecated warnings in the method.
     */
    @SuppressWarnings({ "unchecked", "deprecation" })
    public static TableView<CustomerPerformance>
    performanceTable(ArrayList<Order> orders) {
        Map<String, Set<Order>> customerOrders = new HashMap<>();

        for (Order order : orders) {
            String customerId = order.customer().customerId();
            Set<Order> ordersForCustomer =
                customerOrders.computeIfAbsent(
                    customerId, newCustomerId -> new HashSet<>());
            ordersForCustomer.add(order);
        }

        TableView<CustomerPerformance> performanceTable = new TableView<>();
        performanceTable.setColumnResizePolicy(
            TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<CustomerPerformance, String> nameCol =
            new TableColumn<>("Name");
        nameCol.setCellValueFactory(
            new PropertyValueFactory<CustomerPerformance, String>("name"));
        TableColumn<CustomerPerformance, String> idCol =
            new TableColumn<>("ID");
        idCol.setCellValueFactory(
            new PropertyValueFactory<CustomerPerformance, String>("id"));
        TableColumn<CustomerPerformance, String> ordersCol =
            new TableColumn<>("Orders");
        ordersCol.setCellValueFactory(
            new PropertyValueFactory<CustomerPerformance, String>("orders"));
        TableColumn<CustomerPerformance, String> salesCol =
            new TableColumn<>("Sales");
        salesCol.setCellValueFactory(
            new PropertyValueFactory<CustomerPerformance, String>("sales"));
        TableColumn<CustomerPerformance, String> profitsCol =
            new TableColumn<>("Profits");
        profitsCol.setCellValueFactory(
            new PropertyValueFactory<CustomerPerformance, String>("profits"));


        performanceTable.getColumns().addAll(
            nameCol,idCol,ordersCol,salesCol,profitsCol);

        ObservableList<CustomerPerformance> performanceList =
            FXCollections.observableArrayList();
        for (Map.Entry<String, Set<Order>> entry : customerOrders.entrySet()) {
            String customerId = entry.getKey();
            Set<Order> ordersForCustomer = entry.getValue();
            String name = "";
            for (Order order : ordersForCustomer) {
                name = order.customer().name();
                break; 
            }
            int orderSum = entry.getValue().size();
            long totalSales =
                ordersForCustomer.stream().mapToLong(Order::sales).sum();
            long totalProfits =
                ordersForCustomer.stream().mapToLong(Order::profit).sum();

            CustomerPerformance cp =new CustomerPerformance(
                name, customerId, orderSum, totalSales, totalProfits);
            performanceList.add(cp);
        }
        for (TableColumn<CustomerPerformance, ?> column :
                                                performanceTable.getColumns()) {
            column.setComparator((s1, s2) -> {
                try {
                    Integer n1 = Integer.parseInt((String) s1);
                    Integer n2 = Integer.parseInt((String) s2);
                    return n1.compareTo(n2);
                } catch (NumberFormatException e) {
                        return ((String) s1).compareTo((String) s2);

                }
            });
        }
        performanceTable.setItems(performanceList);
        return performanceTable;
    }

    /**
     * ComboBox that suggests inputs based on sorted item list.
     *
     * @param cb    ComboBox to fill.
     * @param list  Set of items to populate the ComboBox.
     */
    public static void autoFillComboBox(ComboBox<String> cb, Set<String> list) {
        TreeSet<String> sortedList = new TreeSet<>();
        sortedList.addAll(list);

        ObservableList<String> items =
            FXCollections.observableArrayList(sortedList);
        FilteredList<String> filteredItems =
            new FilteredList<String>(items);

        cb.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            final TextField search = cb.getEditor();
            final String selected = cb.getSelectionModel().getSelectedItem();

            Platform.runLater(() -> {
                if (selected == null || !selected.equals(search.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().startsWith(newVal.toUpperCase())) {
                            return true;
                        } else {
                            return false;
                        }
                    });
                }
            });
        });
        cb.setEditable(true);
        cb.setItems(filteredItems);
    }

    /**
     * Populates a TableView with data.
     *
     * @param table TableView to populate.
     * @param data  Data to populate the TableView with.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    static void populateSummaryTable(TableView<Order> table,
                                     ObservableList<Order> data){
        table.getItems().clear();
        table.getColumns().clear();

        TableColumn<Order, String> rowCol =
                            createColumn("Row ID", Order::rowID);

        TableColumn<Order, String> dateCol =
                            new TableColumn("Order & Shipping");
        TableColumn<Order, String> orderIDCol =
                            createColumn("Order ID",
                                order -> order.shipOrder().orderId());
        TableColumn<Order, String> orderDateCol =
                            createColumn("Order Date",
                                order -> order.shipOrder().orderDate());
        TableColumn<Order, String> shipDateCol =
                            createColumn("Shipping Date",
                                order -> order.shipOrder().shipDate());
        TableColumn<Order, String> shippingModeCol =
                            createColumn("Shipping Mode",
                                order -> order.shipOrder().shipMode());

        TableColumn<Order, String> customerCol =
                            new TableColumn("Customer");
        TableColumn<Order, String> custNameCol =
                            createColumn("Name",
                                order -> order.customer().name());
        TableColumn<Order, String> custIdCol =
                            createColumn("ID",
                                order -> order.customer().customerId());
        TableColumn<Order, String> segmentCol =
                            createColumn("Segment",
                                order -> order.customer().segment());

        TableColumn<Order, String> locationCol =
                            new TableColumn("Location");
        TableColumn<Order, String> cityCol =
                            createColumn("City",
                                order -> order.location().city());
        TableColumn<Order, String> stateCol =
                            createColumn("State",
                                order -> order.location().state());
        TableColumn<Order, String> postCodeCol =
                            createColumn("Post Code",
                                order -> order.location().postCode());
        TableColumn<Order, String> regionCol =
                            createColumn("Region",
                                order -> order.location().region());
        TableColumn<Order, String> countryCol =
                            createColumn("Country",
                                order -> order.location().country());

        TableColumn<Order, String> productCol =
                            new TableColumn("Products");
        TableColumn<Order, String> prodIDCol =
                            createColumn("ID",
                                order -> order.product().id());
        TableColumn<Order, String> prodCatCol =
                            createColumn("Category",
                                order -> order.product().category());
        TableColumn<Order, String> prodSubCatCol =
                            createColumn("Sub-Category",
                                order -> order.product().subCategory());
        TableColumn<Order, String> prodNameCol =
                            createColumn("Name",
                                order -> order.product().productName());

        TableColumn<Order, String> logisticCol =
                            new TableColumn("Logistics");
        TableColumn<Order, String> salesCol =
                            createColumn("Sales", Order::sales);
        TableColumn<Order, String> quantityCol =
                            createColumn("Quantity", Order::quantity);
        TableColumn<Order, String> discountCol =
                            createColumn("Discount", Order::discount);
        TableColumn<Order, String> profitCol =
                            createColumn("Profit", Order::profit);

        table.setItems(data);

        dateCol.getColumns()
            .addAll(orderIDCol, orderDateCol, shipDateCol, shippingModeCol);
        customerCol.getColumns()
            .addAll(custNameCol, custIdCol, segmentCol);
        locationCol.getColumns()
            .addAll(cityCol, stateCol, postCodeCol, regionCol, countryCol);
        productCol.getColumns()
            .addAll(prodIDCol, prodCatCol, prodSubCatCol, prodNameCol);
        logisticCol.getColumns()
            .addAll(salesCol,quantityCol,discountCol,profitCol);

        table.getColumns().addAll(
            rowCol, dateCol, customerCol, locationCol, productCol, logisticCol);
        for(TableColumn col : table.getColumns()){
            col.setResizable(false);
        }
        table.setEditable(false);
        table.setVisible(true);
    }

    /**
     * Creates a new TableColumn and defines the cellValueFactory for it.
     *
     * @param <S> Type of the TableView row (e.g., Order)
     * @param <T> Type of the property of the row to be displayed in the column
     * @param title Title of the column
     * @param property Function representing the property of the row
     * @return A new TableColumn instance
     */
    public static <S, T> TableColumn<S, String> createColumn(String title,
                                                      Function<S, T> property) {
        TableColumn<S, String> column = new TableColumn<>(title);
        column.setCellValueFactory(cellData -> {
            T value = property.apply(cellData.getValue());
            if (value != null) {
                return new SimpleStringProperty(value.toString());
            } else {
                return new SimpleStringProperty("");
            }
        });
        //column sorting order
        column.setComparator((s1, s2) -> {
            try {
                Integer n1 = Integer.parseInt(s1);
                Integer n2 = Integer.parseInt(s2);
                return n1.compareTo(n2);
            } catch (NumberFormatException e) {
                try {
                    DateTimeFormatter formatter
                        = DateTimeFormatter.ofPattern("d.M.yyyy");
                    LocalDate date1 = LocalDate.parse(s1, formatter);
                    LocalDate date2 = LocalDate.parse(s2, formatter);
                    return date1.compareTo(date2);
                } catch (DateTimeParseException err) {
                    return s1.compareTo(s2);
                }
            }
        });
        return column;
    }


    /**
     * Creates a LineChart based on data map.
     *
     * @param map     Data map.
     * @param parent  Parent category.
     * @param child   Child category.
     * @return        Created LineChart.
     */
    @SuppressWarnings("unchecked")
    public static LineChart<String, Number>createChart(
            Map<String, ? extends Number> map, String parent, String child) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(child);
        yAxis.setLabel(parent);
        yAxis.setForceZeroInRange(false);

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle(parent + " per " + child);

        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        series1.setName(parent);

        long total = map.values().stream().mapToLong(Number::longValue).sum();
        double average = total / map.size();
        XYChart.Series<String, Number> series2 = new XYChart.Series<>();
        series2.setName("Average");

        map.forEach((ch, p) -> {
            series1.getData().add(new XYChart.Data<>(ch, p));
            series2.getData().add(new XYChart.Data<>(ch, average));
        });

        chart.getData().addAll(series2, series1);

        for (XYChart.Data<String, Number> dataPoint : series1.getData()) {
            Tooltip.install(dataPoint.getNode(), new Tooltip(
                dataPoint.getXValue().toString() + ": "
                + numberFormat.format(dataPoint.getYValue())));
        }
        for (XYChart.Data<String, Number> dataPoint : series2.getData()) {
            Tooltip.install(dataPoint.getNode(), new Tooltip(
                "Average: " + numberFormat.format(average)));
            dataPoint.getNode().getStyleClass().add("average-data-point");
        }
        //tooltip for whole series
        Tooltip.install(series2.getNode(), new Tooltip("Average: "
        + numberFormat.format(average)));
        
        chart.setLegendVisible(true);

        // EventController.handleChartZoom(chart, xAxis, yAxis);

        return chart;
    }
}
