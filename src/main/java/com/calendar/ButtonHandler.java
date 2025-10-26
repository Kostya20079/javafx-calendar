package com.calendar;

import javafx.scene.control.Button;

public class ButtonHandler {

    private Calendar calendar;
    private final Runnable onCalendarUpdate;

    public ButtonHandler(Calendar calendar, Runnable onCalendarUpdate) {
        this.calendar = calendar;
        this.onCalendarUpdate = onCalendarUpdate;
    }

    public void setupButtons(Button minusDayBtn, Button plusDayBtn,
                             Button minusWeekBtn, Button plusWeekBt, Button resetBtn) {

        minusDayBtn.setOnAction(event -> {
            calendar.minusDay();
            onCalendarUpdate.run();
        });
        plusDayBtn.setOnAction(event -> {
            calendar.plusDay();
            onCalendarUpdate.run();
        });
        minusWeekBtn.setOnAction(event -> {
            calendar.minusWeek();
            onCalendarUpdate.run();
        });
        plusWeekBt.setOnAction(event -> {
            calendar.plusWeek();
            onCalendarUpdate.run();
        });
        resetBtn.setOnAction(event -> {
            calendar.resetToToday(); // reset calendar data to current date
            onCalendarUpdate.run();
        });
    }
}