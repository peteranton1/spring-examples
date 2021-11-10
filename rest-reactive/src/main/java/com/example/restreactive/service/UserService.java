package com.example.restreactive.service;

import com.example.restreactive.dto.EmailAddressDto;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.model.EmailAddress;
import com.example.restreactive.model.User;
import com.example.restreactive.repository.EmailAddressRepository;
import com.example.restreactive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailAddressRepository emailAddressRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(user -> (UserDto) modelMapper.toDto(user))
            .toList();
    }

    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
            .stream().findFirst()
            .map(user -> (UserDto) modelMapper.toDto(user));
    }

    public UserDto upsertUser(UserDto userDto) {
        requireNonNull(userDto);
        requireNonNull(userDto.getUsername());
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

    public Long upsertEmailAddress(EmailAddressDto emailAddressDto) {
        requireNonNull(emailAddressDto);
        requireNonNull(emailAddressDto.getEmail());
        EmailAddress emailOut = emailAddressRepository.save(emailAddressRepository
            .findByEmail(emailAddressDto.getEmail())
            .stream().findFirst()
            .map(email -> (EmailAddress) modelMapper.update(email, emailAddressDto))
            .orElse((EmailAddress) modelMapper.insert(emailAddressDto))
        );
        return emailOut.getId();
    }
}
