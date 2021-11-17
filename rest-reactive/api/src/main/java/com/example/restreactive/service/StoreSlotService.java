package com.example.restreactive.service;

import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.StoreSlotDto;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.model.StoreSlot;
import com.example.restreactive.repository.StoreSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

@Service
public class StoreSlotService {

    @Autowired
    private StoreSlotRepository storeSlotRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final ServiceHelper serviceHelper = new ServiceHelper();

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

        if (isNull(startTime) || isNull(endTime)) {
            return emptyList();
        }
        return storeSlotRepository
            .findAllSlotsByStoreBetweenStartTimeAndEndTime(
                startTime,
                endTime,
                storeCode)
            .stream()
            .map(slot -> (StoreSlotDto) modelMapper.toDto(slot))
            .toList();
    }

    public List<StoreSlotDto> findByStoreCodeListAndStartTimeAndEndTime(
        List<String> storeCodes,
        ZonedDateTime startTime,
        ZonedDateTime endTime) {

        if (isNull(startTime) || isNull(endTime) ||
            isNull(storeCodes) || storeCodes.isEmpty()) {
            return emptyList();
        }
        return storeSlotRepository
            .findAllSlotsByStoresListBetweenStartTimeAndEndTime(
                startTime,
                endTime,
                storeCodes)
            .stream()
            .map(slot -> (StoreSlotDto) modelMapper.toDto(slot))
            .toList();
    }

    public StoreSlotDto upsertStoreSlot(StoreSlotDto storeSlotDto) {
        verifyStoreSlotDtoForUpdate(storeSlotDto);
        StoreSlot slotOut = storeSlotRepository.save(storeSlotRepository
            .findAllSlotsByStoreCodeAndSlotCode(
                storeSlotDto.getStoreCode(),
                storeSlotDto.getSlotCode()
            ).stream()
            .findFirst()
            .map(slot -> (StoreSlot) modelMapper.update(slot, storeSlotDto))
            .orElse((StoreSlot) modelMapper.insert(storeSlotDto))
        );
        serviceHelper.assertNonNull("slotOut", slotOut);
        return (StoreSlotDto) modelMapper.toDto(slotOut);
    }

    private void verifyStoreSlotDtoForUpdate(StoreSlotDto storeSlotDto) {
        serviceHelper.assertNonNull("storeSlotDto", storeSlotDto);
        serviceHelper.assertNonNull("storeSlotDto.getStoreCode()", storeSlotDto.getStoreCode());
        serviceHelper.assertNonNull("storeSlotDto.getStartTime()", storeSlotDto.getStartTime());
        serviceHelper.assertNonNull("storeSlotDto.getEndTime()", storeSlotDto.getEndTime());
        if (isNull(storeSlotDto.getSlotCode())) {
            storeSlotDto.setSlotCode(UUID.randomUUID().toString());
        }
        storeSlotDto.setSlotCode(storeSlotDto.getSlotCode().toLowerCase());
        storeSlotDto.setStoreCode(storeSlotDto.getStoreCode().toLowerCase());
    }

    public MessageDto deleteAppointmentSlot(
        String storeCode,
        String slotCode
    ) {
        if (isNull(storeCode) || isNull(slotCode)) {
            return MessageDto.builder()
                .code("200")
                .message("Store / Slot not specified.")
                .build();
        }
        String storeCode1 = storeCode.toLowerCase();
        String slotCode1 = slotCode.toLowerCase();
        return storeSlotRepository
            .findAllSlotsByStoreCodeAndSlotCode(storeCode1, slotCode1)
            .stream().findFirst()
            .map(slot -> {
                storeSlotRepository.delete(slot);
                return MessageDto.builder()
                    .code("200")
                    .message("Store Slot deleted: " + storeCode1 + "/" + slotCode)
                    .build();
            })
            .orElse( MessageDto.builder()
            .code("200")
            .message("Store Slot not found: " + storeCode1 + "/" + slotCode)
            .build());

    }
}
