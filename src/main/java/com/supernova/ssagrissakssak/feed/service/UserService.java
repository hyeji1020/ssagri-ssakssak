package com.supernova.ssagrissakssak.feed.service;

import com.supernova.ssagrissakssak.core.exception.UserNotFoundException;
import com.supernova.ssagrissakssak.core.exception.UserRegistrationException;
import com.supernova.ssagrissakssak.core.security.JwtProvider;
import com.supernova.ssagrissakssak.feed.controller.request.ApproveRequest;
import com.supernova.ssagrissakssak.feed.controller.request.SignInRequest;
import com.supernova.ssagrissakssak.feed.controller.response.TokenResponse;
import com.supernova.ssagrissakssak.feed.persistence.repository.UserRepository;
import com.supernova.ssagrissakssak.feed.persistence.repository.entity.UserEntity;
import com.supernova.ssagrissakssak.feed.service.delegator.ApproveValidateDelegator;
import com.supernova.ssagrissakssak.feed.service.delegator.validator.SignInValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApproveValidateDelegator approveValidateDelegator;
    private final SignInValidator signInValidator;
    private final JwtProvider jwtProvider;

    @Transactional
    public Long join(UserEntity user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new UserRegistrationException();
        }

        user.encodePassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user).getId();
    }

    @Transactional
    public void approve(ApproveRequest approveModel) {
        UserEntity user = userRepository.findByEmail(approveModel.email()).orElseThrow(UserNotFoundException::new);
        approveValidateDelegator.validate(user, approveModel);
        user.changeActiveStatus();
    }

    @Transactional
    public TokenResponse signIn(SignInRequest signInRequest) {
        UserEntity user = userRepository.findByEmailAndActiveStatus(signInRequest.email(), true).orElseThrow(UserNotFoundException::new);
        signInValidator.validate(user, signInRequest);
        TokenResponse token = jwtProvider.createTokenResponse(user.getEmail());
        user.registerRefreshToken(token.refreshToken());
        return token;
    }
}