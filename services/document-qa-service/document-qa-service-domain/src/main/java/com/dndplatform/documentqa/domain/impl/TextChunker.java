package com.dndplatform.documentqa.domain.impl;

import java.util.List;

public interface TextChunker {

    List<String> chunk(String text);
}
