package org.luvin.codiblyserver.service;

import lombok.RequiredArgsConstructor;
import org.luvin.codiblyserver.client.OpenMeteoClient;
import org.luvin.codiblyserver.dto.ForecastRequestDto;
import org.luvin.codiblyserver.dto.ForecastResponseDto;
import org.luvin.codiblyserver.dto.WeeklySummaryResponseDto;
import org.luvin.codiblyserver.model.OpenMeteoResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForecastService {

    private final OpenMeteoClient openMeteoClient;

    private final double INSTALLATION_POWER_KW = 2.5;
    private final double PANEL_EFFICIENCY = 0.2;

    /**
     * Retrieves a 7-day weather forecast and maps it to a simplified list of daily weather summaries.
     *
     * @param request the user's geographic coordinates
     * @return list of weather forecasts for each day
     */
    public List<ForecastResponseDto> getWeeklyForecast(ForecastRequestDto request) {
        OpenMeteoResponse response = openMeteoClient.fetchForecast(request);
        OpenMeteoResponse.Daily daily = response.getDaily();

        List<ForecastResponseDto> result = new ArrayList<>();

        // Loop through each day in the forecast and construct our internal response DTO
        for (int i = 0; i < daily.getTime().size(); i++) {
            double sunshineDurationHours = daily.getSunshine_duration().get(i) / 3600;
            double energyKWh = INSTALLATION_POWER_KW * sunshineDurationHours * PANEL_EFFICIENCY;

            result.add(new ForecastResponseDto(
                    // date
                    LocalDate.parse(daily.getTime().get(i)),
                    // weather code
                    daily.getWeather_code().get(i),
                    // minimum temperature
                    daily.getTemperature_2m_min().get(i),
                    // maximum temperature
                    daily.getTemperature_2m_max().get(i),
                    // calculated energy production
                    energyKWh
            ));
        }

        return result;
    }

    /**
     * Aggregates a weekly weather summary based on the 7-day forecast.
     *
     * @param request the user's geographic coordinates
     * @return summary statistics for the upcoming week including pressure, sunshine, temperatures, and precipitation status
     */
    public WeeklySummaryResponseDto getWeeklySummary(ForecastRequestDto request) {
        OpenMeteoResponse response = openMeteoClient.fetchForecast(request);
        OpenMeteoResponse.Hourly hourly = response.getHourly();
        OpenMeteoResponse.Daily daily = response.getDaily();

        List<Double> pressures = hourly.getPressure_msl();
        List<Double> sunshineDurations = daily.getSunshine_duration();
        List<Double> minTemps = daily.getTemperature_2m_min();
        List<Double> maxTemps = daily.getTemperature_2m_max();
        List<Integer> weatherCodes = daily.getWeather_code();

        // Average atmospheric pressure (in hPa)
        double avgPressure = pressures.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0);

        // Average sunshine duration (converted from seconds to hours)
        double avgSunshineHours = sunshineDurations.stream()
                .mapToDouble(seconds -> seconds / 3600.0)
                .average()
                .orElse(0);

        // Minimum and maximum temperatures (in °C)
        double minTemperature = minTemps.stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0);
        double maxTemperature = maxTemps.stream()
                .mapToDouble(Double::doubleValue)
                .max()
                .orElse(0);

        // Days with precipitation based on weather code
        long daysWithPrecipitation = weatherCodes.stream()
                .filter(this::isPrecipitation)
                .count();

        // If 4 or more days have precipitation, summarize the week as "precipitation"
        String summary = (daysWithPrecipitation >= 4) ? "precipitation" : "no precipitation";

        return new WeeklySummaryResponseDto(
                avgPressure,
                avgSunshineHours,
                minTemperature,
                maxTemperature,
                summary
        );
    }

    /**
     * Helper method to check if a weather code indicates precipitation.
     */
    private boolean isPrecipitation(int weatherCode) {
        return switch (weatherCode) {
            case 51, 53, 55, 61, 63, 65, 80, 81, 82, 95, 96, 99 -> true;
            default -> false;
        };
    }
}
