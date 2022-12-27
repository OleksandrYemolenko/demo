package com.example.demo.services;

import com.example.demo.entities.Address;
import com.example.demo.repositories.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {

    Logger logger = LoggerFactory.getLogger(AddressService.class);

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Transactional(readOnly = true)
    public List<Address> getAllForUser(UUID userGuid) throws Exception {
        logger.info("Get user addresses: userGuid = {}", userGuid);

        try {
            final List<Address> addresses = addressRepository.findAllByUserGuid(userGuid);

            logger.debug("User addresses was gotten from DB: userGuid = {}", userGuid);

            return addresses;
        } catch (DataAccessException ex) {
            logger.error("Error while getting user addresses from DB: userGuid = " + userGuid, ex);
            throw new Exception("An error occurred while getting user addresses", ex);
        }
    }
}
