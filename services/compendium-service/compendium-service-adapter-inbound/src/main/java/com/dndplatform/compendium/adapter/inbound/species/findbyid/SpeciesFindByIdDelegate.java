package com.dndplatform.compendium.adapter.inbound.species.findbyid;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.domain.SpeciesFindByIdService;
import com.dndplatform.compendium.domain.model.Species;
import com.dndplatform.compendium.view.model.SpeciesFindByIdResource;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModel;
import com.dndplatform.compendium.view.model.vm.SpeciesViewModelBuilder;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;

@Delegate
@RequestScoped
public class SpeciesFindByIdDelegate implements SpeciesFindByIdResource {

    private final SpeciesFindByIdService service;

    @Inject
    public SpeciesFindByIdDelegate(SpeciesFindByIdService service) {
        this.service = service;
    }

    @Override
    public SpeciesViewModel findById(int id) {
        return service.findById(id)
                .map(this::toViewModel)
                .orElseThrow(() -> new NotFoundException("Species not found with id: " + id));
    }

    private SpeciesViewModel toViewModel(Species species) {
        return SpeciesViewModelBuilder.builder()
                .withId(species.id())
                .withName(species.name())
                .withDescription(species.description())
                .withSource(species.source().name())
                .withIsPublic(species.isPublic())
                .build();
    }
}
