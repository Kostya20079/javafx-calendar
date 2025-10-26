package com.calendar;

import com.calendar.Exceptions.CreateCalendarException;
import com.calendar.Month.MonthsTable;
import com.calendar.Month.Month;

import java.time.LocalDate;

/**
 * Represents a specific date (day, month, year) and provides methods for date manipulation,
 * comparison, and determination of the day of the week.
 * This class implements {@link Comparable} for date ordering.
 */
public class Calendar implements Comparable<Calendar> {

    // VARIABLES
    private int day;
    private Month month;
    private int year;

    // CONSTRUCTORS
    /**
     * Constructs a new {@code Calendar} object initialized to the current system date.
     * It uses {@link LocalDate#now()} to get the current day, month, and year.
     */
    public Calendar() {
        LocalDate data = LocalDate.now();

        this.day = data.getDayOfMonth();
        this.month = MonthsTable.getMonth(data.getMonthValue());
        this.year = data.getYear();
    }

    public Calendar(Calendar other) {
        this.day = other.getDay();
        this.year = other.getYear();
        this.month = other.getMonth();
    }

    public void resetToToday() {
        LocalDate data = LocalDate.now();
        this.day = data.getDayOfMonth();
        this.month = MonthsTable.getMonth(data.getMonthValue());
        this.year = data.getYear();
    }

    /**
     * Constructs a new {@code Calendar} object with the specified day, month, and year.
     * Performs validation to ensure the date is valid (e.g., day within month's range,
     * month within 1-12).
     *
     * @param day The day of the month (1-31, depending on the month and year).
     * @param month The month number (1 for January, 12 for December).
     * @param year The year.
     * @throws CreateCalendarException If the provided day or month is out of its valid range
     * for the given year.
     */
    public Calendar(int day, int month, int year) throws CreateCalendarException {
        this.year = year;

        if(day >= 1 && day <= MonthsTable.getDaysInMonth(year, month)) {
            this.day = day;
        } else {
            throw new CreateCalendarException("Nieprawidłowy dzień miesiąca!");
        }

        if(month >= 1 && month <= 12) {
            this.month = MonthsTable.getMonth(year, month);
        } else {
            throw new CreateCalendarException("Miesiąc musi być w zakresie 1-12!");
        }
    }

    // METHODS
    /**
     * Advances the calendar date by one week (7 days).
     * This method correctly handles month and year rollovers.
     */
    public void plusWeek() {
        int newDay = day + 7;

        while (newDay > month.getDays()) {
            newDay -= month.getDays();
            if (month.getNumOfMonth() == 12) {
                month = MonthsTable.getMonth(year + 1, 1);
                year++;
            } else {
                month = MonthsTable.getMonth(year, month.getNumOfMonth() + 1);
            }
        }
        day = newDay;
    }

    /**
     * Decreases the calendar date by one week (7 days).
     * This method correctly handles month and year rollovers backward.
     */
    public void minusWeek() {
        int newDay = day - 7;
        while (newDay < 1) {
            if(month.getNumOfMonth() == 1) {
                month = MonthsTable.getMonth(year - 1, 12);
                year--;
            } else {
                month = MonthsTable.getMonth(year, month.getNumOfMonth() - 1);
            }
            newDay += month.getDays();
        }
        day = newDay;
    }

    /**
     * Advances the calendar date by one day.
     * This method correctly handles month and year rollovers.
     */
    public void plusDay() {
        int newDay = day + 1;
        if (newDay > month.getDays()) {
            newDay = 1;
            if (month.getNumOfMonth() == 12) {
                month = MonthsTable.getMonth(year + 1, 1);
                year++;
            } else {
                month = MonthsTable.getMonth(year, this.month.getNumOfMonth() + 1);
            }
        }
        day = newDay;
    }

    /**
     * Decreases the calendar date by one day.
     * This method correctly handles month and year rollovers backward.
     */
    public void minusDay() {
        int newDay = day - 1;
        if (newDay < 1) {
            if (month.getNumOfMonth() == 1) {
                month = MonthsTable.getMonth(year - 1, 12);
                year--;
            } else {
                month = MonthsTable.getMonth(year, this.month.getNumOfMonth() - 1);
            }
            day = month.getDays();
        } else {
            day = newDay;
        }
    }

    /**
     * Determines the day of the week for this calendar date using a reference date (November 30, 2020, a Monday).
     * It iteratively adds or subtracts weeks/days until the current date is reached, tracking the day of the week.
     *
     * @return The name of the day of the week.
     */
    public String getDayOfWeekByDeterminant() {
        Calendar temp = new Calendar(30, 11, 2020); // wiem że to jest Poniedzialek

        String[] days = {"Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek", "Sobota", "Niedziela"};
        int dayIndex = 0;


        // jeżeli mniejsze
        if(temp.compareTo(this) < 0) {
            while (temp.compareTo(this) < 0) {
                temp.plusWeek();
            }
            while (!temp.equals(this)) {
                temp.minusDay();
                dayIndex = (dayIndex + 6) % 7;
            }
        }

        // jeżeli jest równe bądź większe
        if (temp.compareTo(this) > 0) {
            while (temp.compareTo(this) > 0) {
                temp.minusWeek();
            }
            while (!temp.equals(this)) {
                temp.plusDay();
                dayIndex = (dayIndex + 1) % 7;
            }
        }

        return days[dayIndex % 7];
    }

    /**
     * Determines the day of the week for this calendar date using Zeller's congruence algorithm.
     * This algorithm directly calculates the day of the week for any Gregorian calendar date.
     *
     * @return The name of the day of the week in Polish (e.g., "Sobota", "Niedziela").
     * The mapping is 0=Saturday, 1=Sunday, ..., 6=Friday.
     */
    public String getDayOfWeekByZeller() {

        // (0=Sobota, 1=Niedziela, ...)
        String[] days = {"Sobota", "Niedziela", "Poniedziałek", "Wtorek", "Środa", "Czwartek", "Piątek"};

        int q = this.day; // dzień misiąca
        int m = this.month.getNumOfMonth(); // numer miesiąca
        int y = this.year; // rok

        // (3-Marzec, 4-Kwiecień, ..., 14-Luty)
        if(m < 3) {
            m += 12;
            y--;
        }

        int K = y % 100; // ostatek po dzieleniu
        int J = y / 100; // zero-based year

        // dla Gregirianskigo kalendarza
        int h = (q + (13 * (m + 1)) / 5 + K + (K / 4) + (J / 4) - 2 * J) % 7;

        return days[h];
    }

    // GETTERS AND SETTERS
    /**
     * Returns the day of the month for this {@code Calendar} object.
     * @return The day (1-31).
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the day of the month for this {@code Calendar} object.
     * Note: This setter does not perform validation. Use the constructor for validated date creation.
     * @param day The new day of the month.
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * Returns the {@link Month} object for this {@code Calendar} object.
     * @return The {@link Month} object.
     */
    public Month getMonth() {
        return month;
    }

    /**
     * Sets the month for this {@code Calendar} object.
     * Note: This setter does not perform validation. Use the constructor for validated date creation.
     * @param month The new {@link Month} object.
     */
    public void setMonth(Month month) {
        this.month = month;
    }

    /**
     * Returns the year for this {@code Calendar} object.
     * @return The year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year for this {@code Calendar} object.
     * Note: This setter does not perform validation. Use the constructor for validated date creation.
     * @param year The new year.
     */
    public void setYear(int year) {
        this.year = year;
    }

    public String getDateWithMonth() {
        return String.format("%d %s %d", day, month.getNameOfMonth(), year);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(obj == null || getClass() != obj.getClass()) return false;

        Calendar other = (Calendar) obj;
        return this.day == other.day &&
                this.month.getNumOfMonth() == other.getMonth().getNumOfMonth() &&
                this.year == other.year;
    }

    @Override
    public int compareTo(Calendar other) {
        if (this.year != other.year) return Integer.compare(this.year, other.getYear());
        if (this.month.getNumOfMonth() != other.getMonth().getNumOfMonth()) return Integer.compare(this.month.getNumOfMonth(), other.getMonth().getNumOfMonth());
        return Integer.compare(this.day, other.getDay());
    }

    @Override
    public String toString() {
        return String.format("%02d-%02d-%04d", day, month.getNumOfMonth(), year);
    }
}