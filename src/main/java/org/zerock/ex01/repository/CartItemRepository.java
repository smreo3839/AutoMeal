package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex01.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartIdAndProductProductId(Long cartId,Long productId);

}
