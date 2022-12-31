package ru.fa.fireAlerts.api.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.fa.fireAlerts.api.model.Fire;

public interface FireRepository extends JpaRepository<Fire, Long> {
}
