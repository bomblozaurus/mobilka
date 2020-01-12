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
                .address(pojo.getAddress())
                .description(pojo.getDescription())
                .mainImage(pojo.getMainImage())
                .scope(pojo.getScope())
                .studentHouse(pojo.getStudentHouse())
                .build();
    }
}


