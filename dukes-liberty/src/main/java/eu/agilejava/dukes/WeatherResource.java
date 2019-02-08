/*
 * The MIT License
 *
 * Copyright 2019 Kevin Sutter (kwsutter@gmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package eu.agilejava.dukes;

import eu.agilejava.dukes.weather.WeatherService;
import java.io.StringReader;
import java.lang.String;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.rest.client.inject.RestClient;

/**
 *
 * @author Kevin Sutter (kwsutter@gmail.com)
 */
@Path("/warmHello")
@RequestScoped
public class WeatherResource {

    @Inject
    @ConfigProperty(name = "dukes.zipcode", defaultValue = "94065")
    private String dukesZipcode;

    @Inject
    @ConfigProperty(name = "openweathermap.appid", defaultValue = "")
    private String owmAppid;

    @Inject
    @ConfigProperty(name = "openweathermap.units", defaultValue = "imperial")  // Fahrenheit
    private String owmUnits;

    @Inject
    @RestClient
    private WeatherService weatherService;

    @Metered
    @GET
    @Produces("text/plain")
    public Response helloWithTemp() {

        int temp = getCurrentTemp();
        return Response.ok("Hello " + dukesZipcode + "!  The current tempature is " + temp + " degrees Fahrenheit!").build();
    }


    public int getCurrentTemp() {

        Response currentWeather = weatherService.weather(dukesZipcode, owmUnits, owmAppid);

        String jsonString = currentWeather.readEntity(String.class);
        JsonReader reader = Json.createReader(new StringReader(jsonString));
        JsonObject jsonObject = reader.readObject();
        int temp = jsonObject.getJsonObject("main").getInt("temp");
        return temp;

    }
}
