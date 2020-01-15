package com.teamE.ads.data.entity;

import com.teamE.common.Transformer;
import org.springframework.stereotype.Service;

@Service
public class AdPOJOToAdTransformer implements Transformer<AdPOJO,Ad> {
    @Override
    public Ad transform(AdPOJO pojo) {
        return Ad.builder()
                .name(pojo.getName())
                .price(pojo.getPrice())
                .street(pojo.getStreet())
                .houseNumber(pojo.getHouseNumber())
                .apartmentNumber(pojo.getApartmentNumber())
                .city(pojo.getCity())
                .zip(pojo.getZip())
                .description(pojo.getDescription())
                .mainImage(pojo.getMainImage())
                .scope(pojo.getScope())
                .studentHouse(pojo.getStudentHouse())
                .build();
    }
}


