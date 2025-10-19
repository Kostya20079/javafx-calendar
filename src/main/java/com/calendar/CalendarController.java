package com.calendar;

import com.calendar.Month.MonthsTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    private Calendar calendar;

    @FXML
    private Pane root;
    @FXML
    private Label currentDate;

    private void init() {
        calendar = new Calendar();
        currentDate.setText(calendar.toString());
        createCalendarCardsGrid();
    }

    public void createCalendarCardsGrid() {
        final int days = MonthsTable.getDaysInMonth(calendar);

        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(2);
        calendarGrid.setVgap(2);
        calendarGrid.setPadding(new Insets(10));

        createDateCards(calendarGrid, days);

        calendarGrid.setLayoutX(80);
        calendarGrid.setLayoutY(60);

        root.getChildren().add(calendarGrid);
    }

    private void createDateCards(GridPane grid, final int days) {
        // Add day cells
        int dayCounter = 1;
        int currentRow = 1;
        int currentCol = 0;

        while (dayCounter <= days) {
            Label dayLabel = new Label(String.valueOf(dayCounter));
            dayLabel.setPrefSize(100, 100);
            if (dayCounter == calendar.getDay()) {
                dayLabel.setStyle("-fx-background-color: lightgreen; -fx-alignment: center;");
            } else {
                dayLabel.setStyle("-fx-border-color: lightgray; -fx-alignment: center;");
            }
            grid.add(dayLabel, currentCol, currentRow);

            currentCol++;
            if (currentCol >= 7) {
                currentCol = 0;
                currentRow++;
            }
            dayCounter++;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }
}
