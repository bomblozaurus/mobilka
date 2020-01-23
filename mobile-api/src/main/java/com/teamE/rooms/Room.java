package com.teamE.rooms;

import com.teamE.users.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int dsNumber;

    @Column(nullable = false)
    private String name;

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


