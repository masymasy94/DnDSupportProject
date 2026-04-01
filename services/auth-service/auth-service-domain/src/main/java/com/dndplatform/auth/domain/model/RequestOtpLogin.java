package com.dndplatform.auth.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record RequestOtpLogin(String email) {
}
