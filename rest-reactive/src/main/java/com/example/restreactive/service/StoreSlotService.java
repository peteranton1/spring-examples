package com.example.restreactive.service;

import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.model.StoreSlot;
import com.example.restreactive.repository.StoreSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
public class StoreSlotService {

    @Autowired
    private StoreSlotRepository storeSlotRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final ServiceHelper helper = new ServiceHelper();

    public List<StoreSlotDto> findAllSlots() {
        return storeSlotRepository.findAll()
            .stream()
            .map(slot -> (StoreSlotDto) modelMapper.toDto(slot))
            .toList();
    }

    public List<StoreSlotDto> findByStartTimeAndEndTime(ZonedDateTime startTime,
                                                        ZonedDateTime endTime) {
        return storeSlotRepository
            .findAllSlotsBetweenStartTimeAndEndTime(startTime, endTime)
            .stream()
            .map(slot -> (StoreSlotDto) modelMapper.toDto(slot))
            .toList();
    }

    public List<StoreSlotDto> findByStoreCodeAndStartTimeAndEndTime(
        String storeCode,
        ZonedDateTime startTime,
        ZonedDateTime endTime) {

        return storeSlotRepository
            .findAllSlotsByStoreBetweenStartTimeAndEndTime(
                startTime, endTime, storeCode)
            .stream()
            .map(slot -> (StoreSlotDto) modelMapper.toDto(slot))
            .toList();
    }


    public StoreSlotDto upsertAppointmentSlot(StoreSlotDto storeSlotDto) {
        requireNonNull(storeSlotDto);
        requireNonNull(storeSlotDto.getStoreCode());
        requireNonNull(storeSlotDto.getStartTime());
        requireNonNull(storeSlotDto.getEndTime());
        if(Objects.isNull(storeSlotDto.getSlotCode())){
            storeSlotDto.setSlotCode(UUID.randomUUID().toString());
        }
        StoreSlot slotOut = storeSlotRepository.save(storeSlotRepository
            .findAllSlotsByStoreBetweenStartTimeAndEndTime(
                storeSlotDto.getStartTime(),
                storeSlotDto.getEndTime(),
                storeSlotDto.getStoreCode()
                ).stream().findFirst()
            .map(slot -> (StoreSlot) modelMapper.update(slot, storeSlotDto))
            .orElse((StoreSlot) modelMapper.insert(storeSlotDto))
        );
        helper.assertNonNull("slotOut", slotOut);
        return (StoreSlotDto) modelMapper.toDto(slotOut);
    }

    public MessageDto deleteAppointmentSlot(String slotCode) {
        if (Objects.isNull(slotCode)) {
            return MessageDto.builder()
                .code("200")
                .message("Store not specified.")
                .build();
        }
        String storeCode1 = slotCode.toLowerCase();
        storeSlotRepository
            .findAllSlotsBySlotCode(storeCode1)
            .stream().findFirst()
            .ifPresent(store1 -> storeSlotRepository.delete(store1));
        return MessageDto.builder()
            .code("200")
            .message("Store Slot deleted: " + slotCode)
            .build();
    }
}
