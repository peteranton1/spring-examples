package com.example.restreactive.service;

import com.example.restreactive.dto.EmailAddressDto;
import com.example.restreactive.dto.MessageDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.mapping.AppointmentException;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.model.EmailAddress;
import com.example.restreactive.model.User;
import com.example.restreactive.repository.EmailAddressRepository;
import com.example.restreactive.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailAddressRepository emailAddressRepository;

    @Autowired
    private ModelMapper modelMapper;

    private final ServiceHelper helper = new ServiceHelper();

    public List<UserDto> findAllUsers() {
        List<UserDto> userDtos = userRepository.findAll()
            .stream()
            .map(user -> (UserDto) modelMapper.toDto(user))
            .toList();
        userDtos.forEach(userDto ->
            log.info("Retrieved user from db : {}", userDto));
        log.info("Retrieved {} users from db", userDtos.size());
        return userDtos;
    }

    public Optional<UserDto> findByUsername(String username) {
        helper.assertNonNull("username", username);
        return userRepository.findByUsername(username.toLowerCase())
            .stream().findFirst()
            .map(user -> (UserDto) modelMapper.toDto(user));
    }

    public UserDto upsertUser(UserDto userDto) {
        helper.assertNonNull("userDto", userDto);
        helper.assertNonNull("userDto.getUsername()", userDto.getUsername());
        userDto.setUsername(userDto.getUsername().toLowerCase());
        EmailAddressDto emailAddressDto = userDto.getEmail();
        if (nonNull(emailAddressDto)) {
            emailAddressDto.setId(upsertEmailAddress(emailAddressDto));
        }
        User userOut = userRepository.save(userRepository
            .findByUsername(userDto.getUsername())
            .stream().findFirst()
            .map(user -> (User) modelMapper.update(user, userDto))
            .orElse((User) modelMapper.insert(userDto))
        );
        return (UserDto) modelMapper.toDto(userOut);
    }

    public Integer upsertEmailAddress(EmailAddressDto emailAddressDto) {
        helper.assertNonNull("emailAddressDto", emailAddressDto);
        helper.assertNonNull("emailAddressDto.getEmail()", emailAddressDto.getEmail());
        emailAddressDto.setEmail(emailAddressDto.getEmail().toLowerCase());
        EmailAddress emailOut = emailAddressRepository.save(emailAddressRepository
            .findByEmail(emailAddressDto.getEmail().toLowerCase())
            .stream().findFirst()
            .map(email -> (EmailAddress) modelMapper.update(email, emailAddressDto))
            .orElse((EmailAddress) modelMapper.insert(emailAddressDto))
        );
        return emailOut.getId();
    }

    public MessageDto deleteUser(String username) {
        if (Objects.isNull(username)) {
            return MessageDto.builder()
                .code("200")
                .message("User not specified.")
                .build();
        }
        String username1 = username.toLowerCase();
        return userRepository
            .findByUsername(username1)
            .stream().findFirst()
            .map(user1 -> {
                userRepository.delete(user1);
                return MessageDto.builder()
                    .code("200")
                    .message("User deleted: " + username)
                    .build();
            })
            .orElse(MessageDto.builder()
                .code("200")
                .message("User not found: " + username)
                .build());

    }

}
