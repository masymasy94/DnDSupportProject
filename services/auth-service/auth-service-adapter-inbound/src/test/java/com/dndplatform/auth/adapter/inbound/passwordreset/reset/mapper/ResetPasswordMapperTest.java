package com.dndplatform.auth.adapter.inbound.passwordreset.reset.mapper;

import com.dndplatform.auth.view.model.vm.ResetPasswordViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ResetPasswordMapperTest {

    private final ResetPasswordMapper sut = Mappers.getMapper(ResetPasswordMapper.class);

    @Test
    void shouldMapToResetPassword(@Random ResetPasswordViewModel vm) {
        var result = sut.apply(vm);

        assertThat(result.token()).isEqualTo(vm.token());
        assertThat(result.newPassword()).isEqualTo(vm.newPassword());
    }
}
