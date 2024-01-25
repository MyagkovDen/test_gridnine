package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Flight> flights = FlightBuilder.createFlights();
        System.out.println("Initial test set:\n" +
                "" + flights);

        FLightFilter fLightFilter = new FLightFilter(flights);
        // 1. Исключить перелеты с вылетом до текущего момента времени
        List<Flight> flightsAfterNow = fLightFilter.getFlightsWithDepartureAfterInstant(LocalDateTime.now());
        System.out.println("Flights with departure time after now:\n" +
                "" + flightsAfterNow);

        // 2. Исключить перелеты, имеющие сегменты с датой прилёта раньше даты вылета
        List<Flight> validFlights = fLightFilter.getFlightsWithDepartureBeforeArrival();
        System.out.println("Flights without segments where arrival time is before departure time: \n" +
                "" + validFlights);

        // 3. Исключить перелеты, в которых общее время, проведённое на земле, превышает два часа
        List<Flight> flightsWithGroundTimeNotLongerThanTwoHours =
                fLightFilter.getFlightsWithOverallGroundTimeNotLongerThan(Duration.of(2, ChronoUnit.HOURS));
        System.out.println("Flights with overall ground time not longer than two hours:\n" +
                "" + flightsWithGroundTimeNotLongerThanTwoHours);
    }
}
