package com.calendar.Event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * This class represents an event with a specific date and description.
 * This immutable class stores event information and provides:
 * <ul>
 *     <li>Basic event data storage (date and description)</li>
 *     <li>Standard date formatting pattern ({@code "dd-MM-yyyy"})</li>
 *     <li>Proper equality comparison and hashing</li>
 * </ul>
 *
 * @see LocalDate
 * */
public class Event {

    private final LocalDate date;
    private final String description;

    /**
     * Standard date formatter pattern for all Event instances.
     * <p>
     *      Pattern format: day-month-year
     * </p>
     */
    public static final DateTimeFormatter DATE_PATTERN = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Creates a new Event with the specified date and description.
     *
     * @param date the date of the event (must not be null)
     * @param description the event description (must not be null or empty)
     * @throws IllegalArgumentException if date or description are invalid
     */
    public Event(LocalDate date, String description) {
        this.date = date;
        this.description = description;
    }

    // GETTERS
    /**
     * Gets the event date.
     *
     * @return the event date (never null)
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Gets the event description
     *
     * @return the event description (never null or empty)
     * */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(date, event.date) && Objects.equals(description, event.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, description);
    }

    @Override
    public String toString() {
        return "Event{" +
                "date=" + date +
                ", description='" + description + '\'' +
                '}';
    }
}
