package com.example.nicholaslaprade_comp228testfall2023;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VehicleApp {
    private final JPanel panel;
    private final JButton filter;
    private final JLabel lblFilter;
    private final JTextField txtFilter;
    private final JTable filterResults;

    public VehicleApp() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));

        // Setting the JTextField to 50 columns to make sure it fits all the way across the screen
        lblFilter = new JLabel("Filter by Make:");
        txtFilter = new JTextField(50);

        // I have this set to "Show All" because I could not figure out how to populate the table at the start of the application
        // Inside the connect method logic when a user selects the "Show All" button without anything inside the JTextField
        // The button label changes to "Filter", when a user presses the button the text will change back to "Show All"
        filter = new JButton("Show All");

        filterPanel.add(lblFilter);
        filterPanel.add(txtFilter);
        filterPanel.add(filter);

        // Creating the JTable filterResults that will be used to show the information from the database
        filterResults = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Make");
        model.addColumn("Model");
        model.addColumn("Year");
        model.addColumn("Color");
        model.addColumn("Price");

        filterResults.setModel(model);
        // I don't want the user to meddle with the layout :)
        filterResults.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(filterResults);
        panel.add(filterPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Button Action Listener that runs the connect method with the data inside the txtFilter as a variable
        filter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connect(txtFilter.getText());
            }
        });
    }
    public void connect(String make) {
        try {
            // Connections for the database, as you have requested inside the documentation my username and password are available
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "";
            String user = "";
            String password = "";

            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                String query;
                // Here is the logic that was described earlier
                if (make != null && !make.isEmpty()) {
                    query = "SELECT * FROM VEHICLE WHERE make = ?";
                    filter.setText("Show All");
                } else {
                    query = "SELECT * FROM VEHICLE";
                    filter.setText("Filter");
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    // Prepared statement for securities reasons (anti SQL injections)
                    if (make != null && !make.isEmpty()) {
                        // If the variable "make" is not null and not empty sets the value of the first parameter in the
                        // prepared statement to the "make" variable
                        preparedStatement.setString(1, make);
                    }
                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        // Creating a Default Table Model so that it can be populated in the event of a button action
                        DefaultTableModel model = new DefaultTableModel();
                        model.addColumn("ID");
                        model.addColumn("Make");
                        model.addColumn("Model");
                        model.addColumn("Year");
                        model.addColumn("Color");
                        model.addColumn("Price");

                        while (resultSet.next()) {
                            // This iterates over the rows inside the "Result Set" and creates an array
                            // that contains the values for the current row
                            Object[] row = {
                                    resultSet.getInt("ID"),
                                    resultSet.getString("Make"),
                                    resultSet.getString("Model"),
                                    resultSet.getInt("Year"),
                                    resultSet.getString("Color"),
                                    resultSet.getInt("Price")
                            };
                            // Each time a row is iterated a row is added to the DTM
                            model.addRow(row);
                        }
                        filterResults.setModel(model);
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            // To catch any errors/exceptions will be displayed in another window and in the terminal
            JOptionPane.showMessageDialog(null, ex.getMessage());
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Vehicle App (Press 'SHOW ALL' To See The Options In The Database)");
        VehicleApp vehicleApp = new VehicleApp();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vehicleApp.panel);
        frame.setSize(800, 450);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}