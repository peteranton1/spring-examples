package com.example.restreactive.service;

import com.example.restreactive.dto.CountryDto;
import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.dto.StreetAddressDto;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.model.Country;
import com.example.restreactive.model.Store;
import com.example.restreactive.model.StreetAddress;
import com.example.restreactive.repository.CountryRepository;
import com.example.restreactive.repository.StoreRepository;
import com.example.restreactive.repository.StreetAddressRepository;
import com.example.restreactive.service.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.nonNull;

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

    private final ServiceHelper helper = new ServiceHelper();

    public List<StoreDto> findAllStores() {
        return storeRepository.findAll()
            .stream()
            .map(store -> (StoreDto) modelMapper.toDto(store))
            .toList();
    }

    public List<Store> findByStoreCode(String storeCode) {
        return storeRepository.findByStoreCode(storeCode);
    }

    public StoreDto upsertStore(StoreDto storeDto) {
        verifyStoreForUpdate(storeDto);
        Store storeOut = storeRepository.save(storeRepository
            .findByStoreCode(storeDto.getStoreCode()).stream()
            .findFirst()
            .map(store -> (Store) modelMapper.update(store, storeDto))
            .orElse((Store) modelMapper.insert(storeDto))
        );
        helper.assertNonNull("storeOut", storeOut);
        return (StoreDto) modelMapper.toDto(storeOut);
    }

    private void verifyStoreForUpdate(StoreDto storeDto) {
        helper.assertNonNull("storeDto", storeDto);
        StreetAddressDto streetAddressDto = storeDto.getAddress();
        if (nonNull(streetAddressDto)) {
            Integer id = upsertStreetAddress(streetAddressDto);
            streetAddressDto.setId(id);
            String storeCode = storeDto.getStoreName().toLowerCase() + "-" + id;
            storeDto.setStoreCode(storeCode);
        }
        helper.assertNonNull("storeDto.getStoreCode()", storeDto.getStoreCode());
    }

    public Integer upsertStreetAddress(StreetAddressDto streetAddressDto) {
        helper.assertNonNull("streetAddressDto", streetAddressDto);
        helper.assertNonNull("streetAddressDto.getPostcode()", streetAddressDto.getPostcode());
        CountryDto countryDto = streetAddressDto.getCountry();
        if (nonNull(countryDto)) {
            Integer id = upsertCountry(countryDto);
            helper.assertNonNull("country.id", id);
            countryDto.setId(id);
        }
        StreetAddress streetAddress = streetAddressRepository
            .save(streetAddressRepository
                .findByLine1AndPostcode(
                    streetAddressDto.getLine1(),
                    streetAddressDto.getPostcode()
                ).stream().findFirst()
                .map(address -> (StreetAddress) modelMapper
                    .update(address, streetAddressDto))
                .orElse((StreetAddress) modelMapper.insert(streetAddressDto))
            );
        helper.assertNonNull("streetAddress", streetAddress);
        return streetAddress.getId();
    }

    public Integer upsertCountry(CountryDto countryDto) {
        helper.assertNonNull("countryDto", countryDto);
        helper.assertNonNull("countryDto.getCode()", countryDto.getCode());
        Country countryOut = countryRepository
            .save(countryRepository
                .findByCode(countryDto.getCode())
                .stream().findFirst()
                .map(country -> (Country) modelMapper
                    .update(country, countryDto))
                .orElse((Country) modelMapper.insert(countryDto))
            );
        helper.assertNonNull("countryOut", countryOut);
        return countryOut.getId();
    }

    public MessageDto deleteStore(String storeCode) {

        helper.assertNonNull("storeCode", storeCode);
        storeCode = storeCode.toLowerCase();
        storeRepository
            .findByStoreCode(storeCode)
            .stream().findFirst()
            .ifPresent(store1 -> storeRepository.delete(store1));
        return MessageDto.builder()
            .code("200")
            .message("Store deleted: " + storeCode)
            .build();
    }

}
