package com.WF360.services;

import com.WF360.model.WF360Navigation;
import com.WF360.repository.WF360NavigationRepository;
import com.WF360.repository.WF360UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WF360NavigationService {

    @Autowired
    private WF360UserRepository userRepository;

    @Autowired
    private WF360NavigationRepository navigationRepository;

    public List<WF360Navigation> findNavigationForUser(String email) {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return navigationRepository.findActiveByUserId(user.getId().toString());
    }
}
