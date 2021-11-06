package com.example.restreactive.mapping;

import com.example.restreactive.dto.AppointmentDto;
import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public class AppointmentMapper implements DtoMapper, EntityMapper {

    @Autowired
    private StoreMapper storeMapper;
    @Autowired
    private AppointmentSlotMapper appointmentSlotMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean accepts(DtoObject dtoObject) {
        return dtoObject instanceof AppointmentDto;
    }

    @Override
    public EntityObject mapTo(DtoObject dtoObject) {
        return appointmentEntityOf((AppointmentDto) dtoObject);
    }

    @Override
    public boolean accepts(EntityObject entityObject) {
        return entityObject instanceof Appointment;
    }

    @Override
    public DtoObject mapTo(EntityObject entityObject) {
        return appointmentDtoOf((Appointment) entityObject);
    }

    @Override
    public EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject) {
        return appointmentUpdate((Appointment) entityObject, (AppointmentDto) dtoObject);
    }

    public Appointment appointmentUpdate(Appointment appointment, AppointmentDto appointmentDto) {
        Long id = nonNull(appointment.getId()) ?
            appointment.getId() :
            appointmentDto.getId();
        Store store = nonNull(appointmentDto.getStore()) ?
            storeMapper.storeEntityOf(appointmentDto.getStore()) :
            appointment.getStore();
        AppointmentSlot slot = nonNull(appointmentDto.getSlot()) ?
            appointmentSlotMapper.appointmentSlotEntityOf(appointmentDto.getSlot()) :
            appointment.getSlot();
        List<User> users = nonNull(appointmentDto.getUsers()) ?
            appointmentDto.getUsers().stream()
                .map(userDto -> userMapper.userEntityOf(userDto))
                .collect(Collectors.toList()) :
            appointment.getUsers();
        return Appointment.builder()
            .id(id)
            .store(store)
            .slot(slot)
            .users(users)
            .build();
    }

    public Appointment appointmentEntityOf(AppointmentDto appointmentDto) {
        return new Appointment(
            appointmentDto.getId()
            , storeMapper.storeEntityOf(appointmentDto.getStore())
            , appointmentSlotMapper.appointmentSlotEntityOf(appointmentDto.getSlot())
            , appointmentDto.getUsers().stream()
            .map(userDto -> userMapper.userEntityOf(userDto))
            .collect(Collectors.toList())
        );
    }

    public AppointmentDto appointmentDtoOf(Appointment appointment) {
        return AppointmentDto.builder()
            .id(appointment.getId())
            .store(storeMapper.storeDtoOf(appointment.getStore()))
            .slot(appointmentSlotMapper.appointmentSlotDtoOf(appointment.getSlot()))
            .users(appointment.getUsers().stream()
                .map(user -> userMapper.userDtoOf(user))
                .collect(Collectors.toList()))
            .build();
    }
}
