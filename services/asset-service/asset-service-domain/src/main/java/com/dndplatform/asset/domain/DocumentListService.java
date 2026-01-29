package com.dndplatform.asset.domain;

import com.dndplatform.asset.domain.model.DocumentListItem;

import java.util.List;

public interface DocumentListService {

    List<DocumentListItem> listAll();
}
