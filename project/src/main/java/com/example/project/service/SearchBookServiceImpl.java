package com.example.project.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.project.entity.BookDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class SearchBookServiceImpl implements SearchBookService{
    @Autowired
    private ElasticsearchClient elasticsearchClient;
    @Override
    public List<BookDocument> searchBookByName(String nameBook)  {
        SearchResponse<BookDocument> response = null;
        try {
            response = elasticsearchClient.search(s -> s
                    .index("book_document")
                    .query(q -> q.fuzzy(f -> f.field("nameBook").value(nameBook).fuzziness("AUTO"))), BookDocument.class
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.hits().hits().stream().map(hit -> hit.source()).toList();
    }

    @Override
    public Page<BookDocument> searchBookByNameAuthor(String nameAuthor, Pageable pageable) {
        try {
            SearchResponse<BookDocument> response = elasticsearchClient.search(s-> s
                    .index("book_document")
                    .query(q -> q.fuzzy(f -> f.field("nameAuthor").value(nameAuthor).fuzziness("AUTO"))), BookDocument.class
            );
            List<BookDocument> list = response.hits().hits().stream().map(hit -> hit.source()).toList();
            int start = (int)pageable.getOffset();
            int  end = Math.min((start + pageable.getPageSize()), list.size());
            List<BookDocument> page = list.subList(start,end);
            return new PageImpl<>(page,pageable,list.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Page<BookDocument> searchBookByNameCategory(String nameCategory, Pageable pageable) {
        try {
            SearchResponse<BookDocument> response = elasticsearchClient.search(s-> s
                    .index("book_document").query(q -> q.fuzzy(f -> f.field("nameCategory").value(nameCategory).fuzziness("AUTO"))), BookDocument.class);
            List<BookDocument> list = response.hits().hits().stream().map(hit -> hit.source()).toList();
            int start = (int)pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()),list.size());
            List<BookDocument> page = list.subList(start,end);
            return new PageImpl<>(page,pageable,list.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<BookDocument> searchBookByNameTopic(String nameTopic, Pageable pageable) {
        try {
            SearchResponse<BookDocument> response = elasticsearchClient.search(s -> s
                    .index("book_document").query(q -> q.fuzzy(f ->  f.field("nameTopic").value(nameTopic).fuzziness("AUTO"))), BookDocument.class);
            List<BookDocument> list = response.hits().hits().stream().map(hit -> hit.source()).toList();
            int start=(int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()),list.size());
            List<BookDocument> page = list.subList(start,end);
            return new PageImpl<>(page,pageable, list.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Page<BookDocument> searchBookByNameBookAndAuthorAndCategoryAndTopic(String keyword, Pageable pageable) {
        try {
            SearchResponse<BookDocument> response = elasticsearchClient.search(s-> s
                    .index("book_document").query(q -> q.multiMatch(m -> m.query(keyword).fields("nameBook","nameAuthor","nameCategory","nameTopic").minimumShouldMatch("70%").fuzziness("Auto"))), BookDocument.class);

            List<BookDocument> list = response.hits().hits().stream().map(hit -> hit.source()).filter(Objects::nonNull).toList();
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()),list.size());
            if (start >= list.size()){
                return new PageImpl<>(List.of(),pageable, list.size());
            }
            return new PageImpl<>(list.subList(start,end),pageable,list.size());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
