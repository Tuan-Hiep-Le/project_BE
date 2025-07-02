package com.example.project.repository.elt;

import com.example.project.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ManagerBookEltRepository extends ElasticsearchRepository<Book,Integer> {
    Page<Book> findByNameBookContainingIgnoreCase(String nameBook, Pageable pageable);

}
