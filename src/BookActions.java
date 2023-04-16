import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class BookActions {

  public static void addBook(TableView<Book> table) {
    Dialog<Book> dialog = new Dialog<>();
    dialog.setResizable(true);
    dialog.setTitle("New Book");
    dialog.setHeaderText("Enter book details:");

    // Create input fields and labels
    TextField titleField = new TextField();
    TextField authorField = new TextField();
    TextField yearField = new TextField();
    TextField genreField = new TextField();

    Label errorMessage = new Label();
    errorMessage.setStyle("-fx-text-fill: red;");

    GridPane gridPane = new GridPane();
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.add(new Label("Title:"), 0, 0);
    gridPane.add(titleField, 1, 0);
    gridPane.add(new Label("Author:"), 0, 1);
    gridPane.add(authorField, 1, 1);
    gridPane.add(new Label("Year:"), 0, 2);
    gridPane.add(yearField, 1, 2);
    gridPane.add(new Label("Genre:"), 0, 3);
    gridPane.add(genreField, 1, 3);
    gridPane.add(errorMessage, 1, 4);
    gridPane.setPrefSize(400, 250);

    dialog.getDialogPane().setContent(gridPane);

    ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
    dialog
      .getDialogPane()
      .getButtonTypes()
      .addAll(addButton, ButtonType.CANCEL);

    Button addBtn = (Button) dialog.getDialogPane().lookupButton(addButton);
    addBtn.addEventFilter(
      ActionEvent.ACTION,
      event -> {
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
          errorMessage.setText("Please enter a valid book title.");
          event.consume();
          return;
        }
        try {
          int year = Integer.parseInt(yearField.getText().trim());
          Book newBook = new Book(
            title,
            authorField.getText().trim(),
            year,
            genreField.getText().trim()
          );
          table.getItems().add(newBook);
        } catch (NumberFormatException e) {
          errorMessage.setText("Please enter a valid year.");
          event.consume();
        }
      }
    );

    dialog.showAndWait();
  }

  
  @FXML
  public static void deleteSelectedBooks(TableView<Book> table) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Delete Book");
    alert.setHeaderText("Are you sure you want to delete the selected book?");
    alert.setContentText("This action cannot be undone.");

    alert
      .showAndWait()
      .ifPresent(response -> {
        if (response == ButtonType.OK) {
          
          // List of all the books and the books that are to be deleted
          ObservableList<Book> bookList = table.getItems();
          ObservableList<Book> removeBooks = FXCollections.observableArrayList();

          // Adds a book to the delete list depending on if the checkbox is selected
          for (Book book : bookList) {
            if (book.getSelected()) {
              removeBooks.add(book);
            }
          }

          // Removes books based off referencing the removeBooks list.
          bookList.removeAll(removeBooks);
        }
      });
  }

  public static void openBook(TableView<Book> table) {
    Book selectedBook = table.getSelectionModel().getSelectedItem();

    if (selectedBook == null) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Book Selected");
        alert.setHeaderText("Please select a book to open.");
        alert.showAndWait();
        return;
    }

    Stage bookDetailsStage = new Stage();
    bookDetailsStage.setTitle("Book Details: " + selectedBook.getTitle());

    GridPane gridPane = new GridPane();
    gridPane.setHgap(10);
    gridPane.setVgap(10);
    gridPane.add(new Label("Title:"), 0, 0);
    gridPane.add(new Label(selectedBook.getTitle()), 1, 0);
    gridPane.add(new Label("Author:"), 0, 1);
    gridPane.add(new Label(selectedBook.getAuthor()), 1, 1);
    gridPane.add(new Label("Year:"), 0, 2);
    gridPane.add(new Label(String.valueOf(selectedBook.getYear())), 1, 2);
    gridPane.add(new Label("Genre:"), 0, 3);
    gridPane.add(new Label(selectedBook.getGenre()), 1, 3);
    gridPane.setPrefSize(400, 250);

    HBox buttonBox = new HBox();
    Button deleteButton = new Button("Delete");
    Button editButton = new Button("Edit");
    buttonBox.getChildren().addAll(deleteButton, editButton);
    buttonBox.setSpacing(10);
    buttonBox.setAlignment(Pos.CENTER);

    VBox vbox = new VBox(gridPane, buttonBox);
    vbox.setSpacing(20);
    vbox.setPadding(new Insets(20, 20, 20, 20));

    deleteButton.setOnAction(e -> {
        table.getItems().remove(selectedBook);
        bookDetailsStage.close();
    });

    editButton.setOnAction(e -> {
        bookDetailsStage.close();
        // addBook(table, selectedBook); // Modify addBook method to allow editing
    });

    Scene scene = new Scene(vbox, 400, 300);
    bookDetailsStage.setScene(scene);
    bookDetailsStage.show();
  }
  }
