package com.biblioteca_api.biblioteca.factories;

import com.biblioteca_api.biblioteca.entities.User;
import com.biblioteca_api.biblioteca.entities.UserRole;

public class UserFactory {
    public static User createValidUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("feijaocomarroz@gmail.com");
        user.setName("Kauanzinho");
        user.setPassword("12345");
        user.setRole(UserRole.READER);
        return user;
    }
}
