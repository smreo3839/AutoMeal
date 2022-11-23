package org.zerock.ex01.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.ex01.dto.CartDetailDTO;
import org.zerock.ex01.entity.CartItem;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    CartItem findByCartIdAndProductProductId(Long cartId,Long productId);

   @Query("select new org.zerock.ex01.dto.CartDetailDTO(ci.id,i.productName,i.price,ci.count,i.imageFirst)"
   +"from CartItem ci join ci.product i where ci.cart.id=:cartId")
    List<CartDetailDTO> findCartDetailDtoList(Long cartId);

}
