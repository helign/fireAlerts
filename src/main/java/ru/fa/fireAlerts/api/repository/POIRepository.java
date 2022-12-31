package ru.fa.fireAlerts.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fa.fireAlerts.api.model.PointOfInterest;

public interface POIRepository extends JpaRepository<PointOfInterest,Long> {
}
