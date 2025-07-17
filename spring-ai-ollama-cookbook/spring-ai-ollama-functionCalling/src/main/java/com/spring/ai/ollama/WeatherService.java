package com.spring.ai.ollama;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.function.Function;

public class WeatherService implements Function<WeatherService.Request, WeatherService.Response> {

    @Override
    public Response apply(Request request) {

        double temperature = 0;
        if (request.location().contains("深圳")) {
            temperature = 15;
        }
        else if (request.location().contains("北京")) {
            temperature = 10;
        }
        else if (request.location().contains("广州")) {
            temperature = 30;
        }

        return new Response(temperature,  Unit.C);
    }

    /**
     * Temperature units.
     */
    public enum Unit {

        /**
         * Celsius.
         */
        C("摄氏度"),
        /**
         * Fahrenheit.
         */
        F("华氏度");

        public final String unitName;

        Unit(String text) {
            this.unitName = text;
        }

    }

    /**
     * Weather Function request.
     */
    @JsonInclude(Include.NON_NULL)
    @JsonClassDescription("Weather API request")
    public record Request(@JsonProperty(required = true,
            value = "location") @JsonPropertyDescription("城市名称，比如：北京、南京、深圳") String location,
                          @JsonProperty(required = true, value = "unit") @JsonPropertyDescription("Temperature unit") Unit unit) {

    }

    /**
     * Weather Function response.
     */
    public record Response(double temp, Unit unit) {

    }

}