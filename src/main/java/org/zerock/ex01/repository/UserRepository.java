package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex01.entity.User;

public interface UserRepository extends JpaRepository<User, String> {//Entitu 타입 정보와 기본키의 자료형 지정
    //인터페이스 선언만으로 자동으로 스프링 빈으로 등록
}
