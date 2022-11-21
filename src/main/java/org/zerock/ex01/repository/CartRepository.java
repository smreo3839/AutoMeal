package org.zerock.ex01.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex01.entity.Cart;

public interface CartRepository extends JpaRepository<Cart,Long> {

}
