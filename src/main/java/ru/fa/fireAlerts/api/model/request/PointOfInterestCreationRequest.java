package ru.fa.fireAlerts.api.model.request;

import lombok.Data;

@Data
public class PointOfInterestCreationRequest {
    private double latitude;
    private double longitude;
    private double radius;
}
