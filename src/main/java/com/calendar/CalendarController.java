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

    private final String EVENTS_PATH = Paths.get(System.getProperty("user.dir")).toAbsolutePath() + "/src/main/java/com/calendar/Event/events.csv";
    private Calendar calendar;
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
    private ListView<Label> eventListView;

    private void init() {
        calendar = new Calendar();
        eventManager = new EventManager(EVENTS_PATH);
        currentDate.setText(calendar.getDayOfWeekByZeller() + ", " + calendar.getDateWithMonthName());
        ButtonHandler buttonHandler = new ButtonHandler(calendar, this::refreshCalendar);
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
                reloadEvents();
            } catch (Exception ex) {
                System.err.println("Błąd dodawania wydarzenia: " + ex.getMessage());
            }
        });

        eventDatePicker.valueProperty().addListener((obs, oldDate, newDate) -> refreshEventList());
    }

    private void loadEventDates() {
        eventManager.loadEvents();
        eventDates = eventManager.getAllEvents()
                .stream()
                .map(Event::getDate)
                .collect(Collectors.toSet());
    }

    private void reloadEvents() {
        loadEventDates();
        refreshEventList();
        createCalendarCardsGrid();
    }

    private void refreshEventList() {
        eventListView.getItems().clear();

        var events = eventManager.getAllEvents();
        if (events.isEmpty()) {
            Label empty = new Label("Brak wydarzeń");
            empty.getStyleClass().add("event-empty");
            empty.setId("event-empty");
            eventListView.getItems().add(empty);
        } else {
            for (var e : events) {
                Label eventItem = new Label("• " + e.getDescription());
                eventItem.getStyleClass().add("event-li");

                eventItem.setId("event-" + e.getDate().toString());

                eventListView.getItems().add(eventItem);
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
            dayLabel.setId("date-item-" + dayCounter);

            // creating date
            LocalDate selectedDate = LocalDate.of(calendar.getYear(), calendar.getMonth().getNumOfMonth(), dayCounter);

            // open modal on click
            dayLabel.setOnMouseClicked(e -> openEventModal(selectedDate));

            // highlight today
            if (dayCounter == calendar.getDay() && isUpdate) {
                dayLabel.getStyleClass().add("date-item-current");
            }
            // highlight days with events
            else if (eventDates.contains(selectedDate)) {
                dayLabel.getStyleClass().add("date-item-event");
            }
            // basic
            else {
                dayLabel.getStyleClass().add("date-item");
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

    private void openEventModal(LocalDate selectedDate) {
        Event existingEvent = eventManager.getEventForDate(selectedDate);

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Wydarzenie");

        // UI elements reused for both modes
        TextField titleField = new TextField();
        DatePicker datePicker = new DatePicker(selectedDate);

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        dialog.getDialogPane().setContent(content);

        ButtonType addButton = new ButtonType("Dodaj", ButtonBar.ButtonData.OK_DONE);
        ButtonType saveButton = new ButtonType("Zapisz", ButtonBar.ButtonData.OK_DONE);
        ButtonType deleteButton = new ButtonType("Usuń", ButtonBar.ButtonData.LEFT);
        ButtonType closeButton = new ButtonType("Zamknij", ButtonBar.ButtonData.CANCEL_CLOSE);

        if (existingEvent == null) {
            // MODE: ADD EVENT
            dialog.setHeaderText("Dodaj wydarzenie na " + selectedDate);

            titleField.setPromptText("Tytuł wydarzenia");

            content.getChildren().addAll(
                    new Label("Tytuł:"), titleField,
                    new Label("Data:"), datePicker
            );

            dialog.getDialogPane().getButtonTypes().addAll(addButton, closeButton);

            // disable "Dodaj" until title is typed
            Node addNode = dialog.getDialogPane().lookupButton(addButton);
            addNode.setDisable(true);
            titleField.textProperty().addListener((o, oldVal, newVal) ->
                    addNode.setDisable(newVal.trim().isEmpty())
            );

            dialog.setResultConverter(button -> {
                if (button == addButton) {
                    eventManager.addEvent(datePicker.getValue(), titleField.getText());
                    reloadEvents();
                }
                return null;
            });

        } else {
            // MODE: SHOW / EDIT EVENT
            dialog.setHeaderText("Informacje o wydarzeniu: " + selectedDate);

            titleField.setText(existingEvent.getDescription());

            content.getChildren().addAll(
                    new Label("Tytuł:"), titleField,
                    new Label("Data:"), datePicker
            );

            dialog.getDialogPane().getButtonTypes().addAll(saveButton, deleteButton, closeButton);

            dialog.setResultConverter(button -> {
                if (button == deleteButton) {
                    eventManager.removeEvent(selectedDate);
                    reloadEvents();
                } else if (button == saveButton) {
                    // remove old and add edited one
                    eventManager.removeEvent(selectedDate);
                    eventManager.addEvent(datePicker.getValue(), titleField.getText());
                    reloadEvents();
                }
                return null;
            });
        }

        dialog.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        init();
    }
}