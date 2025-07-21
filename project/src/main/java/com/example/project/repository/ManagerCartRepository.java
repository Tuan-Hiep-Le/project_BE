package com.example.project.repository;
import com.example.project.entity.Cart;
import com.example.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerCartRepository extends JpaRepository<Cart,Integer>{
    public Cart findByUser(User user);

}
