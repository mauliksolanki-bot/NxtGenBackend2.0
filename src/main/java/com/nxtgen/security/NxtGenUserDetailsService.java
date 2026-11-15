package com.nxtgen.security;

import com.nxtgen.repository.NxtGenUserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NxtGenUserDetailsService implements UserDetailsService {

    private final NxtGenUserRepository userRepository;

    public NxtGenUserDetailsService(NxtGenUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.withUsername(user.getEmpEmail())
                .password(user.getHashPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + user.getRoleName()))
                .disabled(!"Y".equals(user.getIsActive()))
                .build();
    }
}
