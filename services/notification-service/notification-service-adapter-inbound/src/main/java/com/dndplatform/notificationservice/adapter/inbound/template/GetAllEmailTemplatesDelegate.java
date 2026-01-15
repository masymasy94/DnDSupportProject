package com.dndplatform.notificationservice.adapter.inbound.template;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.notificationservice.adapter.inbound.template.mapper.GetAllEmailTemplatesResponseMapper;
import com.dndplatform.notificationservice.domain.GetAllEmailTemplatesService;
import com.dndplatform.notificationservice.view.model.GetAllEmailTemplatesResource;
import com.dndplatform.notificationservice.view.model.vm.GetAllEmailTemplatesResponseViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import java.util.logging.Logger;

@Delegate
@RequestScoped
public class GetAllEmailTemplatesDelegate implements GetAllEmailTemplatesResource {

    private final Logger log = Logger.getLogger(getClass().getName());
    private final GetAllEmailTemplatesResponseMapper responseMapper;
    private final GetAllEmailTemplatesService getAllEmailTemplatesService;

    @Inject
    public GetAllEmailTemplatesDelegate(GetAllEmailTemplatesResponseMapper responseMapper,
                                        GetAllEmailTemplatesService getAllEmailTemplatesService) {
        this.responseMapper = responseMapper;
        this.getAllEmailTemplatesService = getAllEmailTemplatesService;
    }

    @Override
    public GetAllEmailTemplatesResponseViewModel getAll() {
        log.info(() -> "Getting all email templates");

        var results = getAllEmailTemplatesService.getAll();
        return responseMapper.apply(results);
    }
}
