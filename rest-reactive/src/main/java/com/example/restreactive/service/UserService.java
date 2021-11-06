package com.example.restreactive.service;

import com.example.restreactive.dto.UserDto;
import com.example.restreactive.mapping.ModelMapper;
import com.example.restreactive.model.User;
import com.example.restreactive.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Component
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
            .stream()
            .map(user -> (UserDto)modelMapper.toDto(user))
            .toList();
    }

    public Optional<UserDto> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> (UserDto)modelMapper.toDto(user));
    }

    public UserDto upsertUser(UserDto userDto) {
        requireNonNull(userDto);
        requireNonNull(userDto.getUsername());
        User userOut = userRepository.save(userRepository
            .findByUsername(userDto.getUsername())
            .map(user -> (User)modelMapper.update(user, userDto))
            .orElse((User)modelMapper.insert(userDto))
        );
        return (UserDto)modelMapper.toDto(userOut);
    }
}
