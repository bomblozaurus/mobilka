package com.teamE.events.data.entity;


import com.teamE.commonAddsEvents.Scope;
import com.teamE.users.StudentHouse;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.bridge.builtin.EnumBridge;

import javax.persistence.*;
import javax.persistence.Parameter;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@RequiredArgsConstructor
@Indexed
@AnalyzerDef(name = "customanalyzer",
        tokenizer = @TokenizerDef(factory = StandardTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
        })

public class Event {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Field()
    @Analyzer(definition = "customanalyzer")
    private String name;
    private Date date;
    @Field()
    @Analyzer(definition = "customanalyzer")
    private String street;
    private int houseNumber;
    private int apartmentNumber;
    @Field()
    @Analyzer(definition = "customanalyzer")
    private String city;
    private String zip;
    private LocalDateTime creationDate;
    private Long userID;
    @Lob
    @Column
    @Field()
   @Analyzer(definition = "customanalyzer")
    private String description;
    private Long mainImage;
    @Field(bridge = @FieldBridge(impl = EnumBridge.class))
    private Scope scope;
    @Field(bridge=@FieldBridge(impl= StudentHouseBridge.class))
    private StudentHouse studentHouse;
}
