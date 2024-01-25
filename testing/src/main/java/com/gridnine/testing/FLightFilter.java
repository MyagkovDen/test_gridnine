package com.gridnine.testing;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class FLightFilter {

    private final List<Flight> flights;

    public FLightFilter(List<Flight> flights) {
        this.flights = flights;
    }


    public List<Flight> getFlightsWithDepartureAfterInstant(LocalDateTime dateTime) {
        return this.flights.stream().filter(e
                        -> e.getSegments().get(0).getDepartureDate()
                        .isAfter(dateTime))
                .toList();
    }

    public List<Flight> getFlightsWithDepartureBeforeArrival() {
        List<Flight> validFlights = new ArrayList<>();
        boolean isDepartureBeforeArrival;
        for (Flight flight : this.flights) {
            isDepartureBeforeArrival = true;
            for (Segment segment : flight.getSegments()) {
                if (segment.getDepartureDate().isAfter(segment.getArrivalDate())) {
                    isDepartureBeforeArrival = false;
                    break;
                }
            }
            if (isDepartureBeforeArrival) {
                validFlights.add(flight);
            }
        }
        return validFlights;
    }

    public List<Flight> getFlightsWithOverallGroundTimeNotLongerThan(Duration duration) {
        List<Flight> result = new ArrayList<>();
        for (Flight flight : this.flights) {
            Duration groundTime = Duration.of(0, ChronoUnit.SECONDS);
            int countSegmentsOnFlight = flight.getSegments().size();
            for (int i = 1; i < countSegmentsOnFlight; i++) {
                LocalDateTime onGroundFrom = flight.getSegments().get(i - 1).getArrivalDate();
                LocalDateTime onGroundTo = flight.getSegments().get(i).getDepartureDate();
                if (onGroundFrom.isBefore(onGroundTo)) {
                    Duration timeBetweenSegments = Duration.between(
                            onGroundFrom.toLocalTime(),
                            onGroundTo.toLocalTime()
                    );
                    groundTime = groundTime.plus(timeBetweenSegments);
                }
            }
            if (groundTime.compareTo(duration) <= 0) {
                result.add(flight);
            }
        }
        return result;
    }
}
