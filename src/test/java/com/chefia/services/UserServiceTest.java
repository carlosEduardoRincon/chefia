package com.chefia.services;

import com.chefia.entities.*;
import com.chefia.exceptions.*;
import com.chefia.mapper.AddressMapper;
import com.chefia.mapper.UserMapper;
import com.chefia.repositories.UserRepository;

import com.chefia.users.model.ChangePasswordDTO;
import com.chefia.users.model.CreateUserDTO;
import com.chefia.users.model.UpdateUserDTO;
import com.chefia.users.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.*;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        var createUserDTO = new CreateUserDTO();
        var user = new User();
        var expectedDTO = new UserDTO();

        when(this.userMapper.toEntity(createUserDTO)).thenReturn(user);
        when(this.userMapper.toResponseDTO(user)).thenReturn(expectedDTO);

        var result = this.userService.saveUser(createUserDTO);

        assertEquals(expectedDTO, result);
        verify(this.userRepository).save(user);
    }

    @Test
    void shouldFindUserByIdSuccessfully() {
        var id = 1L;
        var user = new User();
        var expectedDTO = new UserDTO();

        when(this.userRepository.findById(id)).thenReturn(Optional.of(user));
        when(this.userMapper.toResponseDTO(user)).thenReturn(expectedDTO);

        var result = this.userService.findById(id);

        assertEquals(expectedDTO, result);
    }

    @Test
    void shouldThrowWhenUserNotFoundById() {
        when(this.userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> this.userService.findById(1L));
    }

    @Test
    void shouldFindAllUsersPaginated() {
        var page = 0;
        var perPage = 2;
        var users = List.of(new User(), new User());
        var pageResult = new PageImpl<>(users, PageRequest.of(page, perPage), 2);
        var userDTOList = List.of(new UserDTO(), new UserDTO());

        when(this.userRepository.findAll(PageRequest.of(page, perPage))).thenReturn(pageResult);
        when(this.userMapper.toResponseListDTO(users)).thenReturn(userDTOList);

        var result = this.userService.findAll(page, perPage);

        assertEquals(userDTOList, result.getItems());
        assertEquals(2, result.getTotal());
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        var id = 1L;
        var updateDTO = new UpdateUserDTO();
        var existingUser = new User();
        var updatedUser = new User();
        var expectedDTO = new UserDTO();

        when(this.userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(this.userMapper.toUpdateEntity(existingUser, updateDTO)).thenReturn(updatedUser);
        when(this.userMapper.toResponseDTO(updatedUser)).thenReturn(expectedDTO);

        var result = this.userService.updateUser(id, updateDTO);

        assertEquals(expectedDTO, result);
        verify(this.userRepository).flush();
    }

    @Test
    void shouldDeleteUser() {
        var id = 1L;
        this.userService.deleteUser(id);
        verify(this.userRepository).deleteById(id);
    }

    @Test
    void shouldChangeUserStatus() {
        var id = 1L;
        var user = spy(new User());

        when(this.userRepository.findById(id)).thenReturn(Optional.of(user));
        this.userService.changeUserStatus(id, false);

        verify(user).setActive(false);
        verify(user).updatedAt();
        verify(this.userRepository).flush();
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        var id = 1L;
        var user = new User();
        user.setPassword("encodedOld");
        var dto = new ChangePasswordDTO().newPassword("@SenhaForte.123").oldPassword("old");
        var encodedNew = "encodedNew";

        when(this.userRepository.findById(id)).thenReturn(Optional.of(user));
        when(this.passwordEncoder.matches("old", "encodedOld")).thenReturn(true);
        when(this.passwordEncoder.encode("new")).thenReturn(encodedNew);
        when(this.userMapper.toUpdatePasswordEntity(user, encodedNew)).thenReturn(user);

        this.userService.changePassword(id, dto);

        verify(this.userRepository).flush();
    }

    @Test
    void shouldThrowIfOldPasswordDoesNotMatch() {
        var id = 1L;
        var user = new User();
        user.setPassword("encodedOld");
        var dto = new ChangePasswordDTO().oldPassword("wrong").newPassword("new");

        when(this.userRepository.findById(id)).thenReturn(Optional.of(user));
        when(this.passwordEncoder.matches("wrong", "encodedOld")).thenReturn(false);

        assertThrows(PasswordNotMatch.class, () -> this.userService.changePassword(id, dto));
    }

    @Test
    void shouldThrowIfNewPasswordEqualsOldPassword() {
        var id = 1L;
        var user = new User();
        user.setPassword("encodedOld");
        var dto = new ChangePasswordDTO().oldPassword("same").newPassword("same");

        when(this.userRepository.findById(id)).thenReturn(Optional.of(user));
        when(this.passwordEncoder.matches("same", "encodedOld")).thenReturn(true);

        assertThrows(PasswordAlreadyUsed.class, () -> this.userService.changePassword(id, dto));
    }
}
