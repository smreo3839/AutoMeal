package org.zerock.ex01.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.ex01.dto.CartDetailDTO;
import org.zerock.ex01.dto.CartItemDTO;
import org.zerock.ex01.dto.CartOrderDTO;
import org.zerock.ex01.dto.OrderDTO;
import org.zerock.ex01.entity.*;
import org.zerock.ex01.repository.CartItemRepository;
import org.zerock.ex01.repository.CartRepository;
import org.zerock.ex01.repository.ProductRepository;
import org.zerock.ex01.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final OrderService orderService;

    public Long addCart(CartItemDTO cartItemDto, String email) {
        ProductEntity product = productRepository.findByProductId(cartItemDto.getProductId());
        User user = userRepository.findByUserEmail(email);

        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = Cart.createCart(user);
            cartRepository.save(cart);
        }

        CartItem savedCartItem = cartItemRepository.findByCartIdAndProductProductId(cart.getId(), product.getProductId());
        if (savedCartItem != null) {
            savedCartItem.addCount(cartItemDto.getCount());
            return savedCartItem.getId();
        } else {
            CartItem cartItem = CartItem.createCartItem(cart, product, cartItemDto.getCount());
            cartItemRepository.save(cartItem);
            return cartItem.getId();
        }
    }

    @Transactional(readOnly = true)
    public List<CartDetailDTO> getCartList(String email) {
        List<CartDetailDTO> cartDetailDtoList = new ArrayList<>();

        User user = userRepository.findByUserEmail(email);
        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findCartDetailDtoList(cart.getId());

        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email) {
        log.info("Validation {}", cartItemId);
        User curUser = userRepository.findByUserEmail(email);
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        User savedUser = cartItem.getCart().getUser();

        if (!StringUtils.equals(curUser.getUserEmail(), savedUser.getUserEmail())) {
            return false;
        }
        return true;
    }


    public void updateCartItemCount(Long cartItemId, int count) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Order orderCartItem(List<CartOrderDTO> cartOrderDTOList, String email, String imp_uid, String preceipt_url) {
        log.info("단체 주문하기");
        List<OrderDTO> orderDTOList = new ArrayList<>();
        for (CartOrderDTO cartOrderDTO : cartOrderDTOList) {//전달 받은 주문 ㅏㅇ품 번호를 이용하여 주문 로직으로 전달
            CartItem cartItem = cartItemRepository.findById(cartOrderDTO.getCartItemId()).orElseThrow(EntityNotFoundException::new);

            OrderDTO orderDto = new OrderDTO();
            orderDto.setProductId(cartItem.getProduct().getProductId());
            orderDto.setCount(cartItem.getCount());
            orderDTOList.add(orderDto);
        }
        Order order = orderService.orders(orderDTOList, email, imp_uid, preceipt_url);//장바구니에 담은 상품 주문
        for (CartOrderDTO cartOrderDTO : cartOrderDTOList) {
            CartItem cartItem = cartItemRepository.findById(cartOrderDTO.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);//주문한 상품들 장바구니에서 제거
        }
        return order;
    }


}
