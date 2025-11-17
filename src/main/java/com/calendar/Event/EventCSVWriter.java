package com.calendar.Event;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@link EventWriter} for writing events to CSV file.
 * <p>
 *     This writer saves events in the format: {@code "dd-MM-yyyy",Event Description\n}
 *     using the date pattern defined in {@link Event#DATE_PATTERN}.
 *     Each event is appended to the file in a new line.
 * </p>
 * */

public class EventCSVWriter implements EventWriter {

    private final String filePath;
    private final DateTimeFormatter DATE_PATTERN = Event.DATE_PATTERN;

    /**
     * Creates a new EventCSVWriter that will write to the specified file.
     *
     * @param filePath the path to the CSV file (must not be null or empty)
     * @throws IllegalArgumentException if filePath is null or empty
     */
    public EventCSVWriter(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Writes a single event to the CSV file.
     * <p>
     *     The event is appended to the file in the format:
     *     {@code "dd-MM-yyyy",Event Description}
     * </p>
     *
     * @param event the event to write (must not be null)
     */
    @Override
    public void writeEvent(Event event) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(event.getDate().format(DATE_PATTERN) + "," + event.getDescription() + "\n");
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Nie udało się zapisać wydarzenia: " + e.getMessage());
        }
    }

    /**
     *  This method writes all events located in memory to csv file
     *  <p>
     *      This method rewrites all file with data inside
     *  </p>
     * */
    @Override
    public void writeAllEvents(List<Event> events) {
        List<String> lines = new ArrayList<>();
        for (Event e : events) {
            lines.add(e.getDate().format(DATE_PATTERN) + "," + e.getDescription());
        }

        try {
            Files.write(Path.of(filePath), lines);
        } catch (IOException e) {
            System.err.println("Nie udało się zapisać wydarzeń: " + e.getMessage());
        }
    }
}