package com.dndplatform.auth.view.model.vm;

import com.dndplatform.common.annotations.Builder;
import jakarta.json.bind.annotation.JsonbPropertyOrder;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@JsonbPropertyOrder({"username","password"})
@Builder
public record CreateLoginTokensViewModel(@Schema(description = "username", examples = "masy") String username,
                                         @Schema(description = "password", examples = "psw123!") String password) {
}

