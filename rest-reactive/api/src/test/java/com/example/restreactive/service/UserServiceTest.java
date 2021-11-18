package com.example.restreactive.service;

import com.example.restreactive.dto.EmailAddressDto;
import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.repository.EmailAddressRepository;
import com.example.restreactive.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class UserServiceTest {

    public static final String EMAIL = "my@user.name";

    @Autowired
    private UserService underTest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailAddressRepository emailAddressRepository;

    private EntityDtoCreator creator = new EntityDtoCreator();

    @BeforeEach
    void setUp() {
        deleteAll();
    }

    private void deleteAll() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        deleteAll();
    }

    @Test
    void whenFindAllSizeThenEmpty() {
        assertUsersSize(0);
    }

    @Test
    void whenFindByUsernameEmptyThenEmpty() {
        Optional<UserDto> expected = Optional.empty();
        Optional<UserDto> actual = underTest.findByUsername("non-existent");
        assertEquals(expected, actual);
    }

    @Test
    void whenUpsertUserCreateThenCreate() {

        // Check 0 users in db, initial conditions
        assertUsersSize(0);

        // Step 1 - save
        EmailAddressDto emailAddressDto = creator.createEmailAddressDto(EMAIL);
        UserDto userDto = creator.createUserDto(emailAddressDto);
        UserDto expected = creator.createUserDto(emailAddressDto);
        UserDto actual = underTest.upsertUser(userDto);
        // we don't know the id that will be created so set it to
        // whatever it was so following assertEquals works
        expected.setId(actual.getId());
        assertEquals(expected, actual);

        // Check 1 in db, therefore created
        assertUsersSize(1);
    }

    @Test
    void whenUpsertUserUpdateThenUpdate() {

        // Check 0 in db, initial conditions
        assertUsersSize(0);

        // Step 1 - create
        EmailAddressDto emailAddressDto = creator.createEmailAddressDto(EMAIL);
        UserDto userDto = creator.createUserDto(emailAddressDto);
        UserDto expected = creator.createUserDto(emailAddressDto);
        UserDto actual1 = underTest.upsertUser(userDto);
        expected.setId(actual1.getId());
        assertEquals(expected, actual1);

        // Check 1 in db, therefore created
        assertUsersSize(1);

        // Step 3 - update
        UserDto actual2 = underTest.upsertUser(userDto);
        assertEquals(expected, actual2);

        // Check 2 in db, therefore updated
        assertUsersSize(1);
    }

    @Test
    void whenDeleteUserExistsThenDelete() {

        // Check 0 in db, initial conditions
        assertUsersSize(0);

        // Step 1 - create
        EmailAddressDto emailAddressDto = creator.createEmailAddressDto(EMAIL);
        UserDto userDto = creator.createUserDto(emailAddressDto);
        UserDto expected = creator.createUserDto(emailAddressDto);
        UserDto actual1 = underTest.upsertUser(userDto);
        expected.setId(actual1.getId());
        assertEquals(expected, actual1);

        // Check 1 in db, therefore created
        assertUsersSize(1);

        // Step 3 - delete
        String username = userDto.getUsername();
        MessageDto expected2 = MessageDto.builder()
            .code("200")
            .message("User deleted: " + username)
            .build();
        MessageDto actual2 = underTest.deleteUser(username);
        assertEquals(expected2, actual2);

        // Check in db, therefore deleted
        assertUsersSize(0);
    }

    private void assertUsersSize(int expectedSize) {
        List<UserDto> users = underTest.findAllUsers();
        assertEquals(expectedSize, users.size());
    }

}