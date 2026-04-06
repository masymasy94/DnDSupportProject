package com.dndplatform.auth.adapter.inbound.otplogin.validate.mapper;

import com.dndplatform.auth.view.model.vm.ValidateOtpLoginViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class ValidateOtpLoginMapperTest {

    private final ValidateOtpLoginMapper sut = Mappers.getMapper(ValidateOtpLoginMapper.class);

    @Test
    void shouldMapToValidateOtpLogin(@Random ValidateOtpLoginViewModel vm) {
        var result = sut.apply(vm);

        assertThat(result.email()).isEqualTo(vm.email());
        assertThat(result.otpCode()).isEqualTo(vm.otpCode());
    }
}
