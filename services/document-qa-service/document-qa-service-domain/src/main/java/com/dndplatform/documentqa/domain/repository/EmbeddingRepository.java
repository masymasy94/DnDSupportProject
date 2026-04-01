package com.dndplatform.documentqa.domain.repository;

import java.util.List;

public interface EmbeddingRepository {

    float[] embed(String text);

    List<float[]> embedAll(List<String> texts);
}
