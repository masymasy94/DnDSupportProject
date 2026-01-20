package com.dndplatform.notificationservice.domain.impl;

import com.dndplatform.notificationservice.domain.GetAllEmailTemplatesService;
import com.dndplatform.notificationservice.domain.model.EmailTemplateResult;
import com.dndplatform.notificationservice.domain.repository.FindAllEmailTemplatesRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class GetAllEmailTemplatesServiceImpl implements GetAllEmailTemplatesService {

    private final FindAllEmailTemplatesRepository findAllEmailTemplatesRepository;

    @Inject
    public GetAllEmailTemplatesServiceImpl(FindAllEmailTemplatesRepository findAllEmailTemplatesRepository) {
        this.findAllEmailTemplatesRepository = findAllEmailTemplatesRepository;
    }

    @Override
    public List<EmailTemplateResult> getAll() {
        return findAllEmailTemplatesRepository.findAll();
    }
}
