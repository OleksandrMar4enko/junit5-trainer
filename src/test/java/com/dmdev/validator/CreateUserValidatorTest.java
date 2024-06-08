package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateUserValidatorTest {

    private final CreateUserValidator validator = CreateUserValidator.getInstance();

    @Test
    void shouldPassValidation() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Oleksandr")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertFalse(actualResult.isValid());
    }

    @Test
    void invalidBirthday() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Oleksandr")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01 12:23")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.birthday");
    }

    @Test
    void invalidGender() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Oleksandr")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01")
                .role(Role.USER.name())
                .gender("Fake")
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.gender");
    }

    @Test
    void invalidRole() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Oleksandr")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01")
                .role("Fake")
                .gender(Gender.MALE.name())
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(1);
        assertThat(actualResult.getErrors().get(0).getCode()).isEqualTo("invalid.role");
    }

    @Test
    void invalidRoleGenderBirthday() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Oleksandr")
                .email("test@gmail.com")
                .password("123")
                .birthday("2000-01-01 21:15")
                .role("Fake")
                .gender("Fake")
                .build();

        ValidationResult actualResult = validator.validate(dto);

        assertThat(actualResult.getErrors()).hasSize(3);
        List<String> errorCodes = actualResult.getErrors().stream()
                .map(Error::getCode)
                .toList();

        assertThat(errorCodes).contains("invalid.birthday", "invalid.role", "invalid.gender");
    }



}