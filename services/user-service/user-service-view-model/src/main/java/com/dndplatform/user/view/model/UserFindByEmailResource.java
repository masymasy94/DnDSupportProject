package com.dndplatform.user.view.model;

import com.dndplatform.user.view.model.vm.UserFindByEmailViewModel;
import com.dndplatform.user.view.model.vm.UserViewModel;
import jakarta.validation.Valid;

public interface UserFindByEmailResource {
    UserViewModel findByEmail(@Valid UserFindByEmailViewModel vm);
}
