package database;

import abstractions.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract class to hold all queries on the customers table
 */
public abstract class CustomersQuery {

    public static int customerCount;

    /**
     * @return all customers in the customers table
     */
    public static ObservableList getAllCustomers(){
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        try {
            String sql = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID FROM customers";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                int customer_id = rs.getInt(1);
                String customer_name = rs.getString(2);
                String address = rs.getString(3);
                String postal_code = rs.getString(4);
                String phone = rs.getString(5);
                int division_Id = rs.getInt(6);

                Customer customer = new Customer(customer_id, customer_name, address, postal_code, phone, division_Id);
                allCustomers.add(customer);
            }
            customerCount = allCustomers.size() + 1;
            resetAutoInc();
        } catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return allCustomers;
    }

    /**
     * Inserts the given customer into the customers table
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param division_id
     */
    public static void insertCustomer(String name, String address, String postalCode, String phoneNumber, int division_id){
        try {
            String sql = "INSERT INTO customers VALUES(NULL, ?, ?, ?, ?, NULL, NULL, NULL, NULL, ?)";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, name);
            ps.setString(2, address);
            ps.setString(3, postalCode);
            ps.setString(4, phoneNumber);
            ps.setInt(5, division_id);

            ps.execute();

        } catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }

    /**
     * Updates the selected customer with given customer
     * @param customerId
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param division_id
     */
    public static void updateCustomer(int customerId, String name, String address, String postalCode, String phoneNumber, int division_id){
        try {
            String sqlName = "UPDATE CUSTOMERS SET Customer_Name = ? WHERE Customer_ID = ?";
            String sqlAdd = "UPDATE CUSTOMERS SET Address = ? WHERE Customer_ID = ?";
            String sqlPost = "UPDATE CUSTOMERS SET Postal_Code = ? WHERE Customer_ID = ?";
            String sqlPhone = "UPDATE CUSTOMERS SET Phone = ? WHERE Customer_ID = ?";
            String sqlDiv = "UPDATE CUSTOMERS SET Division_ID = ? WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.getConnection().prepareStatement(sqlName);
            PreparedStatement psAdd = JDBC.getConnection().prepareStatement(sqlAdd);
            PreparedStatement psPost = JDBC.getConnection().prepareStatement(sqlPost);
            PreparedStatement psPhone = JDBC.getConnection().prepareStatement(sqlPhone);
            PreparedStatement psDiv = JDBC.getConnection().prepareStatement(sqlDiv);
            ps.setString(1, name);
            psAdd.setString(1, address);
            psPost.setString(1, postalCode);
            psPhone.setString(1, phoneNumber);
            psDiv.setInt(1, division_id);
            ps.setInt(2, customerId);
            psAdd.setInt(2, customerId);
            psPost.setInt(2, customerId);
            psPhone.setInt(2, customerId);
            psDiv.setInt(2, division_id);
            ps.execute();
            psAdd.execute();
            psPost.execute();
            psPhone.execute();
            psDiv.execute();
            resetAutoInc();

        } catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }

    /**
     * Deletes customer with given customer id
     * @param customer_id
     * @throws SQLException
     */
    public static void deleteCustomer(int customer_id) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);
        ps.setInt(1, customer_id);
        AppointmentsQuery.deleteCustomerAppointments(customer_id);
        ps.execute();
        resetAutoInc();
    }

    /**
     * @return all customer ids in the customers table
     */
    public static ObservableList getCustomerIds() {
        ObservableList allCustomerIds = FXCollections.observableArrayList();

        try{
            String sql = "SELECT Customer_ID FROM customers";
            PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                allCustomerIds.add(rs.getInt(1));
            }
        } catch (SQLException sqlException){
            sqlException.printStackTrace();
        }
        return allCustomerIds;
    }

    public static void resetAutoInc() throws SQLException {
        String sql = "ALTER TABLE customers AUTO_INCREMENT = 1";
        PreparedStatement ps = JDBC.getConnection().prepareStatement(sql);

        ps.execute();
    }
}
