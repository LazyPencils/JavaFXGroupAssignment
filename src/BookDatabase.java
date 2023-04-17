import java.awt.BorderLayout;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BookDatabase extends Application {

  ObservableList<Book> books;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Book Database");
    TableView<Book> table = new TableView<>();

    // Button bar
    ButtonBar buttonBar = new ButtonBar();

    // Buttons for bar
    Button newBook = new Button("New Book");
    Button deleteBook = new Button("Delete Book");
    Button openBook = new Button("Open Book");
    Button editBook = new Button("Edit Book");

    // Add actions for buttons 
    // Button Logic by Manh Phu, Zohair and Nabil
    newBook.setOnAction(e -> {
      BookActions.addBook(table);
    });

    deleteBook.setOnAction(e -> {
      BookActions.deleteSelectedBooks(table);
    });

    openBook.setOnAction(e -> {
      BookActions.openBook(table);
  });

    editBook.setOnAction(e -> {
      BookActions.editSelectedBook(table); 
  });
  
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

    // Toggleable select checkboxes column
    TableColumn selectColumn = new TableColumn("Select");

    // This allows the cell to be edited by the user clicking on the select checkboxes displayed in TableView.
    selectColumn.setCellValueFactory(
      new Callback<TableColumn.CellDataFeatures<Book, CheckBox>, ObservableValue<CheckBox>>() {
        @Override
        public ObservableValue<CheckBox> call(
          TableColumn.CellDataFeatures<Book, CheckBox> arg0
        ) {
          Book book = arg0.getValue();
          CheckBox checkBox = new CheckBox();
          checkBox.selectedProperty().setValue(book.getSelected());

          checkBox
            .selectedProperty()
            .addListener(
              new ChangeListener<Boolean>() {
                public void changed(
                  ObservableValue<? extends Boolean> ov,
                  Boolean old_val,
                  Boolean new_val
                ) {
                  book.setSelected(new_val);
                }
              }
            );
          return new SimpleObjectProperty<CheckBox>(checkBox);
        }
      }
    );

    // Column for Book Titles
    TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
    titleColumn.setCellValueFactory(cellData ->
      cellData.getValue().titleProperty()
    );

    // Column for Author Names.
    TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
    authorColumn.setCellValueFactory(cellData ->
      cellData.getValue().authorProperty()
    );

    // Column for the Book's Year of Release
    TableColumn<Book, Integer> yearColumn = new TableColumn<>("Year");
    yearColumn.setCellValueFactory(cellData ->
      cellData.getValue().yearProperty().asObject()
    );

    // Column for the Book's Genre
    TableColumn<Book, String> genreColumn = new TableColumn<>("Genres");
    genreColumn.setCellValueFactory(cellData ->
      cellData.getValue().genreProperty()
    );

    // Add all columns to the TableView instance
    table
      .getColumns()
      .addAll(selectColumn, titleColumn, authorColumn, yearColumn, genreColumn);


    // Adds example Book object to table
    table.getItems().add(new Book("example", "Example", 1999, "Non-fiction"));

    // Layout for JavaFX App
    BorderPane borderPane = new BorderPane();
    borderPane.setTop(buttonBar);
    borderPane.setCenter(table);
    borderPane.setBottom(searchBox);

    Scene scene = new Scene(borderPane, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
