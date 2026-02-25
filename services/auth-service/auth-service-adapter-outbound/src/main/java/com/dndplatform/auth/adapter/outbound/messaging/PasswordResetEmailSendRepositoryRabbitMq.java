package com.dndplatform.auth.adapter.outbound.messaging;

import com.dndplatform.auth.domain.repository.PasswordResetEmailSendRepository;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class PasswordResetEmailSendRepositoryRabbitMq implements PasswordResetEmailSendRepository {

    private static final Long PASSWORD_RESET_TEMPLATE_ID = 2L;

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Emitter<EmailSendRequestViewModel> emitter;

    @Inject
    public PasswordResetEmailSendRepositoryRabbitMq(@Channel("email-requests-out") Emitter<EmailSendRequestViewModel> emitter) {
        this.emitter = emitter;
    }

    @Override
    public void sendResetEmail(String email, String token) {
        log.info(() -> "Publishing password reset email request for: %s".formatted(email));

        var payload = EmailSendRequestViewModelBuilder.builder()
                .withTo(email)
                .withTemplateId(PASSWORD_RESET_TEMPLATE_ID)
                .withTemplateVariables(Map.of("otpCode", token))
                .build();

        emitter.send(payload);
    }
}
