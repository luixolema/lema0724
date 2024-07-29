package com.lema.test.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DateUtilsTest {

    static Stream<Arguments> provideDataForHolidaysIn() {
        return Stream.of(
                Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 1), 0),  // No holidays
                Arguments.of(LocalDate.of(2024, 6, 30), LocalDate.of(2024, 7, 4), 1),  // July 4th at the end
                Arguments.of(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 7, 4), 0),  // July 4th (Sunday) a not included monday
                Arguments.of(LocalDate.of(2021, 1, 1), LocalDate.of(2021, 7, 5), 1),  // July 4th (Sunday) an included monday
                Arguments.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 3), 1),  // July 4th (Saturday) an included friday
                Arguments.of(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 7, 10), 1),  // July 4th (Saturday) an included friday
                Arguments.of(LocalDate.of(2024, 7, 4), LocalDate.of(2024, 7, 7), 1),  // July 4th at the beginning
                Arguments.of(LocalDate.of(2024, 7, 4), LocalDate.of(2024, 9, 2), 2),  // July 4th and Labor Day as limits
                Arguments.of(LocalDate.of(2023, 12, 31), LocalDate.of(2024, 1, 1), 0),  // Year boundary, no holidays
                Arguments.of(LocalDate.of(2023, 9, 1), LocalDate.of(2023, 9, 10), 1) // 2023 Labor Day included
        );
    }

    static Stream<Arguments> provideDataForWeekendDaysIn() {
        // LocalDate.of(202, 11, 7) is saturday
        return Stream.of(
                Arguments.of(LocalDate.of(2024, 7, 27), LocalDate.of(2024, 7, 27), 1), // Same day saturday
                Arguments.of(LocalDate.of(2024, 7, 22), LocalDate.of(2024, 7, 29), 2), // Monday to Monday
                Arguments.of(LocalDate.of(2024, 7, 19), LocalDate.of(2024, 7, 20), 1), // Friday to Sunday
                Arguments.of(LocalDate.of(2024, 7, 27), LocalDate.of(2024, 8, 3), 3), // from Saturday to Saturday (changing months)
                Arguments.of(LocalDate.of(2024, 7, 27), LocalDate.of(2024, 8, 4), 4), // from Saturday  + 1 week to Sunday (changing months)
                Arguments.of(LocalDate.of(2024, 7, 27), LocalDate.of(2024, 8, 2), 2), // 7 days
                Arguments.of(LocalDate.of(2024, 7, 27), LocalDate.of(2024, 8, 3), 3), // 8 starting at saturday
                Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), 104) // whole year
        );
    }

    public static Stream<Arguments> provideDataForRelevantDaysIn() {
        return Stream.of(
                Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), false, false, false, 0), // all days excluded
                Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), true, false, false, 104), // only weekends
                Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), false, true, false, 260), // only weekdays
                Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), false, false, true, 2), // only holidays
                Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), true, true, false, 364), // no holidays
                Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), true, false, true, 106), // no weekdays
                Arguments.of(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), false, true, true, 262) // no weekends
        );
    }

    @ParameterizedTest(name = "{index} - start: {0}, end: {1}, expected: {2}")
    @MethodSource("provideDataForHolidaysIn")
    void holidaysIn(LocalDate startDate, LocalDate endDate, int expectedHolidays) {
        int actualHolidays = DateUtils.holidaysIn(startDate, endDate);
        assertEquals(expectedHolidays, actualHolidays);
    }

    @Test
    void holidaysInThrowsErrorWithInvalidDates() {
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->
                DateUtils.holidaysIn(LocalDate.of(2024, 1, 1), LocalDate.of(2020, 1, 1))
        );
    }

    @ParameterizedTest(name = "{index} - start: {0}, end: {1}, expected: {2}")
    @MethodSource("provideDataForWeekendDaysIn")
    void weekendDays(LocalDate start, LocalDate end, int expectedWeekends) {
        int actualWeekends = DateUtils.weekendDaysIn(start, end);
        assertEquals(expectedWeekends, actualWeekends);
    }

    @Test
    void weekendDaysInThrowsErrorWithInvalidDates() {
        assertThatExceptionOfType(AssertionError.class).isThrownBy(() ->
                DateUtils.holidaysIn(LocalDate.of(2024, 1, 1), LocalDate.of(2020, 1, 1))
        );
    }

    @ParameterizedTest(name = "{index} - excludingWeekendDays: {2}, excludingWeekdays: {3}, excludingHolidays: {42} expected: {5}")
    @MethodSource("provideDataForRelevantDaysIn")
    void relevantDaysIn(LocalDate start, LocalDate end,
                        boolean excludeWeekends,
                        boolean excludeWeekDays,
                        boolean excludeHolyDays,
                        int expectedResult) {
        int result = DateUtils.relevantDaysIn(start, end, excludeWeekends, excludeWeekDays, excludeHolyDays);
        assertEquals(expectedResult, result);
    }

}