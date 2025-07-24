package com.mx.lacomer.usuarios_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SepomexService {

    @Value("${copomex.api.base-url}")
    private String copomexApiBaseUrl;

    @Value("${copomex.api.token}")
    private String apiToken;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> getAddressInfo(String codigoPostal) throws IOException {
        String url = copomexApiBaseUrl + codigoPostal + "?type=simplified&token=" + apiToken;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                if (response.code() == 404) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Código postal no válido o no encontrado en Copomex: " + codigoPostal);
                } else if (response.code() == 401) {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token de Copomex inválido o expirado. Código: " + response.code() + ", Mensaje: " + response.message());
                } else {
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al consultar Copomex. Código: " + response.code() + ", Mensaje: " + response.message());
                }
            }

            String responseBody = response.body().string();
            if (responseBody == null || responseBody.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NO_CONTENT, "La API de Copomex no devolvió contenido para el código postal: " + codigoPostal);
            }

            JsonNode rootNode = objectMapper.readTree(responseBody);


            if (rootNode.has("error") && rootNode.get("error").asBoolean()) {
                String errorMessage = rootNode.has("error_message") ? rootNode.get("error_message").asText() : "Error desconocido de Copomex";
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error de la API de Copomex: " + errorMessage);
            }

            JsonNode responseNode = rootNode.get("response");
            if (responseNode == null || responseNode.isNull() || responseNode.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró información de dirección en la respuesta de Copomex para el código postal: " + codigoPostal);
            }


            List<Map<String, Object>> resultList = new ArrayList<>();
            Map<String, Object> addressInfo = new HashMap<>();


            addressInfo.put("cp", responseNode.get("cp") != null ? responseNode.get("cp").asText() : null);
            addressInfo.put("tipo_asentamiento", responseNode.get("tipo_asentamiento") != null ? responseNode.get("tipo_asentamiento").asText() : null);
            addressInfo.put("municipio", responseNode.get("municipio") != null ? responseNode.get("municipio").asText() : null);
            addressInfo.put("estado", responseNode.get("estado") != null ? responseNode.get("estado").asText() : null);
            addressInfo.put("ciudad", responseNode.get("ciudad") != null ? responseNode.get("ciudad").asText() : null);
            addressInfo.put("pais", responseNode.get("pais") != null ? responseNode.get("pais").asText() : null);


            JsonNode asentamientoNode = responseNode.get("asentamiento");
            if (asentamientoNode != null && asentamientoNode.isArray()) {
                for (JsonNode node : asentamientoNode) {
                    Map<String, Object> singleAsentamientoInfo = new HashMap<>(addressInfo); // Copiar la info base
                    singleAsentamientoInfo.put("asentamiento", node.asText()); // Añadir el asentamiento específico
                    resultList.add(singleAsentamientoInfo);
                }
            } else if (responseNode.get("asentamiento") != null) {

                Map<String, Object> singleAsentamientoInfo = new HashMap<>(addressInfo);
                singleAsentamientoInfo.put("asentamiento", responseNode.get("asentamiento").asText());
                resultList.add(singleAsentamientoInfo);
            } else {

                resultList.add(addressInfo);
            }


            if (resultList.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró información de asentamientos válida para el código postal: " + codigoPostal);
            }

            return resultList;


        } catch (IOException e) {
            throw e;
        }
    }
}