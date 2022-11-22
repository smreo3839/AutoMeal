package org.zerock.ex01.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.dto.CartItemDTO;
import org.zerock.ex01.entity.Cart;
import org.zerock.ex01.entity.CartItem;
import org.zerock.ex01.entity.ProductEntity;
import org.zerock.ex01.entity.User;
import org.zerock.ex01.repository.CartItemRepository;
import org.zerock.ex01.repository.CartRepository;
import org.zerock.ex01.repository.ProductRepository;
import org.zerock.ex01.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public Long addCart(CartItemDTO cartItemDto,String email){
        ProductEntity product=productRepository.findByProductId(cartItemDto.getProductId());
        User user=userRepository.findByUserEmail(email);

        Cart cart = cartRepository.findByUser(user);
        if(cart ==null){
            cart=Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItem savedCartItem=cartItemRepository.findByCartIdAndProductProductId(cart.getId(), product.getProductId());
        if(savedCartItem !=null){
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        }else{
            CartItem cartItem=CartItem.createCartItem(cart,product, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }


}
