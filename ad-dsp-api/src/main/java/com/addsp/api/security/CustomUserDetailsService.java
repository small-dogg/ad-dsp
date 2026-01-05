package com.addsp.api.security;

import com.addsp.domain.partner.entity.Partner;
import com.addsp.domain.partner.repository.PartnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security UserDetailsService implementation for Partner authentication.
 * Loads partner by email for authentication.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final PartnerRepository partnerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Partner partner = partnerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Partner not found with email: " + email));
        return CustomUserDetails.from(partner);
    }
}
