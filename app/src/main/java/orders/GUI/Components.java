package orders.GUI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Function;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import orders.OrderObjects.Order;

import java.util.Locale;
import java.text.NumberFormat;


public class Components {

    private final static NumberFormat numberFormat =
    NumberFormat.getNumberInstance(Locale.US);

    public static void autoFillComboBox(ComboBox<String> cb, Set<String> list) {
        TreeSet<String> sortedList = new TreeSet<>();
        sortedList.addAll(list);
        ObservableList<String> items = FXCollections.observableArrayList(sortedList);
                                                                            //visible 
        FilteredList<String> filteredItems = new FilteredList<String>(items, v -> true);

        cb.getEditor().textProperty().addListener((obs, originalVal, updatedVal) -> {
            final TextField search = cb.getEditor();
            final String selected = cb.getSelectionModel().getSelectedItem();

            Platform.runLater(() -> {
                if (selected == null || !selected.equals(search.getText())) {
                    filteredItems.setPredicate(item -> {
                        if (item.toUpperCase().startsWith(updatedVal.toUpperCase())) {
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    static void populateTable(TableView<Order> table, ObservableList<Order> data){
        //use ___Col.setVisible(false/true) for checkboxs
        table.getItems().clear();
        table.getColumns().clear();
        TableColumn<Order, String> countCol = createColumn("Row ID", Order::rowID);

        TableColumn<Order, String> dateCol = new TableColumn("Order & Shipping");
        TableColumn<Order, String> orderIDCol = createColumn("Order ID", order -> order.shipOrder().orderId());
        TableColumn<Order, String> orderDateCol = createColumn("Order Date",  order -> order.shipOrder().orderDate());
        TableColumn<Order, String> shipDateCol = createColumn("Shipping Date",  order -> order.shipOrder().shipDate());
        TableColumn<Order, String> shippingModeCol = createColumn("Shipping Mode",  order -> order.shipOrder().shipMode());


        TableColumn<Order, String> customerCol = new TableColumn("Customer");
        TableColumn<Order, String> custNameCol = createColumn("Name",  order -> order.customer().name());
        TableColumn<Order, String> custIdCol = createColumn("ID",  order -> order.customer().customerId());
        TableColumn<Order, String> segmentCol = createColumn("Segment", order -> order.customer().segment());

        TableColumn<Order, String> locationCol = new TableColumn("Location");
        TableColumn<Order, String> cityCol = createColumn("City", order -> order.location().city());
        TableColumn<Order, String> stateCol = createColumn("State", order -> order.location().state());
        TableColumn<Order, String> regionCol = createColumn("Region",order -> order.location().region());

        TableColumn<Order, String> productsCol = new TableColumn("Products");
        TableColumn<Order, String> productIDCol = createColumn("ID", order -> order.product().id());
        TableColumn<Order, String> productSubCatCol = createColumn("Sub-Category", order -> order.product().subCategory());

        TableColumn<Order, String> logisticsCol = new TableColumn("Logistics");
        TableColumn<Order, String> salesCol = createColumn("Sales", Order::sales);
        TableColumn<Order, String> quantityCol = createColumn("Quantity", Order::quantity);
        TableColumn<Order, String> discountCol = createColumn("discount", Order::discount);
        TableColumn<Order, String> profitCol = createColumn("Profit", Order::profit);

        table.setItems(data);

        dateCol.getColumns().addAll(orderIDCol, orderDateCol, shipDateCol, shippingModeCol);
        customerCol.getColumns().addAll(custNameCol, custIdCol, segmentCol);
        locationCol.getColumns().addAll(cityCol, stateCol, regionCol);
        productsCol.getColumns().addAll(productIDCol, productSubCatCol);
        logisticsCol.getColumns().addAll(salesCol,quantityCol,discountCol,profitCol);

        table.getColumns().addAll(countCol, dateCol, customerCol, locationCol, productsCol, logisticsCol);
        for(TableColumn col : table.getColumns()){
            col.setResizable(false);
        }
        table.setEditable(false);
        table.setVisible(true);
    }

    private static <S, T> TableColumn<S, String> createColumn(String title, Function<S, T> property) {
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
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");
                    LocalDate date1 = LocalDate.parse(s1, formatter);
                    LocalDate date2 = LocalDate.parse(s2, formatter);
                    return date1.compareTo(date2);
                } catch (DateTimeParseException err) {
                    return s1.compareTo(s2);
                }
            }
        });

        // column.setResizable(false);
        return column;
    }

    @SuppressWarnings("unchecked")
    public static LineChart<String, Number> createChart(Map<String, ? extends Number> map, String parent, String child) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel(child);
        yAxis.setLabel(parent);

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
                dataPoint.getXValue().toString() + ": " + numberFormat.format(dataPoint.getYValue())));
        }
        for (XYChart.Data<String, Number> dataPoint : series2.getData()) {
            Tooltip.install(dataPoint.getNode(), new Tooltip(
                "Average: " + numberFormat.format(average)));
            dataPoint.getNode().setStyle("-fx-background-color: transparent;");
        }
        //tooltips for whole line except points so can access series1 when they are close
        Tooltip.install(series2.getNode(), new Tooltip("Average: " + numberFormat.format(average)));
        
        chart.setLegendVisible(true);
        EventController.handleChartZoom(chart);

        return chart;
    }
}
