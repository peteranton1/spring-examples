package com.example.restreactive.mapping;

import com.example.restreactive.dto.CountryDto;
import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.dto.EmailAddressDto;
import com.example.restreactive.model.Country;
import com.example.restreactive.model.EmailAddress;
import com.example.restreactive.model.EntityObject;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class CountryMapper implements DtoMapper, EntityMapper{

    @Override
    public boolean accepts(DtoObject dtoObject) {
        return dtoObject instanceof CountryDto;
    }

    @Override
    public EntityObject mapTo(DtoObject dtoObject) {
        return countryEntityOf((CountryDto) dtoObject);
    }

    @Override
    public boolean accepts(EntityObject entityObject) {
        return entityObject instanceof Country;
    }

    @Override
    public DtoObject mapTo(EntityObject entityObject) {
        return countryDtoOf((Country) entityObject);
    }

    @Override
    public EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject) {
        return countryUpdate((Country) entityObject, (CountryDto) dtoObject);
    }

    public Country countryUpdate(Country country, CountryDto countryDto) {
        Long id = nonNull(country.id()) ?
            country.id() :
            countryDto.getId();
        String name = nonNull(countryDto.getName()) ?
            countryDto.getName() :
            country.name();
        String code = nonNull(countryDto.getCode()) ?
            countryDto.getCode() :
            country.code();
        return new Country(
            id,
            name,
            code);
    }

    public Country countryEntityOf(CountryDto countryDto) {
        return new Country(
            countryDto.getId(),
            countryDto.getName(),
            countryDto.getCode());
    }

    public CountryDto countryDtoOf(Country country) {
        return CountryDto.builder()
            .id(country.id())
            .name(country.name())
            .code(country.code())
            .build();
    }
}
