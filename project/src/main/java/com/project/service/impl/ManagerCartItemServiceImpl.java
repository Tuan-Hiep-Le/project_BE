package com.project.service.impl;

import com.project.entity.Book;
import com.project.entity.Cart;
import com.project.entity.CartItem;
import com.project.repository.ManagerCartItemRepository;
import com.project.service.ManagerCartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerCartItemServiceImpl implements ManagerCartItemService {
    @Autowired
    private ManagerCartItemRepository managerCartItemRepository;
    @Override
    @Transactional
    public CartItem addCartItem(CartItem cartItem) {
        cartItem.setIsSelected(false);
        return managerCartItemRepository.saveAndFlush(cartItem);
    }

    @Override
    public List<CartItem> getAllCTByUserId(Integer id) {
        return managerCartItemRepository.listCartItem(id);
    }

    @Override
    public CartItem getCartItemByBookAndCart(Book book, Cart cart) {
        return managerCartItemRepository.findByBookAndCart(book,cart);
    }

    @Transactional
    @Override
    public void removeCartItem(Integer cartItemId) {
        managerCartItemRepository.deleteById(cartItemId);
    }

    @Override
    public CartItem getCartItemById(Integer cartItemId) {
        Optional<CartItem> cartItem = managerCartItemRepository.findById(cartItemId);
        if (cartItem.isPresent()) {
            return cartItem.get();
        }
        return null;
    }
}

