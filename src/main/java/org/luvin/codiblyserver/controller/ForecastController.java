package org.luvin.codiblyserver.controller;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.luvin.codiblyserver.dto.ForecastRequestDto;
import org.luvin.codiblyserver.dto.ForecastResponseDto;
import org.luvin.codiblyserver.dto.WeeklySummaryResponseDto;
import org.luvin.codiblyserver.service.ForecastService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/forecast")
@RequiredArgsConstructor
@Validated
public class ForecastController {

    private final ForecastService forecastService;

    /**
     * Returns a 7-day weather forecast.
     *
     * @param lat latitude of the location
     * @param lon longitude of the location
     * @return list of daily forecast summaries
     */
    @GetMapping("/weekly")
    public List<ForecastResponseDto> getWeeklyForecast(
            @RequestParam @NotNull Double lat,
            @RequestParam @NotNull Double lon
    ) {
        ForecastRequestDto request = new ForecastRequestDto(lat, lon);
        return forecastService.getWeeklyForecast(request);
    }

    /**
     * Returns a weekly summarized weather overview.
     *
     * @param lat latitude of the location
     * @param lon longitude of the location
     * @return weekly weather summary
     */
    @GetMapping("/summary/weekly")
    public WeeklySummaryResponseDto getWeeklySummary(
            @RequestParam @NotNull Double lat,
            @RequestParam @NotNull Double lon
    ) {
        ForecastRequestDto request = new ForecastRequestDto(lat, lon);
        return forecastService.getWeeklySummary(request);
    }
}
