package com.dndplatform.auth.adapter.outbound.messaging;

import com.dndplatform.auth.domain.repository.OtpLoginEmailSendRepository;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModel;
import com.dndplatform.notificationservice.view.model.vm.EmailSendRequestViewModelBuilder;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class OtpLoginEmailSendRepositoryRabbitMq implements OtpLoginEmailSendRepository {

    private static final Long OTP_LOGIN_TEMPLATE_ID = 3L;

    private final Logger log = Logger.getLogger(getClass().getName());
    private final Emitter<EmailSendRequestViewModel> emitter;

    @Inject
    public OtpLoginEmailSendRepositoryRabbitMq(@Channel("email-requests-out") Emitter<EmailSendRequestViewModel> emitter) {
        this.emitter = emitter;
    }

    @Override
    public void sendOtpLoginEmail(String email, String otpCode) {
        log.info(() -> "Publishing OTP login email request for: %s".formatted(email));

        var payload = EmailSendRequestViewModelBuilder.builder()
                .withTo(email)
                .withTemplateId(OTP_LOGIN_TEMPLATE_ID)
                .withTemplateVariables(Map.of("otpCode", otpCode))
                .build();

        emitter.send(payload);
    }
}
