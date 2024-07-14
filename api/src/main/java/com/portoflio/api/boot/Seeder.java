package com.portoflio.api.boot;

import com.portoflio.api.dao.UsersRepository;
import com.portoflio.api.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class Seeder implements CommandLineRunner {

    @Autowired
    UsersRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Override
    public void run(String... args) {
        /*


        Users user = new Users("user@prueba","Nombre","Apellidos","Dirección 1","Dirección 2","Ciudad","11130","643786543", pass, false);


        userRepository.save(user);

        */
        String pass = bCryptPasswordEncoder.encode("password");
        Users admin = new Users("admin", pass, true);
        userRepository.save(admin);
    }


}