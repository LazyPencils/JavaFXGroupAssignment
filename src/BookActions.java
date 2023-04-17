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

    // Add button Logic by Mahn Phu
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

  // Delete button Logic by Mahn Phu
  // Refactored by Yihang
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

  // Open Books button logic by Zohair
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
        BookActions.editSelectedBook(table); 
    });

    Scene scene = new Scene(vbox, 400, 300);
    bookDetailsStage.setScene(scene);
    bookDetailsStage.show();
  }

  public static void editSelectedBook(TableView<Book> table) {
    // Initialize selectedBook and its index in the table
    Book selectedBook = null;
    int selectedIndex = -1;

    // Iterate through the table's items to find the selected book
    for (int i = 0; i < table.getItems().size(); i++) {
        Book book = table.getItems().get(i);
        if (book.getSelected()) {
            if (selectedBook == null) {
                // First book found, store it
                selectedBook = book;
                selectedIndex = i;
            } else {
                // More than one book is selected, show an error message
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Edit Book");
                alert.setHeaderText("Multiple Books Selected");
                alert.setContentText("Please select only one book to edit.");
                alert.showAndWait();
                return;
            }
        }
    }

    // If no book is selected, show an error message
    if (selectedBook == null) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Edit Book");
        alert.setHeaderText("No Book Selected");
        alert.setContentText("Please select a book to edit.");
        alert.showAndWait();
        return;
    }

    // Create a dialog for editing book details
    Dialog<Book> dialog = new Dialog<>();
    dialog.setResizable(true);
    dialog.setTitle("Edit Book");
    dialog.setHeaderText("Edit book details:");

    // Create input fields and labels with the selected book's information
    TextField titleField = new TextField(selectedBook.getTitle());
    TextField authorField = new TextField(selectedBook.getAuthor());
    TextField yearField = new TextField(Integer.toString(selectedBook.getYear()));
    TextField genreField = new TextField(selectedBook.getGenre());

    // Create a label to display error messages
    Label errorMessage = new Label();
    errorMessage.setStyle("-fx-text-fill: red;");

    // Create a GridPane layout for the dialog
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

    // Add the gridPane to the dialog
    dialog.getDialogPane().setContent(gridPane);

    // Add Save and Cancel buttons to the dialog
    ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

    // Create an event filter for the Save button
    Button saveBtn = (Button) dialog.getDialogPane().lookupButton(saveButton);
    Book finalSelectedBook = selectedBook;
    int finalSelectedIndex = selectedIndex;
    saveBtn.addEventFilter(
            ActionEvent.ACTION,
            event -> {
                String title = titleField.getText().trim();
                // Check if the title is not empty
                if (title.isEmpty()) {
                    errorMessage.setText("Please enter a valid book title.");
                    event.consume();
                    return;
                }
                try {
                    // Check if the year is a valid integer
                    int year = Integer.parseInt(yearField.getText().trim());

                    // Update the selected book's properties with the input values
                    finalSelectedBook.setTitle(title);
                    finalSelectedBook.setAuthor(authorField.getText().trim());
                    finalSelectedBook.setYear(year);
                    finalSelectedBook.setGenre(genreField.getText().trim());

                    // Refresh the table to display the updated values
                    table.getItems().set(finalSelectedIndex, finalSelectedBook);
                } catch (NumberFormatException e) {
                    // If the year is not a valid integer, display an error message
                    errorMessage.setText("Please enter a valid year.");
                    event.consume();
                }
            }
    );

    // Show the dialog and wait for the user's action
    dialog.showAndWait();
}

  }
