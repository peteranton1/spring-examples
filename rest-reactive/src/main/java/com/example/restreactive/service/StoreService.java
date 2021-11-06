package com.example.restreactive.service;

import com.example.restreactive.dto.CountryDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.dto.StreetAddressDto;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.model.Country;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.StreetAddress;
import com.example.restreactive.repository.CountryRepository;
import com.example.restreactive.repository.StoreRepository;
import com.example.restreactive.repository.StreetAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private StreetAddressRepository streetAddressRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<StoreDto> findAllStores() {
        return storeRepository.findAll()
            .stream()
            .map(store -> (StoreDto) modelMapper.toDto(store))
            .toList();
    }

    public Optional<StoreDto> findByStoreCode(String storeCode) {
        return storeRepository.findByStoreCode(storeCode)
            .map(store -> (StoreDto) modelMapper.toDto(store));
    }

    public StoreDto upsertStore(StoreDto storeDto) {
        requireNonNull(storeDto);
        requireNonNull(storeDto.getStoreCode());
        StreetAddressDto streetAddressDto = storeDto.getAddress();
        if (nonNull(streetAddressDto)) {
            streetAddressDto.setId(upsertStreetAddress(streetAddressDto));
        }
        Store storeOut = storeRepository.save(storeRepository
            .findByStoreCode(storeDto.getStoreCode())
            .map(store -> (Store) modelMapper.update(store, storeDto))
            .orElse((Store) modelMapper.insert(storeDto))
        );
        return (StoreDto) modelMapper.toDto(storeOut);
    }

    public Long upsertStreetAddress(StreetAddressDto streetAddressDto) {
        requireNonNull(streetAddressDto);
        requireNonNull(streetAddressDto.getPostcode());
        CountryDto countryDto = streetAddressDto.getCountry();
        if (nonNull(countryDto)) {
            countryDto.setId(upsertCountry(countryDto));
        }
        StreetAddress streetAddress = streetAddressRepository
            .save(streetAddressRepository
                .findByLine1AndPostcode(
                    streetAddressDto.getLine1(),
                    streetAddressDto.getPostcode()
                )
                .map(address -> (StreetAddress) modelMapper
                    .update(address, streetAddressDto))
                .orElse((StreetAddress) modelMapper.insert(streetAddressDto))
            );
        return streetAddress.getId();
    }

    public Long upsertCountry(CountryDto countryDto) {
        requireNonNull(countryDto);
        requireNonNull(countryDto.getCode());
        Country countryOut = countryRepository
            .save(countryRepository
                .findByCode(countryDto.getCode())
                .map(country -> (Country) modelMapper
                    .update(country, countryDto))
                .orElse((Country) modelMapper.insert(countryDto))
            );
        return countryOut.getId();
    }
}
