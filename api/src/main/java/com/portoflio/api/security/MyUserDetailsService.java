package com.portoflio.api.security;

import com.portoflio.api.dao.UsersRepository;
import com.portoflio.api.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        System.out.println("MyUserDetailsService: loadUserByUsername");
        Optional<Users> user = usersRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        return new MyUserPrincipal(user.get());
    }
}