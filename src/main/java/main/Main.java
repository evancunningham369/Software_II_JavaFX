package main;

import abstractions.Appointment;
import database.AppointmentsQuery;
import database.CustomersQuery;
import database.JDBC;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Stream;

/**
 * Main class to hold general constants and methods to be used throughout the program
 * @Author Evan Cunningham
 */
public class Main extends Application {

    public final String LOGIN_FORM = "LogIn.fxml";
    public final String CUSTOMER_FORM = "Customers.fxml";
    public final String ADD_CUSTOMER_FORM = "AddCustomer.fxml";
    public final String APPOINTMENT_FORM = "Appointments.fxml";
    public final String ADD_APPOINTMENT_FORM = "AddAppointment.fxml";

    /**
     * Loads the initial form
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Loads the form with the given filename
     * @param actionEvent
     * @param filename
     * @throws IOException
     */
    public static void loadFile(ActionEvent actionEvent, String filename) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/" + filename));
        Stage stage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    /**
     * Cancels any changes and loads given form
     * @param event
     * @param fileName
     * @throws IOException
     */
    @FXML
    public void onCancelButtonClick(ActionEvent event, String fileName) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to cancel?");
        if(alert.showAndWait().get() == ButtonType.OK){
            loadFile(event, fileName);
        }
    }

    /**
     * Creates an alert with given parameters
     * @param alertType
     * @param alertTitle
     * @param alertMsg
     */
    public static void makeAlert(Alert.AlertType alertType, String alertTitle, String alertMsg){
        Alert alert = new Alert(alertType);
        alert.setTitle(alertTitle);
        alert.setContentText(alertMsg);
        alert.showAndWait();
    }

    /**
     * Main method to launch program
     * @param args
     */
    public static void main(String[] args) {

        launch();
    }
}
