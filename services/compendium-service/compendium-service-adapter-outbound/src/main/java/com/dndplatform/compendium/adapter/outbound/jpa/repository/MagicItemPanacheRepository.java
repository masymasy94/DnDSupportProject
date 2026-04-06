package com.dndplatform.compendium.adapter.outbound.jpa.repository;

import com.dndplatform.compendium.adapter.outbound.jpa.entity.MagicItemEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;

@ApplicationScoped
public class MagicItemPanacheRepository implements PanacheRepository<MagicItemEntity> {

    public long countFiltered(String query, Map<String, Object> params) {
        return count(query, params);
    }

    public List<MagicItemEntity> findFiltered(String query, Map<String, Object> params, Sort sort, int page, int pageSize) {
        return find(query, sort, params).page(page, pageSize).list();
    }
}
