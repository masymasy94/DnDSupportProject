package com.dndplatform.compendium.adapter.outbound.jpa.mapper;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.EquipmentEntity;
import com.dndplatform.compendium.domain.model.Equipment;
import com.dndplatform.compendium.domain.model.SourceType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.function.Function;

@Mapper
public interface EquipmentMapper extends Function<EquipmentEntity, Equipment> {

    @Override
    @Mapping(target = "category", source = "equipmentCategory.name")
    @Mapping(target = "source", source = "source", qualifiedByName = "toSourceType")
    @Mapping(target = "isPublic", source = "isPublic", defaultValue = "false")
    Equipment apply(EquipmentEntity entity);

    @Named("toSourceType")
    default SourceType toSourceType(String source) {
        if (source == null) {
            return SourceType.OFFICIAL;
        }
        return SourceType.valueOf(source.toUpperCase());
    }
}
