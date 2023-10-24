package com.no3.game.auth;

import com.no3.game.entity.Member;
import com.no3.game.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository userRepository;

    // 시큐리티 session = Authentication = UserDetails
    // 실행되면 ->
    // 시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member userEntity = userRepository.findByName(username)
                .orElseThrow(() -> new UsernameNotFoundException("오류 해결 좀."));

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new PrincipalDetails(userEntity);

    }
}
