package com.teamE.rooms;

import com.teamE.events.Searcher;
import org.apache.lucene.search.Query;
import org.hibernate.Criteria;
import org.hibernate.SharedSessionContract;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.transform.BasicTransformerAdapter;
import org.hibernate.transform.ResultTransformer;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class RoomSearch extends Searcher<Room, RoomWithConfigurationDto> {

    public RoomSearch() {
        super(Room.class);
    }

    @Override
    public Query checkQuery(String text) {
        return getQueryBuilder().phrase()
                .withSlop(3).onField("name")
                .andField("description")
                .sentence(text).createQuery();
    }

    @Override
    public FullTextQuery getJpaQuery(Query luceneQuery, Pageable page) {
        FullTextQuery fullTextQuery = super.getJpaQuery(luceneQuery, page);
        return fullTextQuery.setProjection(FullTextQuery.THIS)
                .setResultTransformer(new BasicTransformerAdapter() {
                    @Override
                    public RoomWithConfigurationDto transformTuple(Object[] tuple, String[] aliases) {
                        return new RoomWithConfigurationDto(
                                ((Room) tuple[0]).getId(),
                                ((Room) tuple[0]).getDsNumber(),
                                ((Room) tuple[0]).getName(),
                                ((Room) tuple[0]).getMainImage(),
                                ((Room) tuple[0]).getDescription(),
                                ((Room) tuple[0]).getConfiguration().getOpenFrom(),
                                ((Room) tuple[0]).getConfiguration().getOpenTo(),
                                ((Room) tuple[0]).getConfiguration().getRentInterval(),
                                ((Room) tuple[0]).getConfiguration().getPricePerInterval());
                    }
                });
    }

}
