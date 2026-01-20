package com.dndplatform.compendium.adapter.inbound.equipment.findall;

import com.dndplatform.common.annotations.Delegate;
import com.dndplatform.compendium.adapter.inbound.equipment.findall.mapper.EquipmentViewModelMapper;
import com.dndplatform.compendium.domain.EquipmentFindAllService;
import com.dndplatform.compendium.domain.filter.EquipmentFilterCriteria;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.PagedResult;
import com.dndplatform.compendium.view.model.EquipmentFindAllResource;
import com.dndplatform.compendium.view.model.vm.PagedEquipmentViewModel;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.jspecify.annotations.NonNull;

@Delegate
@RequestScoped
public class EquipmentFindAllDelegate implements EquipmentFindAllResource {

    private final EquipmentFindAllService service;
    private final EquipmentViewModelMapper mapper;

    @Inject
    public EquipmentFindAllDelegate(EquipmentFindAllService service,
                                    EquipmentViewModelMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public PagedEquipmentViewModel findAll(String name, String category,
                                           Integer page, Integer pageSize) {
        var criteria = getEquipmentFilterCriteria(name, category, page, pageSize);
        var result = service.findAll(criteria);
        return getPagedResult(result);
    }

    private @NonNull PagedEquipmentViewModel getPagedResult(PagedResult<Equipment> result) {
        return new PagedEquipmentViewModel(
                result.content().stream().map(mapper).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }

    private static @NonNull EquipmentFilterCriteria getEquipmentFilterCriteria(
            String name, String category, Integer page, Integer pageSize) {
        return new EquipmentFilterCriteria(
                name,
                category,
                page != null ? page : EquipmentFilterCriteria.DEFAULT_PAGE,
                pageSize != null ? pageSize : EquipmentFilterCriteria.DEFAULT_PAGE_SIZE
        );
    }
}
