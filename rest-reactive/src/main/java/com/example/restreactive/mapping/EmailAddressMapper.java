package com.example.restreactive.mapping;

import com.example.restreactive.dto.DtoObject;
import com.example.restreactive.dto.EmailAddressDto;
import com.example.restreactive.model.EmailAddress;
import com.example.restreactive.model.EntityObject;
import org.springframework.stereotype.Component;

import static java.util.Objects.*;

@Component
public class EmailAddressMapper implements DtoMapper, EntityMapper {

    @Override
    public boolean accepts(DtoObject dtoObject) {
        return dtoObject instanceof EmailAddressDto;
    }

    @Override
    public EntityObject mapTo(DtoObject dtoObject) {
        return emailAddressEntityOf((EmailAddressDto) dtoObject);
    }

    @Override
    public boolean accepts(EntityObject entityObject) {
        return entityObject instanceof EmailAddress;
    }

    @Override
    public DtoObject mapTo(EntityObject entityObject) {
        return emailAddressDtoOf((EmailAddress) entityObject);
    }

    @Override
    public EntityObject updateEntity(EntityObject entityObject, DtoObject dtoObject) {
        return emailAddressUpdate((EmailAddress) entityObject, (EmailAddressDto) dtoObject);
    }

    public EmailAddress emailAddressUpdate(EmailAddress emailAddress, EmailAddressDto emailAddressDto) {
        Long id = nonNull(emailAddress.getId()) ?
            emailAddress.getId() :
            emailAddressDto.getId();
        String email = nonNull(emailAddressDto.getEmail()) ?
            emailAddressDto.getEmail() :
            emailAddress.getEmail();
        return EmailAddress.builder()
            .id(id)
            .email(email)
            .build();
    }

    public EmailAddress emailAddressEntityOf(EmailAddressDto emailAddressDto) {
        if(isNull(emailAddressDto)) {
            return null;
        }
        return EmailAddress.builder()
            .id(emailAddressDto.getId())
            .email(emailAddressDto.getEmail())
            .build();
    }

    public EmailAddressDto emailAddressDtoOf(EmailAddress emailAddress) {
        if(isNull(emailAddress)) {
            return null;
        }
        return EmailAddressDto.builder()
            .id(emailAddress.getId())
            .email(emailAddress.getEmail())
            .build();
    }
}
