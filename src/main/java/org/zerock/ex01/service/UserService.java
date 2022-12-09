package org.zerock.ex01.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.repository.UserRepository;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PayMentManager payMentManager;

    //회원 생성하기
    public User create(final User userEntity) {
        if (userEntity == null || userEntity.getUserEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }
        final String userEmail = userEntity.getUserEmail();

        if (userRepository.existsByUserEmail(userEmail)) {
            log.warn("Username already exists {}", userEmail);
            throw new RuntimeException("User already exists");
        }
        return userRepository.save(userEntity);
    }

    public User getByCredentials(final String userEmail) {
        final User originalUser = userRepository.findByUserEmail(userEmail);
        log.info("로그인해볼게용");
        if (originalUser != null) {
            return originalUser;
        }
        return null;
    }

    public User updateUser(final User userEntity, final String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        log.info("user:{}", userEntity);
        user.updateUser(userEntity);
        userRepository.save(user);
        return user;
    }

    public User setDiet(final String diet, final String userEmail) {
        User user = userRepository.findByUserEmail(userEmail);
        user.changeDiet(diet);
        userRepository.save(user);
        return user;
    }

    public Map<String, String> phoneNumberWithApi(String imp_uid) throws IOException, ParseException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone_num", payMentManager.CertificationCheck(payMentManager.getToken02(), imp_uid));
        return map;
    }

}
