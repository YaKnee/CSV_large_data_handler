package orders.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import orders.OrderObjects.Order;

/**
 * Handles events for GUI components.
 */
public class EventController {

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

    public static void totalComboChoice(ComboBox<String> parent,
    ComboBox<String> child, GridPane grid,
    LineChart<String, Number>[] chartWrapper, ArrayList<Order> orders){
        parent.setOnAction(e -> {
            grid.getChildren().remove(chartWrapper[0]);
            chartWrapper[0] = EventController.createChartForCategory(
                parent.getValue(), child.getValue(), orders);
            grid.add(chartWrapper[0], 0, 4, 2, 2);
        }); 
        child.setOnAction(e -> {
            grid.getChildren().remove(chartWrapper[0]);
            chartWrapper[0] = EventController.createChartForCategory(
                parent.getValue(), child.getValue(), orders);
            grid.add(chartWrapper[0], 0, 4, 2, 2);
        }); 
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
    public static LineChart<String, Number>
        createChartForCategory(String parent, String child,
                                ArrayList<Order> orders) {
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
                dataMap = new HashMap<>();
                break;
        }
        return Components.createChart(dataMap, parent, child);
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
