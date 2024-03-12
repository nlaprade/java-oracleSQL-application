module com.example.nicholaslaprade_comp228testfall {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires com.oracle.database.jdbc;


    opens com.example.nicholaslaprade_comp228testfall2023 to javafx.fxml;
    exports com.example.nicholaslaprade_comp228testfall2023;
}