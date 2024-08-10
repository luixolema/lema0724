package com.lema.test.components.orders.internal.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DateUtils {
    private static final EnumSet<DayOfWeek> WEEKENDS = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    public static Integer relevantDaysIn(
            LocalDate startDate,
            LocalDate dueDate,
            boolean includeWeekends,
            boolean includeWeekDays,
            boolean includeHolyDays
    ) {
        int totalDays = (int) ChronoUnit.DAYS.between(startDate, dueDate.plusDays(1));
        int chargeDays = totalDays;

        if (!includeWeekends || !includeWeekDays || !includeHolyDays) {
            int weekendedDays = DateUtils.weekendDaysIn(startDate, dueDate);
            int holidays = DateUtils.holidaysIn(startDate, dueDate);

            if (!includeWeekends) {
                chargeDays -= weekendedDays;
            }

            if (!includeWeekDays) {
                chargeDays -= (totalDays - weekendedDays) - holidays;
            }

            if (!includeHolyDays) {
                chargeDays -= holidays;
            }
        }

        return chargeDays;
    }

    /**
     * Calculate the holidays amount Between startDate (inclusive) and endDate (inclusive)
     *  the possible holidays will be with in a 1-year range,
     *  so only startDate and endDate years are taking in consideration to check the holidays
     *
     * @param startDate
     * @param endDate
     */
    public static int holidaysIn(LocalDate startDate, LocalDate endDate) {
        assert startDate.isBefore(endDate.plusDays(1)) : "start date is after endDate";

        Set<LocalDate> possibleHolidays = new HashSet<>(getHolidaysOfYear(startDate.getYear()));
        possibleHolidays.addAll(getHolidaysOfYear(endDate.getYear()));

        return (int) possibleHolidays.stream().filter(holiday ->
                holiday.isAfter(startDate.minusDays(1)) && holiday.isBefore(endDate.plusDays(1))
        ).count();
    }

    public static int weekendDaysIn(LocalDate startDate, LocalDate endDate) {
        assert startDate.isBefore(endDate.plusDays(1)) : "startDate date is after endDate";

        int duration = (int) (ChronoUnit.DAYS.between(startDate, endDate) + 1);

        if (duration < 7) {
            return getWeekendDaysSimple(startDate, endDate);
        }

        int fullWeeks = duration / 7;

        int weekendDays = fullWeeks * 2;

        if (duration % 7 > 0) {
            weekendDays += getWeekendDaysSimple(startDate.plusDays(fullWeeks * 7), endDate);
        }

        return weekendDays;
    }

    private static List<LocalDate> getHolidaysOfYear(int year) {

        LocalDate july4thHoliday = LocalDate.of(year, Month.JULY, 4);
        DayOfWeek dayOfWeek = july4thHoliday.getDayOfWeek();

        if (dayOfWeek == DayOfWeek.SATURDAY) {
            july4thHoliday = july4thHoliday.minusDays(1); // Adjust to Friday
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            july4thHoliday = july4thHoliday.plusDays(1); // Adjust to Monday
        }


        LocalDate laborDay = LocalDate.of(year, Month.SEPTEMBER, 1)
                .with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));

        return List.of(july4thHoliday, laborDay);
    }

    private static int getWeekendDaysSimple(LocalDate start, LocalDate end) {
        return (int) start.datesUntil(end.plusDays(1))
                .map(LocalDate::getDayOfWeek)
                .filter(WEEKENDS::contains)
                .count();
    }
}
