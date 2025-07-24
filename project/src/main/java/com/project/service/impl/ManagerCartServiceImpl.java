package com.project.service.impl;

import com.project.entity.Cart;
import com.project.entity.User;
import com.project.repository.ManagerCartRepository;
import com.project.service.ManagerCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
