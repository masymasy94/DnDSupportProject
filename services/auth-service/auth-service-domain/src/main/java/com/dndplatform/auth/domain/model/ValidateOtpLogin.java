package com.dndplatform.auth.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record ValidateOtpLogin(String email, String otpCode) {
}
