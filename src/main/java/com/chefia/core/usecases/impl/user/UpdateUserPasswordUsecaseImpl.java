package com.chefia.core.usecases.impl.user;

import com.chefia.core.exceptions.PasswordAlreadyUsed;
import com.chefia.core.exceptions.PasswordNotMatch;
import com.chefia.core.exceptions.UserNotFoundException;
import com.chefia.core.exceptions.UserNotStrongPassword;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.usecases.interfaces.user.UpdateUserPasswordUsecase;
import com.chefia.users.model.ChangePasswordDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.chefia.infra.validation.annotation.StrongPasswordValidator.isValid;

@AllArgsConstructor
@Component
public class UpdateUserPasswordUsecaseImpl implements UpdateUserPasswordUsecase {

    private final UserGateway userGateway;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void execute(Long userId, ChangePasswordDTO changePasswordDTO) {
        var userEntity = this.userGateway
                .findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        boolean passwordMatches = this.passwordEncoder.matches(changePasswordDTO.getOldPassword(), userEntity.getPassword());
        boolean newPasswordIsEqualToOld = changePasswordDTO.getOldPassword().equals(changePasswordDTO.getNewPassword());

        if (!passwordMatches) {
            throw new PasswordNotMatch("Wrong password");
        } else if (newPasswordIsEqualToOld) {
            throw new PasswordAlreadyUsed("The passwords are equals");
        } else if (!isValid(changePasswordDTO.getNewPassword())) {
            throw new UserNotStrongPassword("New password not strong");
        }

        this.userGateway.updateUserPassword(userId, userEntity);
    }
}
