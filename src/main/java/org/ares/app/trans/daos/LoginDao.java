package org.ares.app.trans.daos;

import org.ares.app.trans.entities.Loginuser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginDao extends JpaRepository<Loginuser, String> {
	Loginuser findByUsernameAndUserpwd(String username,String userpwd);
}
