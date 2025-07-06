package com.example.project.service.book_service;

import com.example.project.entity.Book;
import com.example.project.repository.ManagerBookRepository;
import com.example.project.service.book_service.ManagerBookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service

public class ManagerBookServiceImpl implements ManagerBookService {
    @Autowired
    private ManagerBookRepository managerBookRepository;

    //Lấy toàn bộ sách

    @Override
    public Page<Book> getAllBook(Pageable pageable) {
        Page<Book> page =  managerBookRepository.findAll(pageable);
        if (page.isEmpty()) {
            return null;
        }
        return page;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public Book addBook(Book book){
        Book book1 = managerBookRepository.findByNameBookAndNameAuthorAndLanguage(book.getNameBook(), book.getNameAuthor(), book.getLanguage());
        if (book1 == null) {
            return managerBookRepository.saveAndFlush(book);
        }
        book1.setQuantity(book1.getQuantity() +book.getQuantity());
        return managerBookRepository.saveAndFlush(book1);
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
    @Transactional
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
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Book updateBook(Book book) {
        Book bookExist = managerBookRepository.findByNameBookAndNameAuthorAndLanguage(book.getNameBook(), book.getNameAuthor(), book.getLanguage());
        if (bookExist == null){
            throw new RuntimeException("Sách không tồn tại");
        }
        bookExist.setNameBook(book.getNameBook());
        bookExist.setNameAuthor(book.getNameAuthor());
        bookExist.setLanguage(book.getLanguage());
        bookExist.setQuantity(bookExist.getQuantity() +book.getQuantity());
        bookExist.setNameCategory(book.getNameCategory());
        bookExist.setPrice(book.getPrice());
        bookExist.setNameTopic(book.getNameTopic());
        return managerBookRepository.saveAndFlush(bookExist);
    }

    //Phân loại sách bằng tác giả
    @Override
    public Page<Book> findByAuthor(String nameAuthor, Pageable pageable) {
        return managerBookRepository.findByNameAuthor(nameAuthor,pageable);
    }
    //Phân loại sách bằng thể loại

    @Override
    public Page<Book> findByCategory(String nameCategory, Pageable pageable) {
        return managerBookRepository.findByNameCategory(nameCategory,pageable);
    }
    //Phân loại sách bằng chủ đề

    @Override
    public Page<Book> findByTopic(String nameTopic,Pageable pageable) {
        return managerBookRepository.findByNameTopic(nameTopic, pageable);
    }

    @Override
    public List<String> getAllAuthor() {
        return managerBookRepository.listAuthor();
    }

    @Override
    public List<String> getAllCategory() {
        return managerBookRepository.listCategory().stream().map(String::trim).toList();
    }

    @Override
    public List<String> getAllTopic() {
        return managerBookRepository.listTopic();
    }

    @Override
    public Book getBookById(Integer id) {
        return managerBookRepository.findByBookId(id);
    }


}
