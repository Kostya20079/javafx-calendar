package com.calendar;

import com.calendar.Event.EventManager;
import com.calendar.Month.MonthsTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

//    private final String EVENTS_PATH;
    private Calendar calendar;
    private ButtonHandler buttonHandler;
    private EventManager eventManager;

    @FXML
    private AnchorPane root;
    @FXML
    private Label currentDate;

    // Buttons
    @FXML
    private Button minusDayBtn, plusDayBtn, minusWeekBtn, plusWeekBtn, resetBtn;


    private void init() {
        calendar = new Calendar();
//        eventManager = new EventManager();
        currentDate.setText(calendar.getDateWithMonth());
        buttonHandler = new ButtonHandler(calendar, this::refreshCalendar);

        buttonHandler.setupButtons(minusDayBtn, plusDayBtn, minusWeekBtn, plusWeekBtn, resetBtn);

        createCalendarCardsGrid();
    }

    private void refreshCalendar() {
        currentDate.setText(calendar.getDateWithMonth());
        createCalendarCardsGrid();
    }

    public void createCalendarCardsGrid() {
        final int days = MonthsTable.getDaysInMonth(calendar);

        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(2);
        calendarGrid.setVgap(2);
        calendarGrid.setPadding(new Insets(10));
        calendarGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
        calendarGrid.setPrefWidth(850);
        calendarGrid.setMaxWidth(Region.USE_PREF_SIZE);

        createDateCards(calendarGrid, days);

        calendarGrid.setLayoutX(150);
        calendarGrid.setLayoutY(110);

        // remove old one
        root.getChildren().removeIf(node -> node instanceof GridPane);
        root.getChildren().add(calendarGrid);
    }

    private void createDateCards(GridPane grid, final int days) {
        // Add day cells
        int dayCounter = 1;
        int currentRow = 1;
        int currentCol = 0;

        final double cellWidth = 150;
        final double cellHeight = 130;

        while (dayCounter <= days) {
            Label dayLabel = new Label(String.valueOf(dayCounter));
            dayLabel.setPrefSize(cellWidth, cellHeight);
            dayLabel.setAlignment(Pos.CENTER);
            dayLabel.setStyle(dayCounter == calendar.getDay()
                    ? "-fx-background-color: #F0DFAD; -fx-border-color: gray;"
                    : "-fx-border-color: lightgray; -fx-alignment: center;");

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
