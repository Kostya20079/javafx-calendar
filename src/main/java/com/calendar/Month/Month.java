package com.calendar.Month;

import java.util.Objects;

/**
 * This class represents a month with its number, name, and number of days.
 * */

public class Month {
    private final int numOfMonth;
    private final String nameOfMonth;
    private final int daysInMonth;

    /**
     * Constructs a new {@code Month} object.
     *
     * @param numOfMonth The number of the month (1-12).
     * @param nameOfMonth The name of the month.
     * @param daysInMonth The number of days in month.
     * */
    public Month(int numOfMonth, String nameOfMonth, int daysInMonth) {
        this.numOfMonth = numOfMonth;
        this.nameOfMonth = nameOfMonth;
        this.daysInMonth = daysInMonth;
    }

    /**
     * Returns the numerical representation of the month.
     *
     * @return The number of the month (1-12).
     */
    public int getNumOfMonth() {
        return numOfMonth;
    }

    /**
     * Returns the name of the month.
     *
     * @return The name of the month.
     */
    public String getNameOfMonth() {
        return nameOfMonth;
    }

    /**
     * Returns the number of days in the month.
     *
     * @return The number of days.
     */
    public int getDays() {
        return daysInMonth;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Month month = (Month) o;
        return numOfMonth == month.numOfMonth && daysInMonth == month.daysInMonth && Objects.equals(nameOfMonth, month.nameOfMonth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numOfMonth, nameOfMonth, daysInMonth);
    }

    @Override
    public String toString() {
        return "Month{" +
                "numOfMonth=" + numOfMonth +
                ", nameOfMonth='" + nameOfMonth + '\'' +
                ", daysInMonth=" + daysInMonth +
                '}';
    }
}