package com.example.restreactive.mapping;

import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.dto.StreetAddressDto;
import com.example.restreactive.model.Country;
import com.example.restreactive.model.EntityObject;
import com.example.restreactive.model.StreetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class StreetAddressMapper implements DtoMapper, EntityMapper {

    @Autowired
    private CountryMapper countryMapper;

    @Override
    public boolean accepts(DtoObject dtoObject) {
        return dtoObject instanceof StreetAddressDto;
    }

    @Override
    public EntityObject mapTo(DtoObject dtoObject) {
        return streetAddressEntityOf((StreetAddressDto) dtoObject);
    }

    @Override
    public boolean accepts(EntityObject entityObject) {
        return entityObject instanceof StreetAddress;
    }

    @Override
    public DtoObject mapTo(EntityObject entityObject) {
        return streetAddressDtoOf((StreetAddress) entityObject);
    }

    @Override
    public EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject) {
        return streetAddressUpdate((StreetAddress) entityObject, (StreetAddressDto) dtoObject);
    }

    public StreetAddress streetAddressUpdate(StreetAddress streetAddress, StreetAddressDto streetAddressDto) {
        Long id = nonNull(streetAddress.id()) ?
            streetAddress.id() :
            streetAddressDto.getId();
        String line1 = nonNull(streetAddressDto.getLine1()) ?
            streetAddressDto.getLine1() :
            streetAddress.line1();
        String line2 = nonNull(streetAddressDto.getLine2()) ?
            streetAddressDto.getLine2() :
            streetAddress.line2();
        String city = nonNull(streetAddressDto.getCity()) ?
            streetAddressDto.getCity() :
            streetAddress.city();
        String county = nonNull(streetAddressDto.getCounty()) ?
            streetAddressDto.getCounty() :
            streetAddress.county();
        Country country = nonNull(streetAddressDto.getCountry()) ?
            countryMapper.countryEntityOf(streetAddressDto.getCountry()) :
            streetAddress.country();
        String postcode = nonNull(streetAddressDto.getPostcode()) ?
            streetAddressDto.getPostcode() :
            streetAddress.postcode();
        return new StreetAddress(
            id
            , line1
            , line2
            , city
            , county
            , country
            , postcode);
    }

    public StreetAddress streetAddressEntityOf(StreetAddressDto streetAddressDto) {
        return new StreetAddress(
            streetAddressDto.getId()
            , streetAddressDto.getLine1()
            , streetAddressDto.getLine2()
            , streetAddressDto.getCity()
            , streetAddressDto.getCounty()
            , countryMapper.countryEntityOf(streetAddressDto.getCountry())
            , streetAddressDto.getPostcode()
        );
    }

    public StreetAddressDto streetAddressDtoOf(StreetAddress streetAddress) {
        return StreetAddressDto.builder()
            .id(streetAddress.id())
            .line1(streetAddress.line1())
            .line2(streetAddress.line2())
            .city(streetAddress.city())
            .county(streetAddress.county())
            .country(countryMapper.countryDtoOf(streetAddress.country()))
            .postcode(streetAddress.postcode())
            .build();
    }

}
