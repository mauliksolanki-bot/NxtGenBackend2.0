package com.nxtgen.services;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nxtgen.model.NxtGenEducationUpdate;
import com.nxtgen.model.NxtGenProfileImage;
import com.nxtgen.model.NxtGenProfileImageUpdate;
import com.nxtgen.model.NxtGenUser;
import com.nxtgen.repository.NxtGenUserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@Service public class NxtGenProfileService { private final NxtGenUserRepository users; private final ObjectMapper objectMapper; public NxtGenProfileService(NxtGenUserRepository users,ObjectMapper objectMapper){this.users=users;this.objectMapper=objectMapper;} public NxtGenUser profile(String email){return users.findByEmailIgnoreCase(email).orElseThrow(()->new UsernameNotFoundException("User not found"));} public NxtGenProfileImage profileImage(String email){return new NxtGenProfileImage(profile(email).getProfileImage());} public NxtGenUser updateEducation(String email,NxtGenEducationUpdate update){var user=profile(email);try{users.updateEducation(user.getId().toString(),objectMapper.writeValueAsString(update.getEducation()),email);return profile(email);}catch(JsonProcessingException exception){throw new IllegalArgumentException("Education details must be valid JSON.",exception);}} public NxtGenUser updateProfileImage(String email,NxtGenProfileImageUpdate update){var user=profile(email);users.updateProfileImage(user.getId().toString(),update.getProfileImage(),email);return profile(email);} }
