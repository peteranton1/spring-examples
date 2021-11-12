package com.example.restreactive.controller;

import com.example.restreactive.dto.EmailAddressDto;
import com.example.restreactive.dto.ErrorDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.service.UserService;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = {UserController.class})
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    void whenListAllUsersWithLimit10AndEmptyThenEmpty() {
        when(userService.findAllUsers())
            .thenReturn(emptyList());

        List<?> actual = webTestClient.get()
            .uri("/users/10")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .returnResult().getResponseBody();

        List<?> expected = emptyList();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenListAllUsersWithLimit10And1RecThen1Rec() {
        UserDto userDto = getUserDto(1);
        when(userService.findAllUsers())
            .thenReturn(ImmutableList.of(userDto));

        List<?> actual = webTestClient.get()
            .uri("/users/10")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(List.class)
            .returnResult().getResponseBody();

        List<?> expected = ImmutableList.of(asMapUserDto(userDto));
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenFindUserNotExistsThenNotFound() {
        when(userService.findByUsername("user1"))
            .thenReturn(Optional.empty());

        ErrorDto actual = webTestClient.get()
            .uri("/user/user1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorDto.class)
            .returnResult().getResponseBody();

        ErrorDto expected = ErrorDto.builder()
            .code("404")
            .message("AppointmentException: User not found: user1")
            .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenFindUserExistsThenOk() {
        UserDto userDto = getUserDto(1);
        when(userService.findByUsername("user1"))
            .thenReturn(Optional.of(userDto));

        UserDto actual = webTestClient.get()
            .uri("/user/user1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UserDto.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(userDto, actual);
    }

    @Test
    void whenUpsertUserInsertThenOk() {
        UserDto userDto = getUserDto(1);
        when(userService.upsertUser(userDto))
            .thenReturn(userDto);

        UserDto actual = webTestClient.put()
            .uri("/user")
            .accept(MediaType.APPLICATION_JSON)
            .body(Mono.just(userDto), UserDto.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(UserDto.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(userDto, actual);
    }

    private UserDto getUserDto(int i) {
        String username = "user" + i;
        String emailaddress = username + "@user.user";
        return UserDto.builder()
            .username(username)
            .email(EmailAddressDto.builder()
                .email(emailaddress)
                .build())
            .firstName(username)
            .lastName(username)
            .build();
    }

    private Map<String, Object> asMapUserDto(UserDto userDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (userDto != null) {
            outerMap.put("id", userDto.getId());
            outerMap.put("username", userDto.getUsername());
            outerMap.put("firstName", userDto.getFirstName());
            outerMap.put("lastName", userDto.getLastName());
            outerMap.put("email", asMapEmailDto(userDto.getEmail()));
        }
        return outerMap;
    }

    private Map<String, Object> asMapEmailDto(EmailAddressDto emailDto) {
        Map<String, Object> outerMap = new LinkedHashMap<>();
        if (emailDto != null) {
            outerMap.put("id", emailDto.getId());
            outerMap.put("email", emailDto.getEmail());
        }
        return outerMap;
    }
}