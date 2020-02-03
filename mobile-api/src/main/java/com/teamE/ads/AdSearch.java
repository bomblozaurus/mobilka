package com.teamE.ads;

import com.teamE.ads.data.entity.Ad;
import com.teamE.events.Searcher;
import org.apache.lucene.search.Query;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdSearch extends Searcher<Ad,Ad> {

    public AdSearch() {
        super(Ad.class);
    }

    @Override
    public Query checkQuery(String text) {
        return  getQueryBuilder().phrase()
                .withSlop(3).onField("name")
                .andField("street")
                .andField("description")
                .andField("city")
                .sentence(text).createQuery();
    }
}
