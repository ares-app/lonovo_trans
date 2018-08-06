package org.ares.app.trans.daos;

import org.ares.app.trans.entities.Caruser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CaruserDao extends JpaRepository<Caruser, String> {

}
