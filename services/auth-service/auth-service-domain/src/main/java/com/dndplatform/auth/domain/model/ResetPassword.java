package com.dndplatform.auth.domain.model;

import com.dndplatform.common.annotations.Builder;

@Builder
public record ResetPassword(String token, String newPassword) {
}
