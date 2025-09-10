package com.PostGreSQL.springSecurity;

import com.PostGreSQL.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository registrationRepository;


    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.PostGreSQL.model.User> data = registrationRepository.findByEmail(username);

        if (data.isPresent()) {
            com.PostGreSQL.model.User gg = data.get();

            // Ensure you retrieve roles dynamically
            // For example, if roles are stored as a list in Employee:
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();


            authorities.add(new SimpleGrantedAuthority("SNVN"));

            return new User(username, gg.getName(), authorities);  // Returning user with roles
        }
        throw new UsernameNotFoundException("User not found");
    }

}

