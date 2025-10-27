package com.chefia.core.usecases.impl.user;

import com.chefia.core.entities.User;
import com.chefia.core.gateway.UserGateway;
import com.chefia.core.mapper.UserMapper;
import com.chefia.core.usecases.interfaces.user.ReadAllUserUsecase;
import com.chefia.users.model.PaginatedUsersDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ReadAllUserUsecaseImpl implements ReadAllUserUsecase {

    private final UserGateway userGateway;
    private final UserMapper userMapper;

    @Override
    public PaginatedUsersDTO execute(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<User> userPage = this.userGateway.findAll(pageable);

        var userDTOs = this.userMapper.toResponseListDTO(userPage);

        return new PaginatedUsersDTO()
                .page(page)
                .perPage(perPage)
                .total((long) userPage.size())
                .items(userDTOs);
    }
}
