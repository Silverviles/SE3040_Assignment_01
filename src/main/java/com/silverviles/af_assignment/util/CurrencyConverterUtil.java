package com.silverviles.af_assignment.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class CurrencyConverterUtil {
    @Value("${currency.api.url}")
    private String API_URL;
    @Value("${currency.api.key}")
    private String API_KEY;

    public double convertCurrency(double amount, String fromCurrency, String toCurrency) throws Exception {
        URI uri = new URI(API_URL + "?apikey=" + API_KEY + "&currencies=" + fromCurrency + "," + toCurrency);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        connection.disconnect();

        // Parse the JSON response
        ObjectMapper mapper = new ObjectMapper();
        Map responseMap = mapper.readValue(content.toString(), Map.class);
        Map<String, Double> rates = (Map<String, Double>) responseMap.get("data");

        // Calculate the conversion
        double fromRate = rates.get(fromCurrency);
        double toRate = rates.get(toCurrency);
        return amount * (toRate / fromRate);
    }
}
