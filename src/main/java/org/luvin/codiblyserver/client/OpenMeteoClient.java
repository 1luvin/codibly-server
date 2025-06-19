package org.luvin.codiblyserver.client;

import lombok.RequiredArgsConstructor;
import org.luvin.codiblyserver.dto.ForecastRequestDto;
import org.luvin.codiblyserver.model.OpenMeteoResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class OpenMeteoClient {

    private final WebClient openMeteoWebClient;

    /**
     * Fetches weather forecast data from the Open-Meteo API for the given coordinates.
     *
     * @param request DTO containing latitude and longitude
     * @return structured weather response mapped to OpenMeteoResponse
     */
    public OpenMeteoResponse fetchForecast(ForecastRequestDto request) {
        return openMeteoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        // Required weather parameters: coordinates
                        .queryParam("latitude", request.latitude())
                        .queryParam("longitude", request.longitude())
                        // Daily weather parameters: max & min temperatures, weather code, sunshine duration
                        .queryParam("daily", "temperature_2m_max,temperature_2m_min,weather_code,sunshine_duration")
                        // Hourly weather parameters: pressure
                        .queryParam("hourly", "pressure_msl")
                        // Use local timezone
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                // Map JSON response into Java object
                .bodyToMono(OpenMeteoResponse.class)
                // Block to return response synchronously
                .block();
    }
}
