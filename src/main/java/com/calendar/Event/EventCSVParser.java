package com.calendar.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link EventParser} for parsing events from CSV format.
 * <p>
 *      This parser converts CSV lines into {@link Event} objects using the format:
 *      {@code "yyyy-MM-dd,Event Description"}. The date format is shared with
 *      the {@link Event} class through {@link Event#DATE_PATTERN}.
 * </p>
 *
 * @see EventParser
 * @see Event
 */
public class EventCSVParser implements EventParser {

    private final DateTimeFormatter DATE_PATTERN = Event.DATE_PATTERN;

    /**
     * Parses a single line of CSV into an Event object.
     * <p>
     *      Expected format: {@code "dd-MM-yyyy,Event Description"}
     * </p>
     *
     * @param line the CSV line to parse (must not be null or empty)
     * @return the parsed Event object
     * @throws IllegalArgumentException if the line is malformed
     * @throws java.time.format.DateTimeParseException if the date cannot be parsed
     */
    @Override
    public Event parseFrom(String line) {
        String[] columns = line.split(",");

        final LocalDate date = LocalDate.parse(columns[0], DATE_PATTERN);
        final String description = columns[1];

        return new Event(date, description);
    }

    /**
     * Parses multiple lines of CSV into a list of Event objects.
     * <p>
     *      Each line is processed by {@link #parseFrom(String)}.
     *      Empty line will cause an IllegalArgumentException.
     * </p>
     *
     * @param lines the list of CSV lines to parse (must not be null)
     * @return a list of parsed Event objects
     * @throws IllegalArgumentException if any line is malformed
     * @throws java.time.format.DateTimeParseException if any date cannot be parsed
     * @see #parseFrom(String)
     */
    @Override
    public List<Event> parseLinesFrom(final List<String> lines) {
        final List<Event> eventsList = new ArrayList<>();

        for (final String line : lines) {
            eventsList.add(parseFrom(line));
        }
        return eventsList;
    }
}