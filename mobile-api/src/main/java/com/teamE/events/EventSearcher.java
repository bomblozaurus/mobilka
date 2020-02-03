package com.teamE.events;

import com.teamE.events.data.entity.Event;
import org.apache.lucene.search.Query;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventSearcher extends Searcher<Event,Event> {

    public EventSearcher() {
        super(Event.class);
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
