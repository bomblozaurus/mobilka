package com.teamE.events;

import com.teamE.commonAddsEvents.Scope;
import com.teamE.events.data.entity.Event;
import com.teamE.users.StudentHouse;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.MustJunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.hibernate.search.query.dsl.QueryBuilder;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.apache.lucene.search.Query;

import java.util.LinkedList;
import java.util.List;

@Configuration
public class EventSearcher {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public EventSearcher(EntityManager entityManager) {
        super();
        this.entityManager = entityManager;
    }

    public Page<Event> searchEvent(Scope scope, StudentHouse studentHouse, String text, Pageable page) {

        Query keywordQuery;
        QueryBuilder queryBuilder = getQueryBuilder();
        MustJunction mustJunction = queryBuilder
                .bool().must(queryBuilder.phrase()
                        .withSlop(1).onField("name")
                        .andField("street")
                        .andField("description")
                        .andField("city")
                        .sentence(text).createQuery());

        keywordQuery = mustJunction.createQuery();

        List<Event> events = getJpaQuery(keywordQuery, page).getResultList();

        long total = getJpaQuery(keywordQuery, page).getResultSize();
        return new PageImpl<>(events, page, total);
    }

    private FullTextQuery getJpaQuery(org.apache.lucene.search.Query luceneQuery, Pageable page) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Event.class);

        fullTextQuery.setFirstResult((int) page.getOffset());
        fullTextQuery.setMaxResults(page.getPageSize());
        return fullTextQuery;
    }

    private QueryBuilder getQueryBuilder() {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Event.class)
                .get();
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void resetIndex() throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();
    }
}
