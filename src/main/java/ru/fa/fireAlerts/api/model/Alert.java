package ru.fa.fireAlerts.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name="alert")
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "area_id")
    private PointOfInterest area;
    private Date generation_date;
    @Enumerated(EnumType.STRING)
    private AlertStatus status;
    public String message;
}
