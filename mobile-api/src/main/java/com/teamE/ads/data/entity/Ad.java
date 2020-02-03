package com.teamE.ads.data.entity;

import com.teamE.commonAddsEvents.Scope;
import com.teamE.events.data.entity.StudentHouseBridge;
import com.teamE.users.StudentHouse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.bridge.builtin.EnumBridge;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
public class Ad {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Field()
    @Analyzer(definition = "customanalyzer")
    private String name;
    BigDecimal price;
    @Field()
    @Analyzer(definition = "customanalyzer")
    private String street;
    private int houseNumber;
    private int apartmentNumber;
    private LocalDateTime date;
    private Long userID;
    @Field()
    @Analyzer(definition = "customanalyzer")
    private String city;
    private String zip;
    @Lob
    @Column
    @Field()
    @Analyzer(definition = "customanalyzer")
    private String description;
    private Long mainImage;
    @Field(bridge = @FieldBridge(impl = EnumBridge.class))
    private Scope scope;
    @Field(bridge=@FieldBridge(impl= StudentHouseBridge.class))
    @Enumerated(EnumType.STRING)
    private StudentHouse studentHouse;
}
