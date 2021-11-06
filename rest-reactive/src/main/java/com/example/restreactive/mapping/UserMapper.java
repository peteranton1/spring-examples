package com.example.restreactive.mapping;

import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.dto.UserDto;
import com.example.restreactive.model.EmailAddress;
import com.example.restreactive.model.EntityObject;
import com.example.restreactive.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
public class UserMapper implements DtoMapper, EntityMapper {

    @Autowired
    private EmailAddressMapper emailAddressMapper;

    @Override
    public boolean accepts(DtoObject dtoObject) {
        return dtoObject instanceof UserDto;
    }

    @Override
    public EntityObject mapTo(DtoObject dtoObject) {
        return userEntityOf((UserDto) dtoObject);
    }

    @Override
    public boolean accepts(EntityObject entityObject) {
        return entityObject instanceof User;
    }

    @Override
    public DtoObject mapTo(EntityObject entityObject) {
        return userDtoOf((User) entityObject);
    }

    @Override
    public EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject) {
        return userUpdate((User) entityObject, (UserDto) dtoObject);
    }

    public User userUpdate(User user, UserDto userDto) {
        Long id = nonNull(user.id()) ?
            user.id() :
            userDto.getId();
        String username = nonNull(user.username()) ?
            user.username() :
            userDto.getUsername();
        String firstName = nonNull(userDto.getFirstName()) ?
            userDto.getFirstName() : user.firstName();
        String lastName = nonNull(userDto.getLastName()) ?
            userDto.getLastName() : user.lastName();
        EmailAddress email = emailAddressMapper
            .emailAddressUpdate(user.email(), userDto.getEmail());
        return new User(
            id,
            username,
            firstName,
            lastName,
            email);
    }

    public User userEntityOf(UserDto userDto) {
        return new User(
            userDto.getId(),
            userDto.getUsername(),
            userDto.getFirstName(),
            userDto.getLastName(),
            emailAddressMapper.emailAddressEntityOf(userDto.getEmail()));
    }

    public UserDto userDtoOf(User user) {
        return UserDto.builder()
            .id(user.id())
            .username(user.username())
            .firstName(user.firstName())
            .lastName(user.lastName())
            .email(emailAddressMapper.emailAddressDtoOf(user.email()))
            .build();
    }
}
