package com.example.restreactive.controller;

import com.example.restreactive.controller.UserController;
import com.example.restreactive.dto.EmailAddressDto;
import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.mapping.EmailAddressMapper;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.mapping.UserMapper;
import com.example.restreactive.model.EmailAddress;
import com.example.restreactive.model.User;
import com.example.restreactive.repository.EmailAddressRepository;
import com.example.restreactive.repository.UserRepository;
import com.example.restreactive.service.UserService;
import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {UserController.class})
@Import({UserService.class,
    ModelMapper.class,
    UserMapper.class,
    EmailAddressMapper.class
})
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ModelMapper modelMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private EmailAddressRepository emailAddressRepository;

    @Test
    void whenListAllUsersWithLimit10AndEmptyThenEmpty() {
        when(userRepository.findAll())
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
        User user = (User)modelMapper.toEntity(userDto);
        when(userRepository.findAll())
            .thenReturn(ImmutableList.of(user));

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
        when(userRepository.findByUsername("user1"))
            .thenReturn(emptyList());

        MessageDto actual = webTestClient.get()
            .uri("/user/user1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(MessageDto.class)
            .returnResult().getResponseBody();

        MessageDto expected = MessageDto.builder()
            .code("404")
            .message("AppointmentException: User not found: user1")
            .build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenFindUserExistsThenOk() {
        UserDto userDto = getUserDto(1);
        User user = (User)modelMapper.toEntity(userDto);
        when(userRepository.findByUsername("user1"))
            .thenReturn(ImmutableList.of(user));

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
        User user = (User)modelMapper.toEntity(userDto);
        when(userRepository.findByUsername("user1"))
            .thenReturn(emptyList());
        when(userRepository.save(any()))
            .thenReturn(user);
        EmailAddress emailAddress = (EmailAddress)modelMapper.toEntity(userDto.getEmail());
        when(emailAddressRepository.findByEmail(any()))
            .thenReturn(ImmutableList.of(emailAddress));
        when(emailAddressRepository.save(any()))
            .thenReturn(emailAddress);

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

    @Test
    void whenDeleteUserNotExistsThenOk() {
        String username = "user1";
        MessageDto messageDto = MessageDto.builder()
            .code("200")
            .message("User not found: " + username)
            .build();
        when(userRepository.findByUsername("user1"))
            .thenReturn(emptyList());

        MessageDto actual = webTestClient.delete()
            .uri("/user/" + username)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(MessageDto.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(messageDto, actual);
    }

    @Test
    void whenDeleteUserExistsThenOk() {
        String username = "user1";
        MessageDto messageDto = MessageDto.builder()
            .code("200")
            .message("User deleted: " + username)
            .build();
        User user = User.builder().username(username).build();
        when(userRepository.findByUsername("user1"))
            .thenReturn(ImmutableList.of(user));

        MessageDto actual = webTestClient.delete()
            .uri("/user/" + username)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody(MessageDto.class)
            .returnResult().getResponseBody();

        Assertions.assertEquals(messageDto, actual);
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