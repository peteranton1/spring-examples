package com.example.restreactive.mapping;

import com.example.restreactive.dto.AppointmentSlotDto;
import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.dto.EmailAddressDto;
import com.example.restreactive.dto.StoreDto;
import com.example.restreactive.model.*;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

import static java.util.Objects.nonNull;

@Component
public class AppointmentSlotMapper implements DtoMapper, EntityMapper{

    @Override
    public boolean accepts(DtoObject dtoObject) {
        return dtoObject instanceof AppointmentSlotDto;
    }

    @Override
    public EntityObject mapTo(DtoObject dtoObject) {
        return appointmentSlotEntityOf((AppointmentSlotDto) dtoObject);
    }

    @Override
    public boolean accepts(EntityObject entityObject) {
        return entityObject instanceof AppointmentSlot;
    }

    @Override
    public DtoObject mapTo(EntityObject entityObject) {
        return appointmentSlotDtoOf((AppointmentSlot) entityObject);
    }

    @Override
    public EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject) {
        return appointmentSlotUpdate((AppointmentSlot) entityObject, (AppointmentSlotDto) dtoObject);
    }

    public AppointmentSlot appointmentSlotUpdate(AppointmentSlot appointmentSlot, AppointmentSlotDto appointmentSlotDto) {
        Long id = nonNull(appointmentSlot.id()) ?
            appointmentSlot.id() :
            appointmentSlotDto.getId();
        ZonedDateTime startTime = nonNull(appointmentSlotDto.getStartTime()) ?
            appointmentSlotDto.getStartTime() :
            appointmentSlot.startTime();
        ZonedDateTime endTime = nonNull(appointmentSlotDto.getEndTime()) ?
            appointmentSlotDto.getEndTime() :
            appointmentSlot.endTime();
        return new AppointmentSlot(
            id
            ,startTime
            ,endTime);
    }

    public AppointmentSlot appointmentSlotEntityOf(AppointmentSlotDto appointmentSlotDto) {
        return new AppointmentSlot(
            appointmentSlotDto.getId()
            ,appointmentSlotDto.getStartTime()
            ,appointmentSlotDto.getEndTime()
        );
    }

    public AppointmentSlotDto appointmentSlotDtoOf(AppointmentSlot appointmentSlot) {
        return AppointmentSlotDto.builder()
            .id(appointmentSlot.id())
            .startTime(appointmentSlot.startTime())
            .endTime(appointmentSlot.endTime())
            .build();
    }
}
