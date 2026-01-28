package com.dndplatform.asset.domain.repository;

import com.dndplatform.asset.domain.model.DocumentListItem;

import java.util.List;

public interface DocumentListRepository {

    List<DocumentListItem> listAll();
}
