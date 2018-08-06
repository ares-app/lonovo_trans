package org.ares.app.common.osrv.dto;

import org.ares.app.common.osrv.user.Privilege;
import org.ares.app.common.osrv.user.User;


public class UserFormDto extends UserDto {
    private static final long serialVersionUID = 7959857016962260738L;

    private String password;

    public UserFormDto() {
    }


    public Privilege[] getAllPrivileges() {
        return new Privilege[]{Privilege.MOBILE, Privilege.UNITY};
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User newUser() {
        final User user = new User()
                .username(getUsername())
                .phone(getPhone())
                .email(getEmail())
                .password("cc");
        user.privileges().addAll(getPrivileges());
        return user;
    }
}
