package com.example;

import io.github.cdimascio.dotenv.Dotenv;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TripServiceApplicationTests {
    private static String baseUrl = "http://localhost:";

    private static String userServiceUrl = baseUrl;
    private static String tripServiceUrl = baseUrl;
    private static String notificationServiceUrl = baseUrl;

    private static String passengerToken;
    private static String driverToken;

    private static HttpClient httpClient;

    private static long passengerId;
    private static long driverId;
    private static long tripId;

    @BeforeAll
    public static void setUp() {
        Dotenv dotenv = Dotenv.configure()
                .directory("../")
                .load();

        userServiceUrl += dotenv.get("USER_SERVICE_PORT");
        tripServiceUrl += dotenv.get("TRIP_SERVICE_PORT");
        notificationServiceUrl += dotenv.get("NOTIFICATION_SERVICE_PORT");

        httpClient = HttpClient.newHttpClient();
    }

    @Test
    @Order(1)
    @DisplayName(value = "Регистрация пассажира")
    public void testRegisterPassenger() throws IOException, InterruptedException, JSONException {
        String requestBody = """
                {
                  "name": "danil",
                  "email": "danil@example.com",
                  "phone": "79351456730",
                  "password": "dfgdfg4332"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(userServiceUrl + "/api/passengers"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());
        passengerId = json.getLong("id");

        System.out.println("Пассажир зарегистрирован:");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(2)
    @DisplayName(value = "Логин пассажира")
    public void testLoginPassenger() throws IOException, InterruptedException, JSONException {
        String requestBody = """
                {
                  "email": "danil@example.com",
                  "password": "dfgdfg4332",
                  "role": "PASSENGER"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(userServiceUrl + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());
        passengerToken = json.getString("accessToken");

        System.out.println("\nПассажир залогинился");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(3)
    @DisplayName(value = "Регистрация водителя")
    public void testRegisterDriver() throws IOException, InterruptedException, JSONException {
        String requestBody = """
                {
                  "name": "ashot",
                  "email": "ashot@example.com",
                  "phone": "79361456730",
                  "licenseNumber": "12345678",
                  "password": "pushka0999"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(userServiceUrl + "/api/drivers"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());
        driverId = json.getLong("id");

        System.out.println("\nВодитель зарегистрирован:");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(4)
    @DisplayName(value = "Логин водителя")
    public void testLoginDriver() throws IOException, InterruptedException, JSONException {
        String requestBody = """
                {
                  "email": "ashot@example.com",
                  "password": "pushka0999",
                  "role": "DRIVER"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(userServiceUrl + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());
        driverToken = json.getString("accessToken");

        System.out.println("\nВодитель залогинился");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(5)
    @DisplayName(value = "Обновление статуса водителя на AVAILABLE")
    public void testUpdateDriverStatus() throws IOException, InterruptedException, JSONException {
        String requestBody = """
                {
                  "status": "AVAILABLE"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(userServiceUrl + String.format("/api/drivers/%d/status", driverId)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + driverToken)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());

        System.out.println("\nСтатус водителя поменялся");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(6)
    @DisplayName(value = "Создание поездки")
    public void testCreateTrip() throws IOException, InterruptedException, JSONException {
        String requestBody = String.format("""
                {
                  "passengerId": %d,
                  "origin": "Moscow",
                  "destination": "Novosibirsk",
                  "distance": 50,
                  "tariff": 2
                }
                """, passengerId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tripServiceUrl + "/api/trips"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + driverToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());
        tripId = json.getLong("id");

        System.out.println("\nПоездка создалась");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(7)
    @DisplayName(value = "Принятие поездки водителем")
    public void testAcceptTrip() throws IOException, InterruptedException, JSONException {
        String requestBody = """
                {
                  "status": "ACCEPTED"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tripServiceUrl + String.format("/api/trips/%d/status", tripId)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + driverToken)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());

        System.out.println("\nВодитель принял поездку");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(8)
    @DisplayName(value = "Старт поездки")
    public void testStartTrip() throws IOException, InterruptedException, JSONException {
        String requestBody = """
                {
                  "status": "STARTED"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tripServiceUrl + String.format("/api/trips/%d/status", tripId)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + driverToken)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());

        System.out.println("\nВодитель начал поездку");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(9)
    @DisplayName(value = "Завершение поездки")
    public void testFinishTrip() throws IOException, InterruptedException, JSONException {
        String requestBody = """
                {
                  "status": "COMPLETED"
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tripServiceUrl + String.format("/api/trips/%d/status", tripId)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + driverToken)
                .method("PATCH", HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());

        System.out.println("\nВодитель завершил поездку");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(10)
    @DisplayName(value = "Оценка поездки пассажиром")
    public void testRateTrip() throws IOException, InterruptedException, JSONException {
        String requestBody = """
                {
                  "rating": 5
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tripServiceUrl + String.format("/api/trips/%d/rate", tripId)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + passengerToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());

        System.out.println("\nПассажир оценил поездку");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(11)
    @DisplayName(value = "Просмотр уведомлений о поездке")
    public void getNotifications() throws IOException, InterruptedException, JSONException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(notificationServiceUrl + String.format("/api/notifications?trip_id=%d", tripId)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + passengerToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONArray json = new JSONArray(response.body());

        System.out.println("\nУведомления о поездке");
        System.out.println(json.toString(2));
    }

    @Test
    @Order(12)
    @DisplayName(value = "Просмотр статистики")
    public void getStatistic() throws IOException, InterruptedException, JSONException {
        String date = LocalDate.now().toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tripServiceUrl + String.format("/api/trips/statistic?date=%s", date)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + passengerToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

        JSONObject json = new JSONObject(response.body());

        System.out.println("\nCтатистика");
        System.out.println(json.toString(2));
    }
}
