package com.example.project.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.project.entity.Book;
import com.example.project.repository.ManagerBookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
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

    //Phân loại sách bằng tác giả
    @Override
    public Page<Book> findByAuthor(String nameAuthor, Pageable pageable) {
        return managerBookRepository.findByAuthor(nameAuthor,pageable);
    }
    //Phân loại sách bằng thể loại

    @Override
    public Page<Book> findByCategory(String nameCategory, Pageable pageable) {
        return managerBookRepository.findByCategory(nameCategory,pageable);
    }
    //Phân loại sách bằng chủ đề

    @Override
    public Page<Book> findByTopic(String nameTopic,Pageable pageable) {
        return managerBookRepository.findByTopic(nameTopic, pageable);
    }


}
