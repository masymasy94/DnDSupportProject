package com.dndplatform.auth.adapter.inbound.passwordreset.request.mapper;

import com.dndplatform.auth.view.model.vm.RequestPasswordResetViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class RequestPasswordResetMapperTest {

    private final RequestPasswordResetMapper sut = Mappers.getMapper(RequestPasswordResetMapper.class);

    @Test
    void shouldMapToRequestPasswordReset(@Random RequestPasswordResetViewModel vm) {
        var result = sut.apply(vm);

        assertThat(result.email()).isEqualTo(vm.email());
    }
}
