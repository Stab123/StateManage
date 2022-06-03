package org.jeecg.modules.prom.repository;

import org.jeecg.modules.prom.entity.ClientLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientLogRepository extends JpaRepository<ClientLog, Integer> {
}
