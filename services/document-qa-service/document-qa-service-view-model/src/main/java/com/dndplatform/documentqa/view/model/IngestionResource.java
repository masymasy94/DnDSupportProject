package com.dndplatform.documentqa.view.model;

import com.dndplatform.documentqa.view.model.vm.IngestionStatusViewModel;

public interface IngestionResource {

    IngestionStatusViewModel getStatus(String documentId);

    void triggerIngestion(String documentId, Long userId);
}
