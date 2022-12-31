package ru.fa.fireAlerts.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="fire", uniqueConstraints = { @UniqueConstraint(columnNames = { "longitude", "latitude" }) })
 public class Fire {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private double longitude;
        private double latitude;
        private String countryId;
        private double brightness;
        private String satellite;
        @ManyToMany(mappedBy = "firesInRange")
        @JsonManagedReference
        private List<PointOfInterest> inRangeOf;
        private String instrument; // MODIS or VIIRS
        private Date firms_acq_time;
        private Date alertsAcquisitionDate;

    }
