package com.project.repository;
import com.project.entity.Cart;
import com.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerCartRepository extends JpaRepository<Cart,Integer>{
    public Cart findByUser(User user);

}
