package com.chefia.core.usecases.interfaces.user;

import com.chefia.users.model.ChangePasswordDTO;

public interface UpdateUserPasswordUsecase {
    void execute(Long userId, ChangePasswordDTO changePasswordDTO);
}
