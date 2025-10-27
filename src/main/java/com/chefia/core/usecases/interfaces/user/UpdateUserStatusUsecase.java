package com.chefia.core.usecases.interfaces.user;

public interface UpdateUserStatusUsecase {
    void execute(Long userId, Boolean status);
}
