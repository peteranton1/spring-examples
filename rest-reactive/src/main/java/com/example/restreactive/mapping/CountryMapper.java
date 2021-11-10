package com.example.restreactive.mapping;

import com.example.restreactive.dto.CountryDto;
import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.model.Country;
import com.example.restreactive.model.EntityObject;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class CountryMapper implements DtoMapper, EntityMapper {

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
        Long id = nonNull(country.getId()) ?
            country.getId() :
            countryDto.getId();
        String name = nonNull(countryDto.getName()) ?
            countryDto.getName() :
            country.getName();
        String code = nonNull(countryDto.getCode()) ?
            countryDto.getCode() :
            country.getCode();
        return Country.builder()
            .id(id)
            .name(name)
            .code(code)
            .build();
    }

    public Country countryEntityOf(CountryDto countryDto) {
        if(isNull(countryDto)) {
            return null;
        }
        return Country.builder()
            .id(countryDto.getId())
            .name(countryDto.getName())
            .code(countryDto.getCode())
            .build();
    }

    public CountryDto countryDtoOf(Country country) {
        if(isNull(country)) {
            return null;
        }
        return CountryDto.builder()
            .id(country.getId())
            .name(country.getName())
            .code(country.getCode())
            .build();
    }
}
