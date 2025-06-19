package org.luvin.codiblyserver.dto;

/**
 * Represents a summary of weekly weather forecast.
 */
public record WeeklySummaryResponseDto(
        // Average atmospheric pressure (in hPa)
        double averagePressure,

        // Average sunshine duration (in hours)
        double averageSunshineHours,

        // Minimum temperature (in °C)
        double minTemperature,

        // Maximum temperature (in °C)
        double maxTemperature,

        // Overall summary for the week: "precipitation" or "no precipitation"
        String summary
) {
}
