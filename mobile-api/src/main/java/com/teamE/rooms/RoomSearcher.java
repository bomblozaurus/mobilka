package com.teamE.rooms;

import com.teamE.commonAddsEvents.Scope;
import com.teamE.events.data.entity.Event;
import com.teamE.users.StudentHouse;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.MustJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Configuration
public class RoomSearcher {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<RoomWithConfigurationProjection> searchRoom(int dsNumber, String text, Pageable page) {


        QueryBuilder queryBuilder = getQueryBuilder();
        Query keywordQuery = queryBuilder
                .bool().must(queryBuilder
                        .bool().should(queryBuilder.phrase()
                                .withSlop(1).onField("name")
                                .sentence(text).createQuery())
                        .should(queryBuilder.phrase()
                                .withSlop(1).onField("description")
                                .sentence(text).createQuery())
                       .createQuery())
                .must(queryBuilder.keyword()
                        .onField("dsNumber")
                        .matching(dsNumber)
                        .createQuery()).createQuery();

        List<RoomWithConfigurationProjection> events = getJpaQuery(keywordQuery, page).getResultList();

        long total = getJpaQuery(keywordQuery, page).getResultSize();
        return new PageImpl<>(events, page, total);
    }

    private FullTextQuery getJpaQuery(Query luceneQuery, Pageable page) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, Room.class);

        fullTextQuery.setFirstResult((int) page.getOffset());
        fullTextQuery.setMaxResults(page.getPageSize());
        return fullTextQuery;
    }

    private QueryBuilder getQueryBuilder() {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        return fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Room.class)
                .get();
    }
}
