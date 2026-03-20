package com.dndplatform.user.view.model;

import com.dndplatform.user.view.model.vm.PagedUserViewModel;

public interface UserSearchResource {
    PagedUserViewModel search(String query, int page, int size);
}
