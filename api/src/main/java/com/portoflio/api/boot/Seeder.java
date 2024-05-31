package com.portoflio.api.boot;

import com.portoflio.api.dao.UsersRepository;
import com.portoflio.api.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    UsersRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Override
    public void run(String... args) {
        String pass = bCryptPasswordEncoder.encode("password");
        Optional<Users> oAdmin = userRepository.findByEmail("admin");
        if(!oAdmin.isPresent()){
            Users admin = new Users("admin", pass, true);
            userRepository.save(admin);
        }
    }
}