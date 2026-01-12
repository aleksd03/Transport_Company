package org.informatics.service;

import org.informatics.dao.TransportDao;
import org.informatics.dto.TransportJsonDto;
import org.informatics.entity.Transport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for exporting and reading transport data in JSON format.
 */
public class TransportJsonService {

    /**
     * Exports all transports from the database to a JSON file.
     *
     * @param filePath path where the JSON file will be created
     * @throws RuntimeException if file writing fails
     */
    public static void exportToJson(String filePath) {
        // Fetch all transports and convert to DTOs
        List<TransportJsonDto> dtos = TransportDao.getAll()
                .stream()
                .map(TransportJsonService::toDto)
                .collect(Collectors.toList());

        // Convert to JSON string
        String json = toJsonArray(dtos);

        // Write to file
        try {
            Files.writeString(Path.of(filePath), json);
        } catch (IOException e) {
            throw new RuntimeException("JSON export failed", e);
        }
    }

    /**
     * Reads JSON content from a file.
     *
     * @param filePath path to the JSON file
     * @return JSON content as string
     * @throws RuntimeException if file reading fails
     */
    public static String readJson(String filePath) {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException("JSON read failed", e);
        }
    }

    // ===================== PRIVATE HELPER METHODS =====================

    /**
     * Converts a Transport entity to a TransportJsonDto.
     */
    private static TransportJsonDto toDto(Transport t) {
        return new TransportJsonDto(
                t.getId(),
                t.getClass().getSimpleName(),
                t.getTransportDate(),
                t.getDestination(),
                t.getPrice(),
                t.getPaymentStatus().name(),
                t.getDriver().getId(),
                t.getDriver().getFirstName() + " " + t.getDriver().getLastName(),
                t.getVehicle().getId(),
                t.getVehicle().getRegistrationNumber(),
                t.getClient().getId(),
                t.getClient().getFirstName() + " " + t.getClient().getLastName()
        );
    }

    /**
     * Converts a list of DTOs to a JSON array string.
     */
    private static String toJsonArray(List<TransportJsonDto> list) {
        return list.stream()
                .map(TransportJsonService::toJsonObject)
                .collect(Collectors.joining(",\n", "[\n", "\n]"));
    }

    /**
     * Converts a single DTO to a JSON object string.
     */
    private static String toJsonObject(TransportJsonDto d) {
        return """
                {
                  "id": %d,
                  "type": "%s",
                  "date": "%s",
                  "destination": "%s",
                  "price": %.2f,
                  "paymentStatus": "%s",
                  "driver": {
                    "id": %d,
                    "name": "%s"
                  },
                  "vehicle": {
                    "id": %d,
                    "registrationNumber": "%s"
                  },
                  "client": {
                    "id": %d,
                    "name": "%s"
                  }
                }
                """.formatted(
                d.getId(),
                d.getType(),
                d.getDate(),
                safe(d.getDestination()),
                d.getPrice(),
                d.getPaymentStatus(),
                d.getDriverId(),
                safe(d.getDriverName()),
                d.getVehicleId(),
                safe(d.getVehicleRegistration()),
                d.getClientId(),
                safe(d.getClientName())
        );
    }

    /**
     * Escapes quotes in strings for JSON safety.
     */
    private static String safe(String s) {
        return s == null ? "" : s.replace("\"", "'");
    }
}
