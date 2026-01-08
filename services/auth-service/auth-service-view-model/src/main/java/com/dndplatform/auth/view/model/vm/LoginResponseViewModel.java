package com.dndplatform.auth.view.model.vm;

public record LoginResponseViewModel(
        String accessToken,
        String refreshToken,
        long accessTokenExpiresAt,
        long refreshTokenExpiresAt,
        String tokenType
) {
    public LoginResponseViewModel(String accessToken, String refreshToken,
                                  long accessTokenExpiresAt, long refreshTokenExpiresAt) {
        this(accessToken, refreshToken, accessTokenExpiresAt, refreshTokenExpiresAt, "Bearer");
    }
}
