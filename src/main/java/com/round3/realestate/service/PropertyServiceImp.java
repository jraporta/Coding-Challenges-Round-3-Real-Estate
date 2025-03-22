package com.round3.realestate.service;

import com.round3.realestate.entity.Property;
import com.round3.realestate.payload.RealStateData;
import com.round3.realestate.repository.PropertyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class PropertyServiceImp implements PropertyService{

    private final PropertyRepository propertyRepository;

    public PropertyServiceImp(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    @Override
    public void createProperty(RealStateData data) {
        Property property = mapToProperty(data);
        propertyRepository.save(property);
        log.info("Saved property: {}", property);
    }

    private Property mapToProperty(RealStateData data) {
        String price = data.getPrice().replace(".", "");
        return Property.builder()
                .availability("Available")
                .location(data.getLocation())
                .name(data.getType())
                .price(new BigDecimal(price))
                .rooms(data.getRooms())
                .size(data.getSize())
                .build();
    }
}
