package com.teamE.rooms;

import com.teamE.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.TermVector;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    @Field(termVector = TermVector.YES)
    private int dsNumber;

    @Field(termVector = TermVector.YES)
    @Column(nullable = false)
    private String name;

    @Field(termVector = TermVector.YES)
    private String description;

    @OneToOne
    @JoinColumn(name = "keyholder_id")
    private User keyholder;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "configuration_id", nullable = false)
    @Builder.Default
    private RoomConfiguration configuration = RoomConfiguration.getDefaultConfiguration();

    private Long mainImage;
}


