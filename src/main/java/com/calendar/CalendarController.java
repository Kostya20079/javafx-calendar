package com.calendar;

import com.calendar.Event.Event;
import com.calendar.Event.EventManager;
import com.calendar.Month.MonthsTable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class CalendarController implements Initializable {

    private final String EVENTS_PATH = Paths.get(System.getProperty("user.dir")).toAbsolutePath().toString() + "/src/main/java/com/calendar/Event/events.csv";
    private Calendar calendar;
    private ButtonHandler buttonHandler;
    private EventManager eventManager;
    private boolean isUpdate;
    private Set<LocalDate> eventDates;

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

        loadEventDates();
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

    private void loadEventDates() {
        eventManager.loadEvents(); // make sure events are loaded
        eventDates = eventManager.getAllEvents()
                .stream()
                .map(Event::getDate)
                .collect(Collectors.toSet());
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
        int dayCounter = 1;
        int currentRow = 1;
        int currentCol = 0;

        final double cellWidth = 150;
        final double cellHeight = 130;

        while (dayCounter <= days) {
            Label dayLabel = new Label(String.valueOf(dayCounter));
            dayLabel.setPrefSize(cellWidth, cellHeight);
            dayLabel.setAlignment(Pos.CENTER);

            LocalDate selectedDate = LocalDate.of(calendar.getYear(), calendar.getMonth().getNumOfMonth(), dayCounter);

            // open modal on click
            dayLabel.setOnMouseClicked(e -> openAddEventModal(selectedDate));

            // highlight today
            if (dayCounter == calendar.getDay() && isUpdate) {
                dayLabel.setStyle("-fx-background-color: #F0DFAD; -fx-border-color: gray;");
            }
            // highlight days with events
            else if (eventDates.contains(selectedDate)) {
                dayLabel.setStyle("-fx-background-color: #ADE8F4; -fx-border-color: gray; -fx-alignment: center;");
            }
            else {
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

    private void openAddEventModal(LocalDate selectedDate) {

        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Dodaj wydarzenie");
        dialog.setHeaderText("Dodaj wydarzenie na " + selectedDate);

        ButtonType addButtonType = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        TextField titleField = new TextField();
        titleField.setPromptText("Tytuł wydarzenia");

        DatePicker datePicker = new DatePicker(selectedDate);

        VBox content = new VBox(10, new Label("Tytuł:"), titleField, new Label("Data:"), datePicker);
        content.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(content);

        Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);
        titleField.textProperty().addListener((obs, oldVal, newVal) -> addButton.setDisable(newVal.trim().isEmpty()));

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return titleField.getText();
            }
            return null;
        });

        dialog.showAndWait().ifPresent(title -> {
            try {
                eventManager.addEvent(datePicker.getValue(), title);

                loadEventDates();
                refreshEventList();
                createCalendarCardsGrid();

            } catch (Exception ex) {
                System.err.println("Błąd dodawania wydarzenia: " + ex.getMessage());
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }
}
