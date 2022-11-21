package org.zerock.ex01.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex01.entity.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {

}
