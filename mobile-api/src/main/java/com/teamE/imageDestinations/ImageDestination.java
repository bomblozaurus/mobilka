package com.teamE.imageDestinations;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "image_destination")
public class ImageDestination {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;
    private Long idDestination;
    private String extension;
    private Destination destination;

    public ImageDestination() {
        this.idDestination = -1L;
    };

    public ImageDestination(Long id) {
        this.id=id;
    }

    public ImageDestination(String extension) {
        this();
        this.extension = extension;
    }

    public String getImageName() {
        return this.id.toString() + "." + this.extension;
    }
}
