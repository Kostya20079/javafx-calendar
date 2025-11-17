package com.calendar.Event;

import java.util.List;

public interface EventWriter {
    void writeEvent(Event event);
    void writeAllEvents(List<Event> events);
}