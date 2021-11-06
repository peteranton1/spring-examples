package com.example.restreactive.mapping;

import com.example.restreactive.dto.*;
import com.example.restreactive.model.*;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ModelMapperTest {

    public static final long ID_1 = 1L;
    public static final String TEST = "TEST";
    public static final ZonedDateTime DATE_TIME = ZonedDateTime
        .parse("2019-04-01T16:24:11.252Z");

    @Autowired
    private ModelMapper underTest;

    private List<EntityObject> entities = ImmutableList.of(
        createUser()
        , createCountry()
        , createEmailAddress()
        , createStreetAddress()
        , createAppointmentSlot()
        , createStore()
        , createAppointment()
    );

    private List<DtoObject> dtos = ImmutableList.of(
        createUserDto()
        , createCountryDto()
        , createEmailAddressDto()
        , createStreetAddressDto()
        , createAppointmentSlotDto()
        , createStoreDto()
        , createAppointmentDto()
    );

    @Test
    void whenToDtoValidThenOk() {
        for (int testNo = 0; testNo < entities.size(); testNo++) {
            EntityObject input = entities.get(testNo);
            DtoObject expected = dtos.get(testNo);
            DtoObject actual = underTest.toDto(input);
            System.out.println("Test " + testNo + ": actual: " + input);
            assertEquals(expected, actual);
        }
    }

    @Test
    void whenToDtoInvalidThenException() {
        EntityObject input = new EntityObject() {
        };
        Exception exception = Assertions.assertThrows(
            ApptException.class,
            () -> underTest.toDto(input)
        );
        String expected = "No mapper found for entity of type " +
            "com.example.restreactive.mapping.ModelMapperTest$1";
        assertEquals(expected, exception.getMessage());
    }

    @Test
    void whenToEntityValidThenOk() {
        for (int testNo = 0; testNo < dtos.size(); testNo++) {
            DtoObject input = dtos.get(testNo);
            EntityObject expected = entities.get(testNo);
            EntityObject actual = underTest.toEntity(input);
            System.out.println("Test " + testNo + ": actual: " + input);
            assertEquals(expected, actual);
        }
    }

    @Test
    void whenToEntityInvalidThenException() {
        DtoObject input = new DtoObject() {
        };
        Exception exception = Assertions.assertThrows(
            ApptException.class,
            () -> underTest.toEntity(input)
        );
        String expected = "No mapper found for dto of type " +
            "com.example.restreactive.mapping.ModelMapperTest$2";
        assertEquals(expected, exception.getMessage());
    }

    private UserDto createUserDto() {
        return UserDto.builder()
            .id(ID_1)
            .username(TEST)
            .firstName(TEST)
            .lastName(TEST)
            .email(EmailAddressDto.builder()
                .id(ID_1)
                .email(TEST)
                .build())
            .build();
    }

    private User createUser() {
        return User.builder()
            .id(ID_1)
            .username(TEST)
            .firstName(TEST)
            .lastName(TEST)
            .email(createEmailAddress())
            .build();
    }

    private CountryDto createCountryDto() {
        return CountryDto.builder()
            .id(ID_1)
            .name(TEST)
            .code(TEST)
            .build();
    }

    private Country createCountry() {
        return new Country(ID_1, TEST, TEST);
    }

    private EmailAddressDto createEmailAddressDto() {
        return EmailAddressDto.builder()
            .id(ID_1)
            .email(TEST)
            .build();
    }

    private EmailAddress createEmailAddress() {
        return EmailAddress.builder()
            .id(ID_1)
            .email(TEST)
            .build();
    }

    private StreetAddressDto createStreetAddressDto() {
        return StreetAddressDto.builder()
            .id(ID_1)
            .line1(TEST)
            .line2(TEST)
            .city(TEST)
            .county(TEST)
            .country(createCountryDto())
            .postcode(TEST)
            .build();
    }

    private StreetAddress createStreetAddress() {
        return new StreetAddress(ID_1
            ,TEST
            ,TEST
            ,TEST
            ,TEST
            ,createCountry()
            ,TEST
        );
    }

    private AppointmentSlotDto createAppointmentSlotDto() {
        return AppointmentSlotDto.builder()
            .id(ID_1)
            .startTime(DATE_TIME)
            .endTime(DATE_TIME)
            .build();
    }

    private AppointmentSlot createAppointmentSlot() {
        return new AppointmentSlot(ID_1
            ,DATE_TIME
            ,DATE_TIME
        );
    }

    private StoreDto createStoreDto() {
        return StoreDto.builder()
            .id(ID_1)
            .storeName(TEST)
            .storeCode(TEST)
            .address(createStreetAddressDto())
            .build();
    }

    private Store createStore() {
        return new Store(ID_1
            ,TEST
            ,TEST
            ,createStreetAddress()
        );
    }

    private AppointmentDto createAppointmentDto() {
        return AppointmentDto.builder()
            .id(ID_1)
            .store(createStoreDto())
            .slot(createAppointmentSlotDto())
            .users(ImmutableList.of(createUserDto()))
            .build();
    }

    private Appointment createAppointment() {
        return new Appointment(ID_1
            ,createStore()
            ,createAppointmentSlot()
            ,ImmutableList.of(createUser())
        );
    }

}