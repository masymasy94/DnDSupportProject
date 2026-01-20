package com.dndplatform.compendium.adapter.inbound.equipment.findall.mapper;

import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.view.model.vm.EquipmentViewModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.function.Function;

@Mapper
public interface EquipmentViewModelMapper extends Function<Equipment, EquipmentViewModel> {

    @Override
    @Mapping(target = "source", expression = "java(equipment.source() != null ? equipment.source().name() : null)")
    EquipmentViewModel apply(Equipment equipment);
}
