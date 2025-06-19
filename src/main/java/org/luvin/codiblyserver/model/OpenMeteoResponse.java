package org.luvin.codiblyserver.model;

import lombok.Data;

import java.util.List;

@Data
public class OpenMeteoResponse {

    // Hourly forecast arrays
    private Hourly hourly;

    @Data
    public static class Hourly {
        // Atmospheric pressure per hour for each day (in hPa)
        private List<Double> pressure_msl;
    }

    // Daily forecast arrays
    private Daily daily;

    @Data
    public static class Daily {
        // Dates in ISO format (e.g. "2025-06-19")
        private List<String> time;

        // Maximum temperature (in °C)
        private List<Double> temperature_2m_max;

        // Minimum temperature (in °C)
        private List<Double> temperature_2m_min;

        // Numeric weather codes from Open-Meteo
        private List<Integer> weather_code;

        // Sunshine duration (in seconds)
        private List<Double> sunshine_duration;
    }
}