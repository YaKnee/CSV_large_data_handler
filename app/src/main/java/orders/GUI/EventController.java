package orders.GUI;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
// import javafx.scene.chart.AreaChart;
// import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import orders.App;
import orders.OrderObjects.Order;


public class EventController {

    private final static NumberFormat numberFormat =
        NumberFormat.getNumberInstance(Locale.US);


    public static <T extends Number> void searchButton(Map<String, T> map,
                Button btn, ComboBox<String> cb, Label output, String item, String criteria) {
        btn.setOnAction(e-> {
            String input = cb.getEditor().getText();
            if (input == null || input.isEmpty()) {
                output.setText("Empty search bar.");
            }  else {
                boolean found = false;
                for (String key : map.keySet()) {
                    if (key.equalsIgnoreCase(input)) {
                        output.setText(item + " in " + key + ": " +
                                numberFormat.format(map.get(key)));
                        System.out.println(item + " in " + key + ": " +
                                numberFormat.format(map.get(key)));
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    System.out.println(item + " not found in " + criteria + ": \"" + input + "\".");
                    output.setText(item + " not found in " + criteria + ": \"" + input + "\".");
                }
            }
        /////////////////////////////////////////////////////////////CHECK IF INPUT IS WRONG TYPE
        });
    }
    
    // public static void averageButton(Button btn, ArrayList<Order> orders,
    //                                                             Label output) {
    //     btn.setOnAction(e -> {
    //         long totalSales = orders.stream().mapToLong(Order::sales).sum();
    //         double averageSales = (double) totalSales / orders.size();
    //         System.out.println("Total Average Sales: " +
    //             numberFormat.format(averageSales));
    //         output.setText(numberFormat.format(averageSales));
    //     });
    // }

    public static void orderSummaryTableFromName(Button btn, 
            ComboBox<String> cb, Label header, TableView<Order> table, 
            Map<String, String> identifiers, Map<String, Set<Order>> orders) {
        btn.setOnAction(e-> {
            String name = cb.getEditor().getText();
            if (name == null || name.isEmpty()) {
                header.setText("Empty search bar.");
                table.setVisible(false);
                return;
            }
            for (String key : identifiers.keySet()) {
                if (key.equalsIgnoreCase(name)) {
                    header.setText(key);
                    ObservableList<Order> data =
                    FXCollections.observableArrayList(
                            orders.get(identifiers.get(key)));
                    Components.populateTable(table, data);
                    return;
                }
            }
            header.setText("Customer not found.");
            table.setVisible(false);
        });
    }

    public static LineChart<String, Number> updateChart(String parent, String child, ArrayList<Order> orders) {
        Map<String, ? extends Number> dataMap;
        switch (child) {
            case "City":
                dataMap = (parent.equals("Customers")) 
                    ? App.generateCustomersMap(orders, order -> order.location().city()) 
                    : App.generateSalesMap(orders, order -> order.location().city());
                break;
            case "State":
                dataMap = (parent.equals("Customers")) 
                    ? App.generateCustomersMap(orders, order -> order.location().state()) 
                    : App.generateSalesMap(orders, order -> order.location().state());
                break;
            case "Region":
                dataMap = (parent.equals("Customers")) 
                    ? App.generateCustomersMap(orders, order -> order.location().region()) 
                    : App.generateSalesMap(orders, order -> order.location().region());
                break;
            case "Segment":
                dataMap = (parent.equals("Customers")) 
                    ? App.generateCustomersMap(orders, order -> order.customer().segment()) 
                    : App.generateSalesMap(orders, order -> order.customer().segment());
                break;
            case "Year":
                dataMap = (parent.equals("Customers")) 
                    ? App.generateCustomersMap(orders, order -> order.shipOrder().orderDate().substring(order.shipOrder().orderDate().length() - 4)) 
                    : App.generateSalesMap(orders, order -> order.shipOrder().orderDate().substring(order.shipOrder().orderDate().length() - 4));
                break;
            default:
                dataMap = new HashMap<>();
                break;
        }
        
        return Components.createChart(dataMap, parent, child);
    }

    public static void handleChartZoom(LineChart<String, Number> chart) {
        final double scalingFactor = 1.1;
        chart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                double scaleFactor = (event.getButton() == MouseButton.PRIMARY) ? scalingFactor : 1 / scalingFactor;
                chart.setScaleX(chart.getScaleX() * scaleFactor);
                chart.setScaleY(chart.getScaleY() * scaleFactor);
                for (XYChart.Series<String, Number> series : chart.getData()) {
                    series.getNode().setStyle("-fx-stroke-width:" + scaleFactor + "px");
                    for (XYChart.Data<String, Number> dataPoint : series.getData()) {
                        dataPoint.getNode().setScaleX(1 / chart.getScaleX());
                        dataPoint.getNode().setScaleY(1 / chart.getScaleY());
                    }
                }
                event.consume();
            }
        });
    }
}
