package com.example.demo.controllers;

import com.example.demo.entities.Address;
import com.example.demo.entities.User;
import com.example.demo.services.AddressService;
import com.example.demo.services.DatabaseService;
import com.example.demo.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    private final UserService userService;
    private final AddressService addressService;

    public UserController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User newUser) throws Exception {
        logger.info("Accepted requested to create a new user:\n{}", newUser);

        User createdUser = userService.create(newUser);

        logger.info("Sending the created users to the client:\n{}", createdUser);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @GetMapping
    public ResponseEntity<User> getUser(@PathVariable String guid) throws Exception {
        logger.info("Client requested the user {}", guid);

        User user = userService.getByGuid(UUID.fromString(guid));

        logger.info("Sending the user to the client:\n{}", user);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    @PutMapping
    public ResponseEntity<User> modifyUser(@RequestBody User user) throws Exception {
        logger.info("Accepted modified user from the client:\n{}", user);

        User modifiedUser = userService.update(user);

        logger.info("Sending the modified user to the client:\n{}", modifiedUser);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(modifiedUser);
    }

    @DeleteMapping
    public ResponseEntity deleteUser(@PathVariable String guid) throws Exception {
        logger.info("Accepted request to delete the user {}", guid);

        userService.deleteByGuid(UUID.fromString(guid));

        logger.info("User ({}) successfully deleted", guid);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping
    public ResponseEntity<List<Address>> getUserAddresses(@PathVariable String guid) throws Exception {
        logger.info("Client requested all the addresses of the employee {}", guid);

        List<Address> addresses = addressService.getAllForUser(UUID.fromString(guid));

        logger.info("Sending the list of addresses of the user {} to the client:\n", addresses);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(addresses);
    }
}
