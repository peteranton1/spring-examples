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
        Long id = nonNull(user.getId()) ?
            user.getId():
            userDto.getId();
        String username = nonNull(user.getUsername()) ?
            user.getUsername() :
            userDto.getUsername();
        String firstName = nonNull(userDto.getFirstName()) ?
            userDto.getFirstName() : user.getFirstName();
        String lastName = nonNull(userDto.getLastName()) ?
            userDto.getLastName() : user.getLastName();
        EmailAddress email = emailAddressMapper
            .emailAddressUpdate(user.getEmail(), userDto.getEmail());
        return User.builder()
            .id(id)
            .username(username)
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .build();
    }

    public User userEntityOf(UserDto userDto) {
        return User.builder()
            .id(userDto.getId())
            .username(userDto.getUsername())
            .firstName(userDto.getFirstName())
            .lastName(userDto.getLastName())
            .email(emailAddressMapper.emailAddressEntityOf(userDto.getEmail()))
            .build();
    }

    public UserDto userDtoOf(User user) {
        return UserDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(emailAddressMapper.emailAddressDtoOf(user.getEmail()))
            .build();
    }
}
