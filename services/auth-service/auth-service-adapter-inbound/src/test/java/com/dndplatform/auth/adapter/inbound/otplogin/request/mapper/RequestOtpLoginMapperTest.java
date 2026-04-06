package com.dndplatform.auth.adapter.inbound.otplogin.request.mapper;

import com.dndplatform.auth.view.model.vm.RequestOtpLoginViewModel;
import com.dndplatform.common.test.Random;
import com.dndplatform.common.test.RandomExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(RandomExtension.class)
class RequestOtpLoginMapperTest {

    private final RequestOtpLoginMapper sut = Mappers.getMapper(RequestOtpLoginMapper.class);

    @Test
    void shouldMapToRequestOtpLogin(@Random RequestOtpLoginViewModel vm) {
        var result = sut.apply(vm);

        assertThat(result.email()).isEqualTo(vm.email());
    }
}
