package com.example.restreactive.service;

import com.example.restreactive.dto.EmailAddressDto;
import com.example.restreactive.dto.UserDto;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService underTest;

    @Test
    void whenFindAllEmptyThenEmpty() {
        List<UserDto> expected = ImmutableList.of();
        List<UserDto> actual = underTest.findAllUsers();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenFindByUsernameEmptyThenEmpty() {
        Optional<UserDto> expected = Optional.empty();
        Optional<UserDto> actual = underTest.findByUsername("non-existent");
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenUpsertUserEmptyThenEmpty() {
        UserDto input = UserDto.builder()
            .username("myusername")
            .email(EmailAddressDto.builder()
                .email("my@user.name")
                .build())
            .build();
        UserDto expected = UserDto.builder()
            .build();
        UserDto actual = underTest.upsertUser(input);
        Assertions.assertEquals(expected, actual);
    }


}