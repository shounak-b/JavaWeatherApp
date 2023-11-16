import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherApp {
    private static final String API_KEY = "caa3435d3bec029e3ae874ff53914ede";
    private static final String API_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the city name: ");
        String cityName = scanner.nextLine();

        try {
            String apiUrl = String.format(API_URL, cityName, API_KEY);
            String weatherData = getWeatherData(apiUrl);

            if (weatherData != null) {
                displayWeatherInfo(weatherData);
            } else {
                System.out.println("Failed to retrieve weather data. Please check the city name and API key.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static String getWeatherData(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            return response.toString();
        } else {
            return null;
        }
    }

    private static void displayWeatherInfo(String weatherData) {
        // Parse and display weather information
        // You can use a JSON library for parsing, such as org.json or Jackson
        // For simplicity, this example uses basic string manipulation
        String temperature = getValueFromJson(weatherData, "\"temp\":");
        String description = getValueFromJson(weatherData, "\"description\":\"");
        String cityName = getValueFromJson(weatherData, "\"name\":\"");

        System.out.println("Weather in " + cityName);
        System.out.println("Temperature: " + temperature + "°C");
        System.out.println("Description: " + description);
    }

    private static String getValueFromJson(String jsonData, String key) {
        int startIndex = jsonData.indexOf(key) + key.length();
        int endIndex = jsonData.indexOf("\"", startIndex);
        return jsonData.substring(startIndex, endIndex);
    }
}
