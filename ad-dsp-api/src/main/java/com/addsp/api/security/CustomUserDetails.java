package com.addsp.api.security;

import com.addsp.common.constant.PartnerStatus;
import com.addsp.domain.partner.entity.Partner;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Spring Security UserDetails implementation for Partner authentication.
 */
@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final Long partnerId;
    private final String email;
    private final String password;
    private final PartnerStatus status;

    public static CustomUserDetails from(Partner partner) {
        return new CustomUserDetails(
                partner.getId(),
                partner.getEmail(),
                partner.getPassword(),
                partner.getStatus()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_PARTNER"));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isEnabled() {
        return status == PartnerStatus.ACTIVE;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status != PartnerStatus.WITHDRAWN;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status != PartnerStatus.SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
