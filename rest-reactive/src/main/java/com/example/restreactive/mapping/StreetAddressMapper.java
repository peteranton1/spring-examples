package com.example.restreactive.mapping;

import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.dto.StreetAddressDto;
import com.example.restreactive.model.Country;
import com.example.restreactive.model.EntityObject;
import com.example.restreactive.model.StreetAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
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
        Integer id = nonNull(streetAddress.getId()) ?
            streetAddress.getId() :
            streetAddressDto.getId();
        String line1 = nonNull(streetAddressDto.getLine1()) ?
            streetAddressDto.getLine1() :
            streetAddress.getLine1();
        String line2 = nonNull(streetAddressDto.getLine2()) ?
            streetAddressDto.getLine2() :
            streetAddress.getLine2();
        String city = nonNull(streetAddressDto.getCity()) ?
            streetAddressDto.getCity() :
            streetAddress.getCity();
        String county = nonNull(streetAddressDto.getCounty()) ?
            streetAddressDto.getCounty() :
            streetAddress.getCounty();
        Country country = nonNull(streetAddressDto.getCountry()) ?
            countryMapper.countryEntityOf(streetAddressDto.getCountry()) :
            streetAddress.getCountry();
        String postcode = nonNull(streetAddressDto.getPostcode()) ?
            streetAddressDto.getPostcode() :
            streetAddress.getPostcode();
        return StreetAddress.builder()
            .id(id)
            .line1(line1)
            .line2(line2)
            .city(city)
            .county(county)
            .country(country)
            .postcode(postcode)
            .build();
    }

    public StreetAddress streetAddressEntityOf(StreetAddressDto streetAddressDto) {
        if(isNull(streetAddressDto)) {
            return null;
        }
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
        if(isNull(streetAddress)) {
            return null;
        }
        return StreetAddressDto.builder()
            .id(streetAddress.getId())
            .line1(streetAddress.getLine1())
            .line2(streetAddress.getLine2())
            .city(streetAddress.getCity())
            .county(streetAddress.getCounty())
            .country(countryMapper.countryDtoOf(streetAddress.getCountry()))
            .postcode(streetAddress.getPostcode())
            .build();
    }
}
