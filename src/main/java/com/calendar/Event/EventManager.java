package com.calendar.Event;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of events with persistence in CSV format.
 * <p>
 *     This class provides functionality to:
 *     <ul>
 *         <li>Load events from a CSV format</li>
 *         <li>Writing event to CSV file</li>
 *         <li>Parsing data from CSV file</li>
 *     </ul>
 *     The CSV file is automatically created if it is not exist
 * </p>
 *
 * @see Event
 * @see EventCSVParser
 * @see EventCSVWriter
 * */

public class EventManager {
    private List<Event> events;
    private final String filePath;
    private final EventCSVParser parser;
    private final EventCSVWriter writer;

    /**
     * Creates a new EventManager for the specified file.
     * <p>
     *      Initializes the parser and writer, and creates an empty event list.
     *      Note: You must call {@link #loadEvents()} to load existing events.
     * </p>
     *
     * @param filePath path to the CSV storage file (must not be null or empty)
     * @throws IllegalArgumentException if filePath is null or empty
     */
    public EventManager(String filePath) {
        this.filePath = filePath;
        this.parser = new EventCSVParser();
        this.writer = new EventCSVWriter(filePath);
        this.events = new ArrayList<>();
    }

    /**
     * Loads events from the CSV file into memory.
     * <p>
     *      If the file doesn't exist, it will be created and an empty list will be loaded.
     *      On any error, an empty list will be initialized and an error message printed.
     * </p>
     *
     * @throws IOException if there are problems creating the file
     */
    public void loadEvents() {
        try {
            Path pathToFile = Path.of(filePath);
            // check if file exist if not create it
            if (!Files.exists(pathToFile)) {
                Files.createFile(pathToFile);
                this.events = new ArrayList<>();
                return;
            }
            List<String> lines = Files.readAllLines(pathToFile);
            this.events = parser.parseLinesFrom(lines);
        } catch(IOException e) {
            System.err.println("Błąd podczas wczytywania wydarzeń: " + e.getMessage());
            this.events = new ArrayList<>();
        }
    }

    /**
     * This method adds a new event and persists it to the CSV file.
     * <p>
     *      The event is immediately written to the file and added to the in-memory list.
     * </p>
     *
     * @param date the date of the event (must not be null)
     * @param description the event description (must not be null or empty)
     * @throws IllegalArgumentException if date or description are invalid
     * @throws IOException if there are problems writing to the file
     */
    public void addEvent(LocalDate date, String description) {
        Event event = new Event(date, description);
        writer.writeEvent(event);
        events.add(event);
    }

    /**
     * Gets all events for a specific date.
     *
     * @param date the date to filter events (must not be null)
     * @return a list of matching events (never null, may be empty)
     * @throws IllegalArgumentException if date is null
     */
    public List<Event> getEventsForDate(LocalDate date) {
        List<Event> result = new ArrayList<>();
        for (Event event : events) {
            if (event.getDate().equals(date)) {
                result.add(event);
            }
        }
        return result;
    }

    /**
     * Looking for fist similar event from the list
     *
     * @param date the date to filter events (must not be null)
     * @return found event or null is event is not exist
     * */
    public Event getEventForDate(LocalDate date) {
        Event result = null;
        for (Event event : events) {
            if (event.getDate().equals(date)) {
                result = event;
            }
        }
        return result;
    }

    /**
     * This method gets a copy of all events.
     * <p>
     *     Returns a defencive copy to project in internal list.
     * </p>
     *
     * @return a new list containing all events
     * */
    public List<Event> getAllEvents() {
        return new ArrayList<>(events); // copy of original events array
    }
}