package com.dndplatform.user.domain;

public interface UserUpdatePasswordService {
    void updatePassword(long id, String newPassword);
}
