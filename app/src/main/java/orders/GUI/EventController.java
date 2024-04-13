package orders.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import orders.OrderObjects.Order;

/**
 * Handles events for GUI s.
 */
public class EventController {

    private ArrayList<Order> orders;
    private GridPane grid;
    private LineChart<Number, Number> chart;
    private TableView<Map.Entry<String, ? extends Number>> table;
    private ComboBox<String> parentChoice;
    private ComboBox<String> childChoice;
    private ToggleGroup tg;
    private RadioButton radioChart;
    private HBox descBox;

    /**
     * Constructor to initialize EventController with given parameters.
     * 
     * @param orders       list of orders
     * @param grid         grid pane
     * @param chart        line chart
     * @param totalsTable  table view
     * @param parentChoice main category combo box 
     * @param childChoice  sub-category combo box 
     * @param tg           toggle group
     * @param radioChart   radio button
     * @param descBox     descriptive text for chart controls
     */
    public EventController(ArrayList<Order> orders, GridPane grid,
    LineChart<Number, Number> chart, 
    TableView<Map.Entry<String, ? extends Number>> totalsTable,
    ComboBox<String> parentChoice, ComboBox<String> childChoice,
    ToggleGroup tg, RadioButton radioChart, HBox descBox){
        this.orders = orders;
        this.grid = grid;
        this.chart = chart;
        this.table = totalsTable;
        this.tg = tg;
        this.radioChart = radioChart;
        this.parentChoice = parentChoice;
        this.childChoice = childChoice;
        this.descBox = descBox;
        addListeners();
    }

    /**
     * Populates a TableView with order summary information based on the 
     * customer nameInput selected from a ComboBox.
     *
     * @param btn       Button triggering the action.
     * @param cb        ComboBox containing customer nameInputs.
     * @param header    Label to display the header information.
     * @param table     TableView to populate with order summary data.
     * @param nameAndID Map containing customer names as keys and IDs as values.
     * @param orders    Map containing IDs as keys and sets of orders as values.
     */
    public static void orderSummaryTableFromName(Button btn, 
            ComboBox<String> cb, Label header, TableView<Order> table, 
            Map<String, String> nameAndID, Map<String, Set<Order>> orders) {
        btn.setOnAction(e-> {
            String nameInput = cb.getEditor().getText();
            if (nameInput == null || nameInput.isEmpty()) {
                header.setText("Empty search bar.");
                table.setVisible(false);
                return;
            }
            for (String name : nameAndID.keySet()) {
                if (name.equalsIgnoreCase(nameInput)) {
                    header.setText(name);
                    ObservableList<Order> data =
                    FXCollections.observableArrayList(
                            orders.get(nameAndID.get(name)));
                    Components.populateSummaryTable(table, data);
                    cb.setValue("");
                    return;
                }
            }
            header.setText("Customer not found.");
            table.setVisible(false);
        });
    }

    /**
     * Adds listeners to toggle group and combo boxes.
     */
    private void addListeners() {
        tg.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == radioChart) {
                updateTotalChart();
            } else {
                updateTotalTable();
            }
        });
        parentChoice.setOnAction(this::comboBoxListener);
        childChoice.setOnAction(this::comboBoxListener);
    }

    /**
     * Listener method for combo box value change, dependant on togglegroup.
     *
     * @param event the action event
     */
    private void comboBoxListener(ActionEvent event) {
        grid.getChildren().removeAll(chart, table);
        if (tg.getSelectedToggle() == radioChart) {
            updateTotalChart();
        } else {
            updateTotalTable();
        }
    }

    /**
     * Updates the total chart based on user input.
     */
    @SuppressWarnings("unchecked")
    private void updateTotalChart() {
        grid.getChildren().removeAll(chart, table, descBox);
        chart = (LineChart<Number, Number>) decipherInputsForMap(
            parentChoice.getValue(), childChoice.getValue(), orders, "Chart");
        grid.add(descBox, 0, 1);
        grid.add(chart, 0, 3);
        GridPane.setHgrow(chart, Priority.ALWAYS);
        GridPane.setVgrow(chart, Priority.ALWAYS);
    }

    /**
     * Updates the total table based on user input.
     */
    @SuppressWarnings("unchecked")
    private void updateTotalTable() {
        grid.getChildren().removeAll(chart, table, descBox);
        table = (TableView<Map.Entry<String, ? extends Number>>) 
            decipherInputsForMap(parentChoice.getValue(),
            childChoice.getValue(), orders,"Table");
        grid.add(table, 0, 3);
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);
    }
    

    /**
     * Generates a map of data based on the parent and child categories, and
     * then uses this data to create a LineChart.
     * 
     * @param parent Category ("Customers" or "Sales")
     * @param child  Category ("City", "State", "Region", "Segment", "Year")
     * @param orders List of orders to generate the data from
     * @return A LineChart displaying data for given parent and child categories
     */
    public static Node decipherInputsForMap(String parent, String child,
                                ArrayList<Order> orders, String node) {
        final Map<String, ? extends Number> dataMap;
        switch (child) {
            case "City":
                dataMap = (parent.equals("Customers")) 
                    ? generateCustomersMap(orders, order ->
                        order.location().city()) 
                    : generateSalesMap(orders, order ->
                        order.location().city());
                break;
            case "State":
                dataMap = (parent.equals("Customers")) 
                    ? generateCustomersMap(orders, order ->
                        order.location().state()) 
                    : generateSalesMap(orders, order ->
                        order.location().state());
                break;
            case "Region":
                dataMap = (parent.equals("Customers")) 
                    ? generateCustomersMap(orders, order ->
                        order.location().region()) 
                    : generateSalesMap(orders, order ->
                        order.location().region());
                break;
            case "Segment":
                dataMap = (parent.equals("Customers")) 
                    ? generateCustomersMap(orders,order ->
                        order.customer().segment())
                    : generateSalesMap(orders, order ->
                        order.customer().segment());
                break;
            case "Year":
                dataMap = (parent.equals("Customers")) 
                    ? generateCustomersMap(orders, order ->
                        order.shipOrder().orderDate().substring(
                            order.shipOrder().orderDate().length() - 4))
                    : generateSalesMap(orders, order ->
                        order.shipOrder().orderDate().substring(
                            order.shipOrder().orderDate().length() - 4));
                break;
            default:
                dataMap = new HashMap<>(); ////////////////////////HANDLE BETTER
                break;
        }
        if(node.equalsIgnoreCase("chart")) {
            return Components.createChart(dataMap, parent, child);
        }
        else if(node.equalsIgnoreCase("table")) {
            return Components.createTotalsTable(dataMap, parent, child);
        }
        return null;
    }

    /**
     * Generates a map of sales data grouped by a specific function.
     * 
     * @param orders list of orders to generate sales data from
     * @param groupingFunction function to group orders by
     * @return map of sales data
     */
    public static Map<String, Long> generateSalesMap(
        ArrayList<Order> orders,Function<Order, String> groupingFunction) {
        return orders.stream().collect(Collectors.groupingBy(
                            groupingFunction, 
                            Collectors.summingLong(Order::sales)));
    }

    /**
     * Generates a map of customers and their count.
     * 
     * @param orders list of orders to generate customer count from
     * @param groupingFunction function to group orders by
     * @return map of customers and their count
     */
    public static Map<String, Integer> generateCustomersMap(ArrayList<Order>
                            orders, Function<Order, String> groupingFunction) {
        return orders.stream()
                     .collect(Collectors.groupingBy(groupingFunction,
                        Collectors.mapping(order -> 
                            order.customer().name(), /////////////////////////////change back to customerID?
                            Collectors.summingInt(x -> 1))));
    }
}
