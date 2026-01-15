package com.dndplatform.user.view.model;

import com.dndplatform.user.view.model.vm.UserViewModel;

public interface UserFindByIdResource {
    UserViewModel findById(long id);
}
