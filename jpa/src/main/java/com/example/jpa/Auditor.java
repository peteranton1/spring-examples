package com.example.jpa;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class Auditor implements AuditorAware<String> {

    private final String user;

    public Auditor(@Value("${user.name}") String user){
        this.user = user;
    }

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(this.user);
    }
}
