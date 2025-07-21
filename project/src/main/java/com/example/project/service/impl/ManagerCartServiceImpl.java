package com.example.project.service.impl;

import com.example.project.entity.Cart;
import com.example.project.entity.CartItem;
import com.example.project.entity.User;
import com.example.project.repository.ManagerCartRepository;
import com.example.project.service.ManagerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerCartServiceImpl implements ManagerCartService {
    @Autowired
    private ManagerCartRepository managerCartRepository;


    @Override
    @Transactional
    public Cart addCart(Cart cart) {
        return managerCartRepository.saveAndFlush(cart);
    }

    @Override
    public Cart getCartByUser(User user) {
        return managerCartRepository.findByUser(user);
    }
}
