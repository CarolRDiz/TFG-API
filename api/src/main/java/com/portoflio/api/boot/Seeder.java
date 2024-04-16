package com.portoflio.api.boot;

import com.portoflio.api.dao.UsersRepository;
import com.portoflio.api.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    UsersRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Override
    public void run(String... args) {
        String pass = bCryptPasswordEncoder.encode("password");
        Users testUser = new Users(1L,"carol", "apellido", pass, true);
        Users testUser2 = new Users(2L,"noad","apellido", "{noop}password", false);
        userRepository.save(testUser);
        userRepository.save(testUser2);

    }
}