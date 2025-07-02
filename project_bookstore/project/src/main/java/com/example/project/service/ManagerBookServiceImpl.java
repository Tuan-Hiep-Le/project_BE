package com.example.project.service;

import com.example.project.entity.Book;
import com.example.project.repository.elt.ManagerBookEltRepository;
import com.example.project.repository.jpa.ManagerBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@EnableJpaRepositories(basePackages = "com.example.project.repository.jpa")
@EnableElasticsearchRepositories(basePackages = "com.example.project.repository.elt")

public class ManagerBookServiceImpl implements ManagerBookService {
    @Autowired
    private ManagerBookRepository managerBookRepository;
    @Autowired
    private ManagerBookEltRepository managerBookEltRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Book addBook(Book book){
        return managerBookRepository.save(book);
    }

    //Xóa toàn bộ số lượng sách
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void removeBook(Integer id){
        if(!managerBookRepository.existsById(id)){
            throw new EntityNotFoundException("Không tìm thấy sách");
        }
        managerBookRepository.deleteById(id);
    }
    //Xóa một phần sách
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void removeQuantityBook(Integer id, int quantityRemove){
        Book book = managerBookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sách"));
        if (book.getQuantity() > quantityRemove) {
            int quantityRemain = book.getQuantity() - quantityRemove;
            book.setQuantity(quantityRemain);
            managerBookRepository.save(book);
        } else if (book.getQuantity() < quantityRemove) {
            throw new IllegalArgumentException("Số lượng xóa vượt quá giới hạn.");
        }else {
            removeBook(id);
        }
    }
    //Sửa sách
    @Override
    public Book updateBook(Book book) {
        return managerBookRepository.saveAndFlush(book);
    }

    //Tìm kiếm sách bằng tên sách
    @Override
    public Page<Book> findByNameBook(String nameBook, Pageable pageable){
        return managerBookEltRepository.findByNameBookContainingIgnoreCase(nameBook,pageable);
    }

    @Override
    public Page<Book> findByAuthor(String nameAuthor, Pageable pageable) {
        return managerBookRepository.findByNameAuthor(nameAuthor,pageable);
    }

    @Override
    public Page<Book> findByCategory(String nameCategory, Pageable pageable) {
        return managerBookRepository.findByNameCategory(nameCategory,pageable);
    }

    @Override
    public Page<Book> findByTopic(String nameTopic,Pageable pageable) {
        return managerBookRepository.findByNameTopic(nameTopic, pageable);
    }
}
