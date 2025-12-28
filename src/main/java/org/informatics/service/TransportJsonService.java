package org.informatics.service;

import org.informatics.dao.TransportDao;
import org.informatics.dto.TransportJsonDto;
import org.informatics.entity.Transport;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class TransportJsonService {

    public static void exportToJson(String filePath) {
        List<TransportJsonDto> dtos = TransportDao.getAll()
                .stream()
                .map(TransportJsonService::toDto)
                .collect(Collectors.toList());

        String json = toJsonArray(dtos);

        try {
            Files.writeString(Path.of(filePath), json);
        } catch (IOException e) {
            throw new RuntimeException("JSON export failed", e);
        }
    }

    public static String readJson(String filePath) {
        try {
            return Files.readString(Path.of(filePath));
        } catch (IOException e) {
            throw new RuntimeException("JSON read failed", e);
        }
    }

    // ===================== helpers =====================

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

    private static String toJsonArray(List<TransportJsonDto> list) {
        return list.stream()
                .map(TransportJsonService::toJsonObject)
                .collect(Collectors.joining(",\n", "[\n", "\n]"));
    }

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

    private static String safe(String s) {
        return s == null ? "" : s.replace("\"", "'");
    }
}
