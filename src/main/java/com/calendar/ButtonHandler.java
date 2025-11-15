package com.calendar;

import javafx.scene.control.Button;
import java.util.function.Consumer;

public class ButtonHandler {

    private final Calendar calendar;
    private final Consumer<Boolean> onCalendarUpdate;

    public ButtonHandler(Calendar calendar, Consumer<Boolean> onCalendarUpdate) {
        this.calendar = calendar;
        this.onCalendarUpdate = onCalendarUpdate;
    }

    public void setupButtons(Button minusDayBtn, Button plusDayBtn,
                             Button minusWeekBtn, Button plusWeekBtn, Button resetBtn,
                             Button prevMonthBtn, Button nextMonthBtn) {

        minusDayBtn.setOnAction(event -> {
            calendar.minusDay();
            onCalendarUpdate.accept(true);
        });
        plusDayBtn.setOnAction(event -> {
            calendar.plusDay();
            onCalendarUpdate.accept(true);
        });
        minusWeekBtn.setOnAction(event -> {
            calendar.minusWeek();
            onCalendarUpdate.accept(true);
        });
        plusWeekBtn.setOnAction(event -> {
            calendar.plusWeek();
            onCalendarUpdate.accept(true);
        });
        resetBtn.setOnAction(event -> {
            calendar.resetToToday();
            onCalendarUpdate.accept(true);
        });

        prevMonthBtn.setOnAction(e -> {
            calendar.addMonths(-1);
            onCalendarUpdate.accept(false);
        });

        nextMonthBtn.setOnAction(e -> {
            calendar.addMonths(1);
            onCalendarUpdate.accept(false);
        });
    }
}