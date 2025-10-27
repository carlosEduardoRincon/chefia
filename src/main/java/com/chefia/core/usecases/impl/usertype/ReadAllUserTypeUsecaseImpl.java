package com.chefia.core.usecases.impl.usertype;

import com.chefia.core.entities.UserType;
import com.chefia.core.gateway.UserTypeGateway;
import com.chefia.core.mapper.UserTypeMapper;
import com.chefia.core.usecases.interfaces.usertype.ReadAllUserTypeUsecase;
import com.chefia.usertypes.model.PaginatedUserTypeDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class ReadAllUserTypeUsecaseImpl implements ReadAllUserTypeUsecase {

    private final UserTypeGateway userTypeGateway;
    private final UserTypeMapper userTypeMapper;
    
    @Override
    public PaginatedUserTypeDTO execute(Integer page, Integer perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        List<UserType> userTypePage = this.userTypeGateway.findAll(pageable);

        var userTypesDto = this.userTypeMapper.toResponseListDTO(userTypePage);

        return new PaginatedUserTypeDTO()
                .page(page)
                .perPage(perPage)
                .total((long) userTypePage.size())
                .items(userTypesDto);
    }
}
