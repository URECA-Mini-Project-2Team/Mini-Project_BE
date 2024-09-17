package kr.co.ureca.service;

import kr.co.ureca.dto.CustomeUserDetails;
import kr.co.ureca.entity.User;
import kr.co.ureca.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomeUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User userData = userRepository.findByUserName(userName);
        if(userData != null){
            return new CustomeUserDetails(userData);
        }
        throw new UsernameNotFoundException("User not found with username: " + userName);
    }
}
