package org.zerock.ex01.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex01.entity.OrderProduct;

public interface OrderProductRepository extends JpaRepository<OrderProduct,Long> {
}
