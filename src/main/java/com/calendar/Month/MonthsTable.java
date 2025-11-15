package com.calendar.Month;

import com.calendar.Calendar;

/**
 * A utility class providing a static table of months and methods to retrieve month information,
 * including handling leap years for February.
 * This class cannot be instantiated.
 */
public class MonthsTable {
    /**
     * An immutable array containing {@link Month} objects for each month of a standard year,
     * indexed from 0 (January) to 11 (December). Leap year adjustments for February are handled
     * by specific methods.
     */
    private static final Month[] months;

    /**
     * Initializes the static {@code months} array with standard month data.
     * This block runs once when the class is loaded.
     */
    static {
        months = new Month[] {
                new Month(1, "Styczeń", 31), // January
                new Month(2, "Luty", 28),    // February (standard)
                new Month(3, "Marzec", 31),  // March
                new Month(4, "Kwiecień", 30),// April
                new Month(5, "Maj", 31),    // May
                new Month(6, "Czerwiec", 30),// June
                new Month(7, "Lipiec", 31),  // July
                new Month(8, "Sierpień", 31),// August
                new Month(9, "Wrzesień", 30),// September
                new Month(10, "Październik", 31),// October
                new Month(11, "Listopad", 30),// November
                new Month(12, "Grudzień", 31) // December
        };
    }

    private static final String[] monthsGenitive = {
            "stycznia",
            "lutego",
            "marca",
            "kwietnia",
            "maja",
            "czerwca",
            "lipca",
            "sierpnia",
            "września",
            "października",
            "listopada",
            "grudnia"
    };

    public static String getMonthNameGenitive(int monthNumber) {
        return monthsGenitive[monthNumber - 1];
    }


    /**
     * Retrieves a {@link Month} object based on its numerical representation (1-12)
     * for a standard year. This method does not account for leap years.
     *
     * @param monthNumber The number of the month (1 for January, 12 for December).
     * @return The {@link Month} object corresponding to the given month number.
     * @throws ArrayIndexOutOfBoundsException if {@code monthNumber} is not between 1 and 12.
     */
    public static Month getMonth(int monthNumber) {
        return months[monthNumber - 1];
    }

    public static Month getMonth(int year, int monthNumber) {
        if(monthNumber == 2) {
            if(isLeapYear(year)) {
                return new Month(2, "Luty", 29);
            } else {
                return months[monthNumber - 1];
            }
        }
        return months[monthNumber - 1];
    }

    /**
     * Calculates the number of days in a specified month for a given year,
     * correctly accounting for leap years in February.
     *
     * @param year        The year for which to calculate the days.
     * @param monthNumber The number of the month (1 for January, 12 for December).
     * @return The number of days in the specified month. Returns 29 for February in a leap year,
     * and 28 for February in a common year.
     * @throws ArrayIndexOutOfBoundsException if {@code monthNumber} is not between 1 and 12.
     */
    public static int getDaysInMonth(int year, int monthNumber) {
        if(monthNumber == 2) {
            return isLeapYear(year) ? 29 : 28;
        }

        return months[monthNumber - 1].getDays();
    }

    public static int getDaysInMonth(Calendar calendar) {
        int monthNum = calendar.getMonth().getNumOfMonth();
        int yearNum = calendar.getYear();
        return getDaysInMonth(yearNum, monthNum);
    }

    /**
     * Checks if a given year is a leap year.
     * A year is a leap year if it is divisible by 4, unless it is divisible by 100
     * but not by 400.
     *
     * @param year The year to check.
     * @return {@code true} if the year is a leap year, {@code false} otherwise.
     */
    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
}