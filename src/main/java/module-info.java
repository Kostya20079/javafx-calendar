module com.calendar {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.calendar to javafx.fxml;
    exports com.calendar;
}
