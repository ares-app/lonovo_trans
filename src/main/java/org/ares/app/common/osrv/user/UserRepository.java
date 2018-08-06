package org.ares.app.common.osrv.user;

import java.util.List;

import org.ares.app.common.osrv.shared.Repository;


public interface UserRepository extends Repository {

    User findByGuid(String guid);

    void saveUser(User user);

    void updateUser(User user);

    User findByUsername(String username);

    List<User> findUsersByUsername(String username);
    
}