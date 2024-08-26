package com.supernova.ssagrissakssak.feed.controller.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank(message = "refresh token은 필수입니다.")
        String refreshToken
) {
}