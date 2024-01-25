package com.gridnine.testing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.gridnine.testing.FlightBuilder.createFlight;
import static org.assertj.core.api.Assertions.assertThat;
class FLightFilterTest {
    List<Flight> flights;
    List<Flight> expectedList;
    FLightFilter fLightFilter;

    @BeforeEach
    void setUp() {
        LocalDateTime oneHourFromNow = LocalDateTime.now().plusHours(1);
        flights = Arrays.asList(
                //index 0 - A normal flight with two hour duration
                createFlight(oneHourFromNow, oneHourFromNow.plusHours(2)),
                //index 1 - A normal multi segment flight
                createFlight(oneHourFromNow, oneHourFromNow.plusHours(2),
                        oneHourFromNow.plusHours(3), oneHourFromNow.plusHours(5)),
                //index 2 - A flight departing in the past
                createFlight(oneHourFromNow.minusDays(6), oneHourFromNow),
                //index 3 - A flight that departs before it arrives
                createFlight(oneHourFromNow, oneHourFromNow.minusHours(6)),
                //index 4 - A flight with more than two hours ground time
                createFlight(oneHourFromNow, oneHourFromNow.plusHours(2),
                        oneHourFromNow.plusHours(5), oneHourFromNow.plusHours(6)),
                //index 5 - Another flight with more than two hours ground time
                createFlight(oneHourFromNow, oneHourFromNow.plusHours(2),
                        oneHourFromNow.plusHours(3), oneHourFromNow.plusHours(4),
                        oneHourFromNow.plusHours(6), oneHourFromNow.plusHours(7)));
        expectedList = new ArrayList<>(flights);
        fLightFilter = new FLightFilter(flights);
    }

    @Test
    void getFlightsWithDepartureAfterInstant() {
        expectedList.remove(2);
        flights = fLightFilter.getFlightsWithDepartureAfterInstant(LocalDateTime.now());

        assertThat(flights).isEqualTo(expectedList);
    }

    @Test
    void getFlightsWithDepartureBeforeArrival() {
        expectedList.remove(3);
        flights = fLightFilter.getFlightsWithDepartureBeforeArrival();

        assertThat(flights).isEqualTo(expectedList);
    }

    @Test
    void getFlightsWithOverallGroundTimeNotLongerThan() {
        Duration duration = Duration.of(2, ChronoUnit.HOURS);
        expectedList.remove(5);
        expectedList.remove(4);
        flights = fLightFilter.getFlightsWithOverallGroundTimeNotLongerThan(duration);

        assertThat(flights).isEqualTo(expectedList);
    }
}