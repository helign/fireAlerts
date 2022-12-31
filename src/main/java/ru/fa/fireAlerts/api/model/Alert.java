package ru.fa.fireAlerts.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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
    @JsonManagedReference
    private PointOfInterest area;
    @CreationTimestamp
    private Date generation_date;
    @Enumerated(EnumType.STRING)
    private AlertStatus status;
    public String message;
}
