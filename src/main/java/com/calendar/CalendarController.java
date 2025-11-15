package com.calendar;

import com.calendar.Event.EventManager;
import com.calendar.Month.MonthsTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    private final String EVENTS_PATH = Paths.get(System.getProperty("user.dir")).toAbsolutePath().toString() + "/src/main/java/com/calendar/Event/events.csv";
    private Calendar calendar;
    private ButtonHandler buttonHandler;
    private EventManager eventManager;
    private boolean isUpdate;

    @FXML
    private AnchorPane root;
    @FXML
    private Label currentDate;
    @FXML
    private Label selectedMonthLabel;

    // Buttons
    @FXML
    private Button minusDayBtn, plusDayBtn, minusWeekBtn, plusWeekBtn, resetBtn;
    @FXML
    private Button prevMonthBtn, nextMonthBtn;

    // Event
    @FXML
    private TextField eventTitleField;
    @FXML
    private DatePicker eventDatePicker;
    @FXML
    private Button addEventBtn;
    @FXML
    private ListView<String> eventListView;


    private void init() {
        calendar = new Calendar();
        eventManager = new EventManager(EVENTS_PATH);
        currentDate.setText(calendar.getDayOfWeekByZeller() + ", " + calendar.getDateWithMonthName());
        buttonHandler = new ButtonHandler(calendar, this::refreshCalendar);
        isUpdate = true;

        buttonHandler.setupButtons(minusDayBtn, plusDayBtn, minusWeekBtn, plusWeekBtn, resetBtn, prevMonthBtn, nextMonthBtn);

        setupEventPanel();
        createCalendarCardsGrid();
    }

    private void refreshCalendar(boolean updateCurrentDate) {
        if (updateCurrentDate) {
            currentDate.setText(calendar.getDayOfWeekByZeller() + ", " + calendar.getDateWithMonthName());
            eventDatePicker.setValue(LocalDate.parse(calendar.toString()));
            this.isUpdate = true;
        } else {
            this.isUpdate = calendar.getMonth().getNumOfMonth() == LocalDate.now().getMonthValue();
        }

        selectedMonthLabel.setText(calendar.getMonth().getNameOfMonth() + " " + calendar.getYear());
        createCalendarCardsGrid();
    }

    private void setupEventPanel() {
        eventManager.loadEvents();
        eventDatePicker.setValue(LocalDate.parse(calendar.toString()));
        refreshEventList();

        addEventBtn.setOnAction(e -> {
            String title = eventTitleField.getText();
            LocalDate date = eventDatePicker.getValue();

            if (title == null || title.isBlank() || date == null) {
                System.out.println("Nieprawidłowe dane wydarzenia.");
                return;
            }

            try {
                eventManager.addEvent(date, title);
                eventTitleField.clear();
                refreshEventList();
            } catch (Exception ex) {
                System.err.println("Błąd dodawania wydarzenia: " + ex.getMessage());
            }
        });

        eventDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> refreshEventList());
    }

    private void refreshEventList() {
        eventListView.getItems().clear();

        var events = eventManager.getAllEvents();
        if (events.isEmpty()) {
            eventListView.getItems().add("Brak wydarzeń");
        } else {
            for (var e : events) {
                eventListView.getItems().add("• " + e.getDescription());
            }
        }
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

            dayLabel.setStyle(dayCounter == calendar.getDay() && isUpdate
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
