package com.supernova.ssagrissakssak.feed.controller.request;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class SignInRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("이메일과 비밀번호 형식이 올바를 경우 request dto가 생성된다.")
    void testValidSignInRequest() {
        SignInRequest request = new SignInRequest("test@example.com", "password123");
        var violations = validator.validate(request);
        assertThat(violations).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "test@", "@example.com"})
    @DisplayName("이메일 형식이 올바르지 않을 경우 예외가 발생한다.")
    void emailValidate(String email) {
        SignInRequest request = new SignInRequest(email, "password123");
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("email") &&
                                violation.getMessage().contains("이메일 형식이 올바르지 않습니다.")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("이메일은 비어있을 수 없다.")
    void emailValidate2(String email) {
        SignInRequest request = new SignInRequest(email, "password123");
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("email") &&
                                violation.getMessage().contains("이메일은 필수입니다.")
                );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("비밀번호는 비어있을 수 없다.")
    void passwordValidate(String password) {
        SignInRequest request = new SignInRequest("test@example.com", password);
        var violations = validator.validate(request);
        assertThat(violations)
                .isNotEmpty()
                .anyMatch(violation ->
                        violation.getPropertyPath().toString().equals("password") &&
                                violation.getMessage().contains("비밀번호는 필수입니다.")
                );
    }
}