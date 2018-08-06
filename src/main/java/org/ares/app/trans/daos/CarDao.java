package org.ares.app.trans.daos;

import org.ares.app.trans.entities.Carinfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarDao extends JpaRepository<Carinfo, String> {

}
