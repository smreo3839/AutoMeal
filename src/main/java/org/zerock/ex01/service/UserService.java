package org.zerock.ex01.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.repository.UserRepository;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //회원 생성하기
    public User create(final User userEntity) {
        if (userEntity == null || userEntity.getUserEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String userEmail = userEntity.getUserEmail();

        if(userRepository.existsByUserEmail(userEmail)){
            log.warn("Username already exists {}", userEmail);
            throw new RuntimeException("User already exists");
        }
        return userRepository.save(userEntity);
    }

    public User getByCredentials(final String userEmail) {
        final User originalUser = userRepository.findByUserEmail(userEmail);
        log.info("머지테스트");
        if (originalUser != null) {
            return originalUser;
        }
        return null;
    }

}
