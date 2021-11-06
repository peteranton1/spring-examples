package com.example.restreactive.mapping;

import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.dto.StreetAddressDto;
import com.example.restreactive.model.Country;
import com.example.restreactive.model.EntityObject;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.StreetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class StoreMapper implements DtoMapper, EntityMapper {

    @Autowired
    private StreetAddressMapper streetAddressMapper;

    @Override
    public boolean accepts(DtoObject dtoObject) {
        return dtoObject instanceof StoreDto;
    }

    @Override
    public EntityObject mapTo(DtoObject dtoObject) {
        return storeEntityOf((StoreDto) dtoObject);
    }

    @Override
    public boolean accepts(EntityObject entityObject) {
        return entityObject instanceof Store;
    }

    @Override
    public DtoObject mapTo(EntityObject entityObject) {
        return storeDtoOf((Store) entityObject);
    }

    @Override
    public EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject) {
        return storeUpdate((Store) entityObject, (StoreDto) dtoObject);
    }

    public Store storeUpdate(Store store, StoreDto storeDto) {
        Long id = nonNull(store.getId()) ?
            store.getId() :
            storeDto.getId();
        String storeName = nonNull(storeDto.getStoreName()) ?
            storeDto.getStoreName() :
            store.getStoreName();
        String storeCode = nonNull(storeDto.getStoreCode()) ?
            storeDto.getStoreCode() :
            store.getStoreCode();
        StreetAddress streetAddress = nonNull(storeDto.getAddress()) ?
            streetAddressMapper.streetAddressEntityOf(storeDto.getAddress()) :
            store.getAddress();
        return Store.builder()
            .id(id)
            .storeName(storeName)
            .storeCode(storeCode)
            .address(streetAddress)
            .build();
    }

    public Store storeEntityOf(StoreDto storeDto) {
        return new Store(
            storeDto.getId()
            , storeDto.getStoreName()
            , storeDto.getStoreCode()
            , streetAddressMapper.streetAddressEntityOf(storeDto.getAddress())
        );
    }

    public StoreDto storeDtoOf(Store store) {
        return StoreDto.builder()
            .id(store.getId())
            .storeName(store.getStoreName())
            .storeCode(store.getStoreCode())
            .address(streetAddressMapper.streetAddressDtoOf(store.getAddress()))
            .build();
    }

}
