package com.calendar.Event;

import java.util.List;

public interface EventParser {
    Event parseFrom(String line);
    List<Event> parseLinesFrom(List<String> lines);
}
