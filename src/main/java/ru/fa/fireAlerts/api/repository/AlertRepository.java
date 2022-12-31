package ru.fa.fireAlerts.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.fa.fireAlerts.api.model.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}
