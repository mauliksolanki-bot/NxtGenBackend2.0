package com.nxtgen.services;

import com.nxtgen.model.NxtGenNavigation;
import com.nxtgen.repository.NxtGenNavigationRepository;
import com.nxtgen.repository.NxtGenUserRepository;
import java.util.List;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NxtGenNavigationService {

    private final NxtGenUserRepository userRepository;
    private final NxtGenNavigationRepository navigationRepository;

    public NxtGenNavigationService(
            NxtGenUserRepository userRepository,
            NxtGenNavigationRepository navigationRepository) {
        this.userRepository = userRepository;
        this.navigationRepository = navigationRepository;
    }

    public List<NxtGenNavigation> findNavigationForUser(String email) {
        var user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return navigationRepository.findActiveByUserId(user.getId().toString());
    }
}
