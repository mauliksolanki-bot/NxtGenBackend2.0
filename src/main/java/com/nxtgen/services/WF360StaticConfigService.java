package com.WF360.services;

import com.WF360.model.WF360StaticConfig;
import com.WF360.repository.WF360StaticConfigRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WF360StaticConfigService {

  @Autowired private WF360StaticConfigRepository staticConfigRepository;

  public Optional<WF360StaticConfig> findActiveByCode(String confCode) {
    return staticConfigRepository.findActiveByCode(confCode);
  }
}
