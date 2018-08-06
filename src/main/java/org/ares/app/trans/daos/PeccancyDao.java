package org.ares.app.trans.daos;

import java.util.List;

import org.ares.app.trans.entities.Peccancy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeccancyDao extends JpaRepository<Peccancy, Integer> {

	List<Peccancy> findByCarnumber(String carnumber);
}
