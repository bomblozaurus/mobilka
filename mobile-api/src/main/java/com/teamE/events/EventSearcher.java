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

    public Page<Event> search(String text, StudentHouse studentHouse, Pageable pageable, Scope scope) {
        MustJunction mustJunction = getQueryBuilder().bool()
                .must(checkStudenHouse(studentHouse))
                .must(checkScope(scope));

        if (text.trim().length() > 0) {
            mustJunction=mustJunction.must(checkQuery(text));
        }

        return pageResult(mustJunction.createQuery(), pageable);
    }

    public Page<Event> search(String text, StudentHouse studentHouse, Pageable pageable) {
        MustJunction mustJunction = getQueryBuilder().bool()
                .must(checkStudenHouse(studentHouse));

        if (text.trim().length() > 0) {
            mustJunction=mustJunction.must(checkQuery(text));
        }

        return pageResult(mustJunction.createQuery(), pageable);
    }

    private Page<Event> pageResult(Query query, Pageable pageable) {
        List<Event> events = getJpaQuery(query, pageable).getResultList();

        long total = getJpaQuery(query, pageable).getResultSize();
        return new PageImpl<>(events, pageable, total);
    }

    public Query checkQuery(String text) {
        return getQueryBuilder().phrase()
                .withSlop(3).onField("name")
                .andField("street")
                .andField("description")
                .andField("city")
                .sentence(text).createQuery();
    }

    private Query checkScope(Scope scope) {
        QueryBuilder queryBuilder = getQueryBuilder();

        if (scope == Scope.OTHER) {
            return queryBuilder
                    .bool().must(queryBuilder.keyword()
                            .onField("scope")
                            .matching(Scope.OTHER)
                            .createQuery()).createQuery();
        }
        if (scope == Scope.STUDENT) {
            return queryBuilder
                    .bool().must(queryBuilder.bool()
                            .should(queryBuilder.keyword()
                                    .onField("scope")
                                    .matching(Scope.OTHER).createQuery())
                            .should(queryBuilder.keyword()
                                    .onField("scope")
                                    .matching(Scope.STUDENT).createQuery())
                            .createQuery()).createQuery();
        }
        return queryBuilder
                .bool().must(queryBuilder.bool()
                        .should(queryBuilder.keyword()
                                .onField("scope")
                                .matching(Scope.OTHER).createQuery())
                        .should(queryBuilder.keyword()
                                .onField("scope")
                                .matching(Scope.STUDENT).createQuery())
                        .should(queryBuilder.keyword()
                                .onField("scope")
                                .matching(Scope.DORMITORY).createQuery())
                        .createQuery()).createQuery();
    }

    private Query checkStudenHouse(StudentHouse studentHouse) {
        QueryBuilder queryBuilder = getQueryBuilder();
        return queryBuilder.bool()
                        .should(queryBuilder.keyword()
                                .onField("studentHouse")
                                .matching(studentHouse).createQuery())
                        .should(queryBuilder.keyword()
                                .onField("studentHouse")
                                .matching(null).createQuery()).createQuery();
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
