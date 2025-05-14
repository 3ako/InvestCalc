package ru.mslotvi.http;

import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;

@UtilityClass
public class HttpRequestService {

    private final HttpClient client = HttpClient.newHttpClient();

    /**
     * Метод для отправки GET запроса.
     * @param url строка URL для запроса
     * @return строка с результатом ответа
     * @throws IOException если произошла ошибка при отправке запроса
     * @throws InterruptedException если запрос был прерван
     */
    public String sendGetRequest(URI url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("Error: Received non-OK response from server. Status code: " + response.statusCode());
            }
        } catch (HttpTimeoutException e) {
            throw new IOException("Request timed out", e);
        }
    }

    /**
     * Метод для отправки POST запроса с телом запроса.
     * @param url строка URL для запроса
     * @param requestBody тело запроса
     * @return строка с результатом ответа
     * @throws IOException если произошла ошибка при отправке запроса
     * @throws InterruptedException если запрос был прерван
     */
    public String sendPostRequest(String url, String requestBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                throw new IOException("Error: Received non-OK response from server. Status code: " + response.statusCode());
            }
        } catch (HttpTimeoutException e) {
            throw new IOException("Request timed out", e);
        }
    }
}
