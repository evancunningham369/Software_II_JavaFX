package controller;

import abstractions.Appointment;
import database.AppointmentsQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppointmentController extends Main implements Initializable {

    @FXML
    private TableView appointmentsTable;
    @FXML
    private TableColumn appointmentId_column;
    @FXML
    private TableColumn title_column;
    @FXML
    private TableColumn description_column;
    @FXML
    private TableColumn location_column;
    @FXML
    private TableColumn type_column;
    @FXML
    private TableColumn appointmentStart_column;
    @FXML
    private TableColumn appointmentEnd_column;
    @FXML
    private TableColumn customerId_column;
    @FXML
    private TableColumn userId_column;
    @FXML
    private TableColumn contactId_column;
    @FXML
    private ComboBox locationComboBox;

    public static boolean addingAppointment;
    public static Appointment selectedAppointment = null;
    private ObservableList allLocations = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentsTable.setItems(AppointmentsQuery.getAllAppointments());
        allLocations = AppointmentsQuery.getAllLocations();
        locationComboBox.setItems((ObservableList) allLocations.stream().distinct().collect(Collectors.toCollection(FXCollections::observableArrayList)));

        appointmentId_column.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        title_column.setCellValueFactory(new PropertyValueFactory<>("title"));
        description_column.setCellValueFactory(new PropertyValueFactory<>("description"));
        location_column.setCellValueFactory(new PropertyValueFactory<>("location"));
        type_column.setCellValueFactory(new PropertyValueFactory<>("type"));
        appointmentStart_column.setCellValueFactory(new PropertyValueFactory<>("appointmentStart"));
        appointmentEnd_column.setCellValueFactory(new PropertyValueFactory<>("appointmentEnd"));
        customerId_column.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        userId_column.setCellValueFactory(new PropertyValueFactory<>("userId"));
        contactId_column.setCellValueFactory(new PropertyValueFactory<>("contactId"));
    }

    @FXML
    private void sortBy(ActionEvent event) throws SQLException {
        RadioButton button = (RadioButton) event.getSource();
        appointmentsTable.setItems(AppointmentsQuery.getAllDateTimes(button));
    }

    @FXML
    private void onBackButtonPressed(ActionEvent event) throws IOException {
        loadFile(event, CUSTOMER_FORM);
    }
    @FXML
    public void onAddButtonClicked(ActionEvent event) throws IOException {
        addingAppointment = true;
        loadFile(event, ADD_APPOINTMENT_FORM);
    }

    @FXML
    public void onLocationButtonClicked(ActionEvent event) {
        String locationSelection = (String) locationComboBox.getSelectionModel().getSelectedItem();
        int count = AppointmentsQuery.getLocationCount(locationSelection);

        makeAlert(Alert.AlertType.INFORMATION, "Appointments Found", String.format("There are %d appointments at %s", count, locationSelection));
    }

    @FXML
    public void onUpdateButtonClicked(ActionEvent event) {
        addingAppointment = false;
        try{
            selectedAppointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
            loadFile(event, ADD_APPOINTMENT_FORM);
        } catch (Exception e){
            makeAlert(Alert.AlertType.ERROR, "No Appointment Selected", "Please select an Appointment");
        }
    }
    @FXML
    public void onDeleteButtonClicked(ActionEvent event) throws SQLException {
        selectedAppointment = (Appointment) appointmentsTable.getSelectionModel().getSelectedItem();
        if(selectedAppointment != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Appointment?");
            if(alert.showAndWait().get() == ButtonType.OK){
                AppointmentsQuery.deleteAppointment(selectedAppointment.getAppointmentId());
                makeAlert(Alert.AlertType.INFORMATION, "Appointment Deleted" , "Appointment with Appointment Id " + selectedAppointment.getAppointmentId() + " and Appointment Type " + selectedAppointment.getType() + " has been deleted");
            }
        } else{
            makeAlert(Alert.AlertType.ERROR, "No Appointment Selected", "Please select an Appointment");
        }
        appointmentsTable.setItems(AppointmentsQuery.getAllAppointments());
    }
}
