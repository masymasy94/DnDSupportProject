package com.dndplatform.user.view.model;

import com.dndplatform.user.view.model.vm.PagedUserViewModel;

public interface UserFindAllResource {
    PagedUserViewModel findAll(int page, int size);
}
