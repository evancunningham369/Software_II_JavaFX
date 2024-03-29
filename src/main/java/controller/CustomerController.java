package controller;

import abstractions.Customer;
import database.CustomersQuery;
import database.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for the Customer form
 */
public class CustomerController extends Main implements Initializable {

    public static boolean addingCustomer = true;

    public static Customer selectedCustomer = null;

    @FXML
    private TableView customersTable;
    @FXML
    private TableColumn customer_id_column;
    @FXML
    private TableColumn customer_name_column;
    @FXML
    private TableColumn address_column;
    @FXML
    private TableColumn postal_code_column;
    @FXML
    private TableColumn phone_column;
    @FXML
    private TableColumn division_id_column;

    /**
     * Initializes the customer table with all customers in the database
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        customersTable.setItems(CustomersQuery.getAllCustomers());

        customer_id_column.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        customer_name_column.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
        address_column.setCellValueFactory(new PropertyValueFactory<>("address"));
        postal_code_column.setCellValueFactory(new PropertyValueFactory<>("postal_code"));
        phone_column.setCellValueFactory(new PropertyValueFactory<>("phone"));
        division_id_column.setCellValueFactory(new PropertyValueFactory<>("division_id"));
    }

    /**
     * Loads the previous form
     * @param event
     * @throws IOException
     */
    @FXML
    public void LogOut(ActionEvent event) throws IOException {
        JDBC.closeConnection();
        loadFile(event, LOGIN_FORM);
    }

    /**
     * Sets the addCustomer variable and loads the Add Customer form
     * @param event
     * @throws IOException
     */
    @FXML
    public void onAddButtonClicked(ActionEvent event) throws IOException {
        addingCustomer = true;
        loadFile(event, ADD_CUSTOMER_FORM);
    }

    /**
     * Sets the addCustomer variable, validates and loads the Add Customer form with the selected Customer
     * @param event
     * @throws IOException
     */
    @FXML
    public void onUpdateButtonClicked(ActionEvent event) throws IOException {
        addingCustomer = false;
        try{
            selectedCustomer = (Customer) customersTable.getSelectionModel().getSelectedItem();
            loadFile(event, ADD_CUSTOMER_FORM);
        } catch (Exception e){
            makeAlert(Alert.AlertType.ERROR, "No Customer Selected", "Please select a Customer");
        }
    }

    /**
     * Validates and deletes the selected customer from the database
     * @throws SQLException
     */
    @FXML
    public void onDeleteButtonClicked() throws SQLException {
            selectedCustomer = (Customer) customersTable.getSelectionModel().getSelectedItem();
            if(selectedCustomer != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
                if(alert.showAndWait().get() == ButtonType.OK){
                    CustomersQuery.deleteCustomer(selectedCustomer.getCustomer_id());
                }
            } else{
                makeAlert(Alert.AlertType.ERROR, "No Customer Selected", "Please select a Customer");
            }
        customersTable.setItems(CustomersQuery.getAllCustomers());
    }

    /**
     * Loads the Appointment form
     * @param event
     * @throws IOException
     */
    @FXML
    public void onAppointmentsButtonClicked(ActionEvent event) throws IOException {
        loadFile(event, APPOINTMENT_FORM);
    }
}
