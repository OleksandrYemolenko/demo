package com.example.demo.repositories;

import com.example.demo.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByUserGuid(UUID guid);
}
