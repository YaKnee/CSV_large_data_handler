package orders.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.Scene;
import javafx.stage.Stage;

import orders.CustomerPerformance;
import orders.CSVHandler.CSVDataReader;
import orders.OrderObjects.Order;


/**
 * Responsible for creating and managing different scenes in the application.
 */
public class Scenes {
    private Stage stage;
    private double stageWidth;
    private double stageHeight;
    private double stageX;
    private double stageY;
    private Scene welcomeScene;
    private LineChart<Number, Number> chart;
    private TableView<Map.Entry<String, ? extends Number>> totalsTable;

    /**
     * Creates a new Scenes instance with the provided Stage.
     * 
     * @param stage the Stage to use for the scenes
     */
    public Scenes(final Stage stage) {
        this.stage = stage;
        this.welcomeScene = createWelcomeScene();
    }

    /**
     * Creates and returns the welcome scene for the application.
     * The welcome scene includes a grid with a file selection ComboBox,
     * a select button, and an exit button. When the select button is clicked,
     * it loads the selected file and creates a new scene based on the file's
     * contents.
     * 
     * @return the created welcome scene
     */
    public Scene createWelcomeScene() {
        Hyperlink git = Components.createGitLink();
        HBox topBox = new HBox(git);
        topBox.setAlignment(Pos.TOP_RIGHT);

        Text welcome = new Text("Welcome!");
        welcome.setStyle("-fx-font: 24 arial;");
        Image image = new Image("files.png");
        ImageView imgView = new ImageView(image);
        imgView.setFitWidth(100);
        imgView.setFitHeight(100);
        Text select = 
            new Text("Please select the data file that you wish to view.");
        String[] files = {"SuperStoreOrders.csv", "SuperStoreReturns.csv"};
        ComboBox<String> fileBox =
            new ComboBox<>(FXCollections.observableArrayList(files));
        Label errorLbl = new Label();
        errorLbl.setStyle("-fx-text-fill: red");
        errorLbl.setVisible(false);
        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.add(welcome, 1, 2);
        grid.add(imgView, 3, 2);
        grid.add(select, 1, 5);
        grid.add(fileBox, 3, 5);
        grid.add(errorLbl, 3, 6);
        grid.getStyleClass().add("grid");
        // resizeElementBasedOnSceneWidth
        Button selectFileButton = new Button("Select");
        selectFileButton.setOnAction(e->{
            if (fileBox.getSelectionModel().isEmpty()) {
                errorLbl.setText("Please select a file.");
                errorLbl.setVisible(true);
            }else if (fileBox.getValue().toString().equals(
                                                "SuperStoreOrders.csv")) {
                errorLbl.setVisible(false);
                ArrayList<Order> orders = CSVDataReader.createOrders(
                    "data/SuperStoreOrders.csv", errorLbl);
                storeStageProperties();
                setStageProperties();
                createMenuScene(orders);
            } else if (fileBox.getValue().toString().equals(
                                                "SuperStoreReturns.csv")) {
                errorLbl.setVisible(false);
                ArrayList<Order> returns =
                    CSVDataReader.createReturns(
                        "data/SuperStoreReturns.csv", errorLbl);
                storeStageProperties();
                setStageProperties();
                createReturnsScene(returns);
            } else {
                errorLbl.setText("I don't even know how you got here...");
                errorLbl.setVisible(true);
            }
        });
        Button exitBtn = new Button("Exit");
        exitBtn.setOnAction(e -> System.exit(0));
        HBox bottomBox = new HBox(selectFileButton, exitBtn);
        bottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        bottomBox.setSpacing(10);

        BorderPane root = new BorderPane();
        root.setTop(topBox);
        root.setCenter(grid);
        root.setBottom(bottomBox);
        root.getStylesheets().add("stylesheet.css");

        welcomeScene = new Scene(root);
        storeStageProperties();
        return welcomeScene;
    }

    /**
     * Creates a menu scene with options to view overall customer performance,
     * total sales or customers per property, and orders summary for
     * selected customers.
     * 
     * @param orders the list of orders to use for the menu options
     */
    public void createMenuScene(ArrayList<Order> orders) {
        Text custText = new Text("Overall performance of customers");
        custText.getStyleClass().add("menu-text");
        Button custBtn = new Button("Performances");
        custBtn.getStyleClass().add("menu-button");
        custBtn.setOnAction(e -> createPerformanceScene(orders));
        VBox custBox = new VBox(custText, custBtn);
        custBox.setAlignment(Pos.CENTER);

        Text totalText = new Text("Total sales or customers per property");
        totalText.getStyleClass().add("menu-text");
        Button totalBtn = new Button("Totals");
        totalBtn.getStyleClass().add("menu-button");
        totalBtn.setOnAction(e -> createTotalsScene(orders));
        VBox totalBox = new VBox(totalText, totalBtn);
        totalBox.setAlignment(Pos.CENTER);

        Text orderText = new Text("Orders summary for specific customer");
        orderText.getStyleClass().add("menu-text");
        Button orderBtn = new Button("Summary");
        orderBtn.getStyleClass().add("menu-button");
        orderBtn.setOnAction(e -> createCustomerSummaryScene(orders));
        VBox orderBox = new VBox(orderText, orderBtn);
        orderBox.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");
        grid.add(custBox, 0, 0);
        grid.add(totalBox, 0, 1);
        grid.add(orderBox, 0, 2);
        grid.setVgap(20);

        BorderPane root = new BorderPane();
        backToWelcomeAndGit(root);
        root.setCenter(grid);
        root.getStylesheets().add("stylesheet.css");
        Scene menuScene = new Scene(root);
        stage.setScene(menuScene);
        storeStageProperties();
    }

    /**
     * Creates a scene for displaying returned orders.
     * 
     * @param returns the list of returned orders to display
     */
    public void createReturnsScene(ArrayList<Order> returns) {
        Text orderText = new Text("Returned Orders: ");
        int uniqueOrderIDs = 
        (int) returns.stream().map(order -> order.shipOrder().orderId())
                              .distinct()
                              .count();
        Text orderSize = new Text(String.valueOf(uniqueOrderIDs));
        Text productText = new Text("Returned Products: ");
        Text productSize = new Text(String.valueOf(returns.size()));
        VBox textBox = new VBox(orderText, productText);
        textBox.setStyle("-fx-font-size: 25px");
        VBox numberBox = new VBox(orderSize, productSize);
        numberBox.setStyle("-fx-font-size: 25px; -fx-font-weight: bold");
        HBox returnsBox = new HBox(textBox, numberBox);

        TableView<Order> table = new TableView<>();
        ObservableList<Order> data = FXCollections.observableArrayList(returns);
        Components.populateSummaryTable(table, data);

        GridPane grid = new GridPane();
        grid.getStyleClass().add("grid");
        grid.add(returnsBox, 0, 0);
        grid.add(table, 0, 1);
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        backToWelcomeAndGit(root);
        root.setCenter(grid);
        root.getStylesheets().add("stylesheet.css");

        Scene returnScene = new Scene(root);
        stage.setScene(returnScene);
        storeStageProperties();
    }

    /**
     * Creates a scene to display customer performance data in a table.
     * 
     * @param orders the list of orders to display
     */
    public void createPerformanceScene(ArrayList<Order> orders) {
        Text descText = new Text("Click a column header to sort the table"
        + " respective to that property.\nClick again to reverse the order.");
        TableView<CustomerPerformance> performanceTable =
                                        Components.performanceTable(orders);

        GridPane grid = new GridPane();
        grid.add(descText, 1, 3);
        grid.add(performanceTable, 1, 5);
        grid.getStyleClass().add("grid");
        GridPane.setHgrow(performanceTable, Priority.ALWAYS);
        GridPane.setVgrow(performanceTable, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        backToMenuAndGit(orders, root);
        root.setCenter(grid);

        root.getStylesheets().add("stylesheet.css");
        Scene performScene = new Scene(root);
        stage.setScene(performScene);
        storeStageProperties();
    }

    /**
     * Creates a scene to display average data for customers or sales, with
     * options to select category and sub-category.
     * 
     * @param orders the list of orders to display
     */
    @SuppressWarnings("unchecked")
    public void createTotalsScene(ArrayList<Order> orders) {
        ToggleGroup tg = new ToggleGroup();
        RadioButton radioTable = new RadioButton("Table");
        RadioButton radioChart = new RadioButton("Chart");
        radioTable.setToggleGroup(tg);
        radioChart.setToggleGroup(tg);
        radioChart.setSelected(true);
        VBox toggleBox = new VBox(radioChart, radioTable);
        toggleBox.setStyle("-fx-alignment: CENTER; -fx-spacing: 20px;");

        Text pText = new Text("Main Category: ");
        String[] parentTxt = {"Customers", "Sales"};
        ComboBox<String> parentChoice =
                new ComboBox<>(FXCollections.observableArrayList(parentTxt));
        parentChoice.getSelectionModel().selectFirst();
        parentChoice.setPrefWidth(100);
        HBox parentBox = new HBox(pText, parentChoice);
        parentBox.getStyleClass().add("hbox");

        Text cText = new Text("Sub-Category: ");
        String[] childTxt = {"City", "State", "Region", "Segment", "Year"};
        ComboBox<String> childChoice =
                new ComboBox<>(FXCollections.observableArrayList(childTxt));
        childChoice.getSelectionModel().select(1);
        childChoice.setPrefWidth(100);
        HBox childBox = new HBox(cText, childChoice);
        childBox.getStyleClass().add("hbox");
        childBox.setStyle("-fx-spacing: 15px;");

        Text descText = new Text(
            "Hover over data points to see their exact values."
            + "\nMousewheel can be used for zooming functions."
            + "\nPan around chart with any mouse button.");
        descText.setStyle("-fx-font-style: italic");
        HBox descBox = new HBox(descText);
        descBox.getStyleClass().add("hbox");

        VBox choiceBox = new VBox(parentBox, childBox);
        choiceBox.getStyleClass().add("vbox");

        HBox selectorsBox = new HBox(choiceBox, toggleBox);
        selectorsBox.getStyleClass().add("hbox");
        selectorsBox.setStyle("-fx-alignment: CENTER; -fx-spacing: 100px;");


        chart = (LineChart<Number, Number>) EventController
            .decipherInputsForMap("Customers", "State", orders, "Chart");

        GridPane grid = new GridPane();
        grid.add(selectorsBox, 0, 0);
        grid.add(descBox, 0, 1);
        grid.add(chart, 0, 3);
        grid.getStyleClass().add("grid");

        GridPane.setVgrow(chart, Priority.ALWAYS);
        GridPane.setHgrow(chart, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        backToMenuAndGit(orders, root);
        root.setCenter(grid); 
        root.getStylesheets().add("stylesheet.css");

        new EventController(orders, grid, chart, totalsTable, parentChoice,
            childChoice, tg, radioChart, descBox);
        Scene totalScene = new Scene(root);
        stage.setScene(totalScene);
        storeStageProperties();
    }

    /**
     * Creates a scene to display a summary of orders for a selected customer.
     * 
     * @param orders the list of orders to display
     */
    public void createCustomerSummaryScene(ArrayList<Order> orders) {
        Map<String, Set<Order>> customerOrders = new HashMap<>();
        Map<String, String> customerNameAndID = new HashMap<>();
        for (Order order : orders) {
            String customerId = order.customer().customerId();
            String customerName = order.customer().name();
            Set<Order> ordersForCustomer =
                customerOrders.computeIfAbsent(
                    customerId, newCustomerId -> new HashSet<>());
            ordersForCustomer.add(order);
            customerNameAndID.putIfAbsent(customerName, customerId);
        }
        Text descText = new Text("Type in name or select one from dropdown list.");
        TableView<Order> table = new TableView<>();
        table.setVisible(false);
        Label tableHeading = new Label();
        tableHeading.setStyle("-fx-font-size: 25px;");
        Label customerLabel=new Label("Customer Name:"); 
        Button customerSearchBtn = new Button("Search");
        ComboBox<String> nameComboBox = new ComboBox<String>();
        Components.autoFillComboBox(nameComboBox, customerNameAndID.keySet());
        EventController.orderSummaryTableFromName(customerSearchBtn,
            nameComboBox, tableHeading, table, customerNameAndID,
            customerOrders);
        HBox selectorBox =
            new HBox(customerLabel, nameComboBox, customerSearchBtn);
        selectorBox.getStyleClass().add("hbox");
        VBox box = new VBox(selectorBox, tableHeading);
        box.getStyleClass().add("vbox");

        GridPane grid = new GridPane();
        grid.add(descText, 0, 0);
        grid.add(box, 0, 1);
        grid.add(table, 0, 2);
        grid.getStyleClass().add("grid");
        GridPane.setHgrow(table, Priority.ALWAYS);
        GridPane.setVgrow(table, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        backToMenuAndGit(orders, root);
        root.setCenter(grid);
        root.getStylesheets().add("stylesheet.css");
        Scene summaryScene = new Scene(root);
        stage.setScene(summaryScene);
        storeStageProperties();
    }

    /**
     * Creates a "Back" button to return to the menu scene and a Git link,
     * and adds them to the top of the provided BorderPane.
     * 
     * @param orders the list of orders
     * @param bp     the BorderPane to add the components to
     */
    private void backToMenuAndGit(ArrayList<Order> orders, BorderPane bp) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            storeStageProperties();
            setStageProperties();
            createMenuScene(orders);
        });
        Hyperlink git = Components.createGitLink();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBox = new HBox(backBtn, spacer, git); 
        topBox.getStyleClass().add("hbox");
        bp.setTop(topBox);
    }
    
    /**
     * Creates a "Back" button to return to the welcome scene and a Git link,
     * and adds them to the top of the provided BorderPane.
     * 
     * @param bp the BorderPane to add the components to
     */
    private void backToWelcomeAndGit(BorderPane bp) {
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> {
            storeStageProperties();
            setStageProperties();
            stage.setScene(welcomeScene);
        });
        Hyperlink git = Components.createGitLink();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBox = new HBox(backBtn, spacer, git);
        topBox.getStyleClass().add("hbox");
        bp.setTop(topBox);
    }

    /**
     * Stores the properties of the stage such as width, height, X, and
     * Y coordinates.
     * If the scene of the stage is not null, it forces a layout pass to ensure
     * stage properties are updated.
     */
    private void storeStageProperties() {
        if (stage.getScene() != null) {
            // Force layout pass to ensure stage properties are updated
            stage.getScene().getRoot().requestLayout();
        }
        stageWidth = stage.getWidth();
        stageHeight = stage.getHeight();
        stageX = stage.getX();
        stageY = stage.getY();
    }

    /**
     * Sets the properties of the stage (width, height, X, and Y coordinates)
     * using the stored values.
     */
    private void setStageProperties() {
        stage.setWidth(stageWidth);
        stage.setHeight(stageHeight);
        stage.setX(stageX);
        stage.setY(stageY);
    }
}
