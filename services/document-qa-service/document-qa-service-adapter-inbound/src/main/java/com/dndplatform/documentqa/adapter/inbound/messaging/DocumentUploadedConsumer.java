package com.dndplatform.documentqa.adapter.inbound.messaging;

import com.dndplatform.documentqa.view.model.vm.DocumentUploadedEventVm;

import org.eclipse.microprofile.reactive.messaging.Message;

import java.util.concurrent.CompletionStage;

public interface DocumentUploadedConsumer {

    CompletionStage<Void> consume(Message<DocumentUploadedEventVm> message);
}
