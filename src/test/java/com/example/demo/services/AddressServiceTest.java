package com.example.demo.services;

import com.example.demo.entities.Address;
import com.example.demo.entities.User;
import com.example.demo.repositories.AddressRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddressServiceTest {

    private AddressService addressService;

    private AddressRepository addressRepository;

    private List<Address> addressList = new ArrayList<>();

    @Before
    public void setUp() {
        addressRepository = mock(AddressRepository.class);

        addressService = new AddressService(addressRepository);

        User user = new User();
        Address address = new Address();

        address.setAddress("some address");
        address.setIdAddress(1L);
        address.setNotes("some notes");
        address.setUser(user);

        addressList.add(address);
    }

    @Test
    public void getAllForUserSuccess() throws Exception {
        when(addressRepository.findAllByUserGuid(any())).thenReturn(addressList);

        List<Address> realAddressList = addressService.getAllForUser(UUID.randomUUID());

        assertEquals(addressList, realAddressList);
    }
}
