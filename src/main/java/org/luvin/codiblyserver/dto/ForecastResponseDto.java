package org.luvin.codiblyserver.dto;

import java.time.LocalDate;

/**
 * Represents a single day of weather forecast.
 */
public record ForecastResponseDto(
        // Forecasted date
        LocalDate date,

        // Numeric weather code from Open-Meteo
        int weatherCode,

        // Minimum temperature (in °C)
        double minTemperature,

        // Maximum temperature (in °C)
        double maxTemperature,

        // Estimated energy production (in kWh)
        double energyKWh
) {
}
