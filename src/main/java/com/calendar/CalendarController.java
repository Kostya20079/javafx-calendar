package com.calendar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    private Calendar calendar;

    @FXML
    private Label currentDateLabel;

    @FXML
    private Button button;

    private void init() {

        calendar = new Calendar();

        currentDateLabel.setText(calendar.toString());
    }

    @FXML
    private void setWeekAhead() {
        calendar.plusWeek(); // add one week

        currentDateLabel.setText(calendar.toString()); // set result
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }
}
