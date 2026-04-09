package com.dndplatform.test.entity;

import com.dndplatform.common.test.InjectRandom;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class DeleteEntitiesExtensionWhereClauseTest {

    @NamedParam
    @InjectRandom
    private String username;

    @NamedParam("custom_name")
    @InjectRandom
    private Long entityId;

    @Test
    void namedParamShouldDefaultToFieldName() throws Exception {
        var field = getClass().getDeclaredField("username");
        var annotation = field.getAnnotation(NamedParam.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEmpty();
        assertThat(DeleteEntitiesExtension.resolveParamName(field)).isEqualTo("username");
    }

    @Test
    void namedParamShouldUseExplicitValueWhenProvided() throws Exception {
        var field = getClass().getDeclaredField("entityId");
        var annotation = field.getAnnotation(NamedParam.class);

        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo("custom_name");
        assertThat(DeleteEntitiesExtension.resolveParamName(field)).isEqualTo("custom_name");
    }

    @Test
    void shouldCollectNamedParamFieldsFromTestInstance() {
        var params = DeleteEntitiesExtension.collectNamedParams(this);

        assertThat(params).containsKeys("username", "custom_name");
        assertThat(params.get("username")).isEqualTo(username);
        assertThat(params.get("custom_name")).isEqualTo(entityId);
    }
}
