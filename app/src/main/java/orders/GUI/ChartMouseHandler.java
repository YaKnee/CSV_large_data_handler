package orders.GUI;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.MouseEvent;

/**
 * Handles mouse events for a line chart.
 */
public class ChartMouseHandler {

    private final LineChart<Number, Number> chart;
    private final NumberAxis xAxis;
    private final NumberAxis yAxis;
    private double lastMouseX;
    private double lastMouseY;

    /**
     * Creates a new ChartMouseHandler for the given chart.
     *
     * @param chart the line chart to handle mouse events for
     */
    public ChartMouseHandler(LineChart<Number, Number> chart) {
        this.chart = chart;
        this.yAxis = (NumberAxis) chart.getYAxis();
        this.xAxis = (NumberAxis) chart.getXAxis();
        chart.setOnScroll(this::handleScroll);
        chart.setOnMousePressed(this::handleMousePressed);
        chart.setOnMouseDragged(this::handleMouseDragged);
    }

    /**
     * Handles a scroll event on the chart.
     * 
     * @param event The scroll event.
     */
    private void handleScroll(ScrollEvent event) {
        double mousePosX = event.getX();
        double mousePosY = event.getY();

        //Current axes lengths
        double xAxisWidth = xAxis.getUpperBound() - xAxis.getLowerBound();
        double yAxisHeight = yAxis.getUpperBound() - yAxis.getLowerBound();
        
        double xScale = chart.getWidth() / xAxisWidth;
        double yScale = chart.getHeight() / yAxisHeight;

        //Calculate offset of mouse position from lower bound of axes
        double xOffset = xAxis.getLowerBound() + mousePosX / xScale;
        double yOffset = yAxis.getUpperBound() - mousePosY / yScale;

        if (event.getDeltaY() > 0) { //Scroll-down, zoom out 10%
            adjustBounds(xOffset, yOffset, 0.9);
        } else if (event.getDeltaY() < 0) { //Scroll-up, zoom in 10%
            adjustBounds(xOffset, yOffset, 1.1);
        }
    }

    /**
     * Adjusts the bounds of chart based on the given offset and zoom factor.
     * 
     * @param xOffset The x-offset to adjust the bounds at.
     * @param yOffset The y-offset to adjust the bounds at.
     * @param zoomFactor The zoom factor to apply.
     */
    private void adjustBounds(double xOffset, double yOffset,
    double zoomFactor) {
        //Current axes lengths
        double xAxisWidth = xAxis.getUpperBound() - xAxis.getLowerBound();
        double yAxisHeight = yAxis.getUpperBound() - yAxis.getLowerBound();

        double newWidth = xAxisWidth * zoomFactor;
        double newHeight = yAxisHeight * zoomFactor;

        //Calculate new lower and upper bounds of both axes from offset, current
        //bounds, and new width/height
        double newLowerX = xOffset -
            (xOffset - xAxis.getLowerBound()) * (newWidth / xAxisWidth);
        double newUpperX = newLowerX + newWidth;
        double newLowerY = yOffset -
            (yOffset - yAxis.getLowerBound()) * (newHeight / yAxisHeight);
        double newUpperY = newLowerY + newHeight;

        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);

        //Set new bounds
        xAxis.setLowerBound(newLowerX);
        xAxis.setUpperBound(newUpperX);
        yAxis.setLowerBound(newLowerY);
        yAxis.setUpperBound(newUpperY);
    }

    /**
     * Handles a mouse press event on the chart.
     * 
     * @param event The mouse press event.
     */
    private void handleMousePressed(MouseEvent event) {
        lastMouseX = event.getX();
        lastMouseY = event.getY();
    }

    /**
     * Handles a mouse dragged event on the chart, adjusting the axis bounds
     * accordingly.
     * 
     * @param event The mouse event.
     */
    private void handleMouseDragged(MouseEvent event) {
        //Calculate x/y distance since last drag event
        double deltaX = event.getX() - lastMouseX;
        double deltaY = event.getY() - lastMouseY;

        //Current axes lengths
        double xAxisWidth = xAxis.getUpperBound() - xAxis.getLowerBound();
        double yAxisHeight = yAxis.getUpperBound() - yAxis.getLowerBound();

        //Calculate scale factors based on chart-size
        double xScale = chart.getWidth() / xAxisWidth;
        double yScale = chart.getHeight() / yAxisHeight;

        //Scale mouse movement to axes coordinates
        double deltaXAxis = deltaX / xScale;
        double deltaYAxis = deltaY / yScale;

        xAxis.setAutoRanging(false);
        yAxis.setAutoRanging(false);

        //Set new bounds from scaled mouse movement
        xAxis.setLowerBound(xAxis.getLowerBound() - deltaXAxis);
        xAxis.setUpperBound(xAxis.getUpperBound() - deltaXAxis);
        yAxis.setLowerBound(yAxis.getLowerBound() + deltaYAxis);
        yAxis.setUpperBound(yAxis.getUpperBound() + deltaYAxis);

        lastMouseX = event.getX();
        lastMouseY = event.getY();
    }
}