//package com.chefia.services;
//
//import com.chefia.addresses.model.AddressDTO;
//import com.chefia.addresses.model.CreateAddressDTO;
//import com.chefia.addresses.model.UpdateAddressDTO;
//import com.chefia.domain.model.Address;
//import com.chefia.domain.model.User;
//import com.chefia.infra.exception.AddressNotFoundException;
//import com.chefia.infra.exception.UserNotFoundException;
//import com.chefia.core.service.AddressService;
//import com.chefia.infra.mapper.AddressMapper;
//import com.chefia.core.port.output.address.AddressRepository;
//import com.chefia.core.port.output.user.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class AddressServiceTest {
//
//    @InjectMocks
//    private AddressService addressService;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private AddressRepository addressRepository;
//
//    @Mock
//    private AddressMapper addressMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void shouldCreateAddressForUser() {
//        var userId = 1L;
//        var dto = new CreateAddressDTO();
//        var user = new User();
//        user.setAddress(new ArrayList<>());
//
//        var addressEntity = new Address();
//        var expectedDto = new AddressDTO();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
//        when(addressMapper.toCreateAddressEntity(dto)).thenReturn(addressEntity);
//        when(addressMapper.toAddressResponseDTO(addressEntity)).thenReturn(expectedDto);
//
//        var result = addressService.createAddressForUser(userId, dto);
//
//        assertNotNull(result);
//        assertEquals(expectedDto, result);
//        verify(userRepository).flush();
//    }
//
//    @Test
//    void shouldThrowWhenUserNotFound() {
//        var userId = 99L;
//        var dto = new CreateAddressDTO();
//
//        when(userRepository.findById(userId)).thenReturn(Optional.empty());
//
//        assertThrows(UserNotFoundException.class, () -> addressService.createAddressForUser(userId, dto));
//    }
//
//    @Test
//    void shouldFindAddressById() {
//        var addressId = 1L;
//        var addressEntity = new Address();
//        var expectedDto = new AddressDTO();
//
//        when(addressRepository.findById(addressId)).thenReturn(Optional.of(addressEntity));
//        when(addressMapper.toAddressResponseDTO(addressEntity)).thenReturn(expectedDto);
//
//        var result = addressService.findById(addressId);
//
//        assertNotNull(result);
//        assertEquals(expectedDto, result);
//    }
//
//    @Test
//    void shouldThrowWhenAddressNotFoundInFindById() {
//        when(addressRepository.findById(123L)).thenReturn(Optional.empty());
//        assertThrows(AddressNotFoundException.class, () -> addressService.findById(123L));
//    }
//
//    @Test
//    void shouldUpdateAddress() {
//        var addressId = 1L;
//        var existingEntity = new Address();
//        var updateDTO = new UpdateAddressDTO();
//        var expectedDTO = new AddressDTO();
//
//        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existingEntity));
//        when(addressMapper.toAddressResponseDTO(existingEntity)).thenReturn(expectedDTO);
//
//        var result = addressService.updateAddress(addressId, updateDTO);
//
//        assertEquals(expectedDTO, result);
//        verify(addressRepository).flush();
//    }
//
//    @Test
//    void shouldThrowWhenAddressNotFoundInUpdate() {
//        when(addressRepository.findById(42L)).thenReturn(Optional.empty());
//        assertThrows(AddressNotFoundException.class, () -> addressService.updateAddress(42L, new UpdateAddressDTO()));
//    }
//
//    @Test
//    void shouldDeleteAddress() {
//        var addressId = 5L;
//        addressService.deleteAddress(addressId);
//        verify(addressRepository).deleteById(addressId);
//    }
//}
