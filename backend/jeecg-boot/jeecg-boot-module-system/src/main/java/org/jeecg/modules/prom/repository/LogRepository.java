package org.jeecg.modules.prom.repository;
import org.jeecg.modules.prom.entity.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogRepository extends JpaRepository<Log, Integer> {
}
