package ru.fa.fireAlerts.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="poi")
public class PointOfInterest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "fires_in_range",
            joinColumns = @JoinColumn(name = "area_id"),
            inverseJoinColumns = @JoinColumn(name = "fire_id"))
    private List<Fire> firesInRange;

    @OneToMany(mappedBy = "area",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Alert> alerts;

    private double longitude;
    private double latitude;
    private double range; // in km
    private static double EARTH_EQUATORIAL_RADIUS = 6378.137;
    private static double EARTH_POLAR_RADIUS = 6356.7523142;
    private static double EARTH_ECCENTRICITY_SQUARED = 0.00669437999014;

    /**
     * Normalize and convert latitude to radians.
     *
     * @param latitude Latitude of the point.
     * @return Returns normalized latitude in radians.
     */
    private double formatLatitude(double latitude) {
        if (Math.abs(latitude) > 90) {
            latitude = ((latitude + 90) % 180) - 90;
        }
        return Math.toRadians(latitude);
    }

    /**
     * Normalize and convert longitude to radians.
     *
     * @param longitude Longitude of the point.
     * @return Returns normalized longitude in radians.
     */
    private double formatLongitude(double longitude) {
        if (Math.abs(longitude) > 180) {
            longitude = ((longitude + 180) % 360) - 180;
        }
        return Math.toRadians(longitude);
    }

    /**
     * Returns latitude degree length in kilometers.
     *
     * @param latitude_degree getting Latitude to calculate
     *                        the sin and cos of the angle.
     * @return Return values:
     * double[0] - latitude length
     * double[1] - longitude length
     */
    private double[] getDegreeLength(double latitude_degree) {
        latitude_degree = this.formatLatitude(latitude_degree);

        double conversion = 1.0; // set for kilometers.

        double latitude_length = (Math.PI * EARTH_EQUATORIAL_RADIUS
                * (1.0 - EARTH_ECCENTRICITY_SQUARED)) / (180.0
                * Math.pow(1 - (EARTH_ECCENTRICITY_SQUARED
                * Math.pow(Math.sin(latitude_degree), 2)), 1.5));

        double longitude_length = (Math.PI * EARTH_EQUATORIAL_RADIUS
                * Math.cos(latitude_degree)) / (180.0
                * Math.pow(1 - (EARTH_ECCENTRICITY_SQUARED
                * Math.pow(Math.sin(latitude_degree), 2)), 0.5));

        return new double[]{latitude_length, longitude_length}; // eucl wise is {x,y}
    }

    public boolean isFireInRadius(Fire fire){
        double h= this.latitude;
        double k= this.longitude;
        double[] ab = this.getDegreeLength(this.latitude);
        double a = ab[0];
        double b = ab[1];
        double x= fire.getLatitude();
        double y= fire.getLongitude();
        boolean inRange = false;
        if ((Math.pow(x-h,2)/Math.pow(a,2))
                + (Math.pow(y-k,2)/Math.pow(b,2)) <=1 ){
            inRange = true;
        }
        LocalDate today = LocalDate.now();
        if (fire.getFirms_acq_time()
                .before(Date.from(today
                        .atStartOfDay(ZoneId.systemDefault()).toInstant()))){
            inRange = false;
        }
        return inRange;


    }

}
