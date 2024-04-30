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
        Users admin = new Users("admin", pass, true);
        Users admin2 = new Users("admin2", "password", true);
        //Users testUser = new Users(1L,"carol", "apellido", pass, true);
        //Users testUser2 = new Users(2L,"noad","apellido", "{noop}password", false);
        //Users testUser3 = new Users("a@gmail.com", "{noop}password", false);
        userRepository.save(admin);
        userRepository.save(admin2);
        //userRepository.save(testUser);
        //userRepository.save(testUser2);

    }
}