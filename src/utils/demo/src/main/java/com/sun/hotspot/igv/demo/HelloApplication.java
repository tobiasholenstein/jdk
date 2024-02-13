package com.sun.hotspot.igv.demo;
// HelloApplication

import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.util.Random;

public class HelloApplication extends Application {

    ZoomableScrollPane zoomablePane;
    Pane contentPane;

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(); // The root container

        // Create buttons for the toolbar
        Button zoomInButton = new Button("Zoom In");
        Button zoomOutButton = new Button("Zoom Out");
        Button zoomResetButton = new Button("Reset to 100%");


        // Create a toolbar and add the buttons
        ToolBar toolBar = new ToolBar(zoomInButton, zoomOutButton, zoomResetButton);

        contentPane = new Pane();
        zoomablePane = new ZoomableScrollPane(contentPane);

        // Example: Attaching a zoom in and zoom out functionality to buttons
        zoomInButton.setOnAction(e -> zoomablePane.zoomIn());
        zoomOutButton.setOnAction(e -> zoomablePane.zoomOut());
        zoomResetButton.setOnAction(e -> zoomablePane.zoomToActual());

        // Add some example nodes
        addExampleNodes();

        // Add the toolbar and scroll pane to the root container
        root.getChildren().addAll(toolBar, zoomablePane);

        Scene scene = new Scene(root, 800, 600);

        // Bind the zoomablePane's size to the scene's size
        zoomablePane.prefWidthProperty().bind(scene.widthProperty());
        zoomablePane.prefHeightProperty().bind(scene.heightProperty().subtract(toolBar.heightProperty()));

        primaryStage.setTitle("Graph Visualizer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addExampleNodes() {
        Random rand = new Random();
        for (int i = 0; i < 20; i++) { // Add 20 random nodes for example
            double radius = 5 + rand.nextDouble() * 15; // Random radius between 5 and 20
            Circle node = new Circle(radius, Color.BLUE);
            node.setCenterX(rand.nextDouble() * 800); // Random position within a reasonable range
            node.setCenterY(rand.nextDouble() * 600);
            addNode(node);
            makeNodeDraggable(node);
        }
    }

    public void addNode(Node node) {
        contentPane.getChildren().add(node);
        node.setOnMouseDragged(e -> {
            // Update node position based on drag
            double offsetX = e.getX() - node.getBoundsInParent().getWidth() / 2;
            double offsetY = e.getY() - node.getBoundsInParent().getHeight() / 2;
            node.setLayoutX(node.getLayoutX() + offsetX);
            node.setLayoutY(node.getLayoutY() + offsetY);
        });
    }

    private void makeDraggable(Node node) {
        node.setOnMousePressed(e -> {
            // Record the current mouse position on the node
            double mouseX = e.getSceneX();
            double mouseY = e.getSceneY();
            double nodeX = ((Circle) node).getCenterX();
            double nodeY = ((Circle) node).getCenterY();
            node.setUserData(new double[]{mouseX, mouseY, nodeX, nodeY});
        });

        node.setOnMouseDragged(e -> {
            double[] userData = (double[]) node.getUserData();
            double deltaX = e.getSceneX() - userData[0];
            double deltaY = e.getSceneY() - userData[1];
            // Adjust for the current zoom level
            double scale = zoomablePane.getScaleValue(); // Assuming zoomablePane is accessible; adjust as necessary
            ((Circle) node).setCenterX(userData[2] + deltaX / scale);
            ((Circle) node).setCenterY(userData[3] + deltaY / scale);
        });
    }

    private void makeNodeDraggable(Node node) {
        final Delta dragDelta = new Delta();

        node.setOnMousePressed(mouseEvent -> {
            dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
            dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
            node.setCursor(Cursor.MOVE);
            zoomablePane.setDragging(true); // Indicate dragging starts
        });

        node.setOnMouseDragged(mouseEvent -> {
            // Adjust node position considering the scale factor
            double scaleFactor = 1 / zoomablePane.getScaleValue(); // Inverse of current scale
            node.setLayoutX(mouseEvent.getSceneX() * scaleFactor + dragDelta.x * scaleFactor);
            node.setLayoutY(mouseEvent.getSceneY() * scaleFactor + dragDelta.y * scaleFactor);
        });

        node.setOnMouseReleased(mouseEvent -> {
            node.setCursor(Cursor.HAND);
            zoomablePane.setDragging(false); // Indicate dragging ends
        });
    }

    private static class Delta { double x, y; }


    public static void main(String[] args) {
        launch(args);
    }
}


