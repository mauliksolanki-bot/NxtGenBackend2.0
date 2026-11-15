package com.nxtgen.services;

import com.nxtgen.model.NxtGenStaticConfig;
import com.nxtgen.repository.NxtGenStaticConfigRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class NxtGenStaticConfigService {

    private final NxtGenStaticConfigRepository staticConfigRepository;

    public NxtGenStaticConfigService(NxtGenStaticConfigRepository staticConfigRepository) {
        this.staticConfigRepository = staticConfigRepository;
    }

    public Optional<NxtGenStaticConfig> findActiveByCode(String confCode) {
        return staticConfigRepository.findActiveByCode(confCode);
    }
}
