package com.paf.issuetracker.security;

import com.paf.issuetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return User.builder()
               .username(user.getUsername())
               .password(user.getPassword())
               .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())))
               .accountLocked(!user.isActive())
               .disabled(!user.isActive())
               .build();
    }
}
