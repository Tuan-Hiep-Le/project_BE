package com.project.service.impl;

import com.project.entity.Book;
import com.project.entity.CartItem;
import com.project.repository.ManagerCartItemRepository;
import com.project.service.ManagerCartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public CartItem getCartItemByBook(Book book) {
        return managerCartItemRepository.findByBook(book);
    }

    @Transactional
    @Override
    public void removeCartItem(Integer cartItemId) {
        managerCartItemRepository.deleteById(cartItemId);
    }
}

