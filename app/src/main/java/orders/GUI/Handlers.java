package orders.GUI;

import java.util.Locale;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import orders.Order;


public class Handlers {
    private final static NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        public static <T extends Number> void searchButtonClick(Map<String, T> map, Button btn, ComboBox<String> cb, Label output, String item) {
            btn.setOnAction(e-> {
                String input = cb.getEditor().getText();
                if (input == null || input.isEmpty()) {
                    output.setText("Empty search bar.");
                }   else if(map.containsKey(input)){
                    output.setText(item + " in " + input + ": " + numberFormat.format(map.get(input)));
                    System.out.println(item + " in " + input + ": " + numberFormat.format(map.get(input)));
                } else {
                    System.out.println(item + " not found in \"" + input + "\".");
                    output.setText(item + " not found in \"" + input + "\".");
                }
            /////////////////////////////////////////////////////////////CHECK IF INPUT IS WRONG TYPE
            });
        }

        public static void averageButtonClick(Button btn, ArrayList<Order> orders, Label output) {
            btn.setOnAction(e -> {
                long totalSales = orders.stream().mapToLong(Order::sales).sum();
                double averageSales = (double) totalSales / orders.size();
                System.out.println("Total Average Sales: " + numberFormat.format(averageSales));
                output.setText(numberFormat.format(averageSales));
            });
        }

        public static void orderSummaryTableFromName(Button btn, ComboBox<String> cb, Label header, TableView<Order> table, Map<String, String> identifiers, Map<String, Set<Order>> orders) {
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
                        ObservableList<Order> data = FXCollections.observableArrayList(orders.get(identifiers.get(key)));
                        Components.populateTable(table, data);
                        return;
                    }
                }
                header.setText("Customer not found.");
                table.setVisible(false);
            });
        }
}
