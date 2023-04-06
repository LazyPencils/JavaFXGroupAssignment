import java.awt.BorderLayout;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BookDatabase extends Application {

    private TableView<Book> table = new TableView<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Book Database");

        // Button bar
        ButtonBar buttonBar = new ButtonBar();
        
        // Buttons for bar
        Button newBook = new Button("New Book");
        Button deleteBook = new Button("Delete Book");
        Button openBook = new Button("Open Book");
        Button editBook = new Button("Edit Book");
        
        buttonBar.getButtons().addAll(newBook, openBook, deleteBook, editBook);
        ButtonBar.setButtonData(newBook, ButtonData.LEFT);
        ButtonBar.setButtonData(deleteBook, ButtonData.LEFT);
        ButtonBar.setButtonData(openBook, ButtonData.LEFT);
        ButtonBar.setButtonData(editBook, ButtonData.LEFT);

        // Search bar
        Label searchLabel = new Label("Search:");
        TextField searchField = new TextField();
        Button searchButton = new Button("Search");
        HBox searchBox = new HBox(searchLabel, searchField, searchButton);
        searchBox.setSpacing(10);
        searchBox.setPadding(new Insets(10, 10, 10, 10));

        // Table
        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().authorProperty());

        TableColumn<Book, Integer> yearColumn = new TableColumn<>("Year");
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearProperty().asObject());

        TableColumn<Book, String> genreColumn = new TableColumn<>("Genres");
        genreColumn .setCellValueFactory(cellData -> cellData.getValue().genreProperty());

        table.getColumns().addAll(titleColumn, authorColumn, yearColumn, genreColumn);

        // Layout
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(buttonBar);
        borderPane.setCenter(table);
        borderPane.setBottom(searchBox);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}


