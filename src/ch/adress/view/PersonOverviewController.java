package ch.adress.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import ch.adress.MainApp;
import ch.adress.util.DateUtil;
import ch.adress.model.Person;
import org.controlsfx.dialog.Dialogs;

import java.awt.*;

public class PersonOverviewController {
    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    // Reference to the main application.
    private MainApp mainApp;

    public PersonOverviewController() {
    }

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        // Add observable list data to the table
        personTable.setItems(mainApp.getPersonData());
    }

    private void showPersonDetails(Person person) {
        if (person != null) {
            // Fill the labels with info from the person object.
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateUtil.format(person.getBirthday()));
        } else {
            // Person is null, remove all the text.
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    @FXML
    public void handleDeletePerson() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            personTable.getItems().remove(selectedIndex);
        } else {
            Dialogs.create()
                    .title("No selection")
                    .masthead("No person selected")
                    .message("Please select a person in the table")
                    .showWarning();
        }

    }

    @FXML
    public void handleNewPerson(){
        Person newPerson = new Person();
        boolean okClicked = mainApp.showPersonEditDialog(newPerson);
        if (okClicked){
            mainApp.getPersonData().add(newPerson);
        }
    }

    @FXML
    public void handleEditPerson(){
        Person selPerson = personTable.getSelectionModel().getSelectedItem();
        if (selPerson != null) {
            boolean okClicked = mainApp.showPersonEditDialog(selPerson);
            if (okClicked){
                showPersonDetails(selPerson);
            }
        } else {
            Dialogs.create()
                    .title("No selection")
                    .masthead("No person selected")
                    .message("Please select a person in the table")
                    .showWarning();
        }
    }

}