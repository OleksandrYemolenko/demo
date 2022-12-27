package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    public static final String PHONE_NUMBER_PART = "380-50-444-55-";
    public static final String GMAIL_COM = "@gmail.com";

    private UserService userService;

    private UserRepository userRepository;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);

        userService = new UserService(userRepository);
    }

    @Test
    public void createUserSuccess() throws Exception {
        final User testUser = createUserForTest(null, null);

        when(userRepository.saveAndFlush(any(User.class))).thenReturn(testUser);

        final User createdUser = userService.create(testUser);

        final ArgumentCaptor<User> acUser = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        verify(userRepository).saveAndFlush(acUser.capture());

        final User actualUser = acUser.getValue();

        Assertions.assertNotNull(createdUser);
    }

    @Test
    public void updateUserSuccess() throws Exception {
        final Long id = 1L;
        final UUID guid = UUID.randomUUID();
        final User testUser = createUserForTest(id, guid);
        final User foundUser = createUserForTest(id, guid);

        when(userRepository.findByGuid(guid)).thenReturn(Optional.ofNullable(foundUser));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(testUser);

        final User updatedUser = userService.update(testUser);

        final ArgumentCaptor<User> acUser = ArgumentCaptor.forClass(User.class);

        verify(userRepository, times(1)).saveAndFlush(any(User.class));
        verify(userRepository).saveAndFlush(acUser.capture());

        final User actualUser = acUser.getValue();

        Assertions.assertNotNull(updatedUser);
        Assertions.assertNotNull(actualUser.getIdUser());
    }

    @Test
    public void deleteUserSuccess() throws Exception {
        final Long id = 1L;
        final UUID guid = UUID.randomUUID();
        final User foundUser = createUserForTest(id, guid);

        when(userRepository.findByGuid(guid)).thenReturn(Optional.ofNullable(foundUser));

        userService.deleteByGuid(guid);

        final ArgumentCaptor<Long> acUserID = ArgumentCaptor.forClass(Long.class);

        verify(userRepository, times(1)).deleteById(any(Long.class));
        verify(userRepository).deleteById(acUserID.capture());

        final Long deletedUserID = acUserID.getValue();

        Assertions.assertEquals(id, deletedUserID);
    }

    private User createUserForTest(Long id, UUID guid) {
        final User user = createOrdinaryUser(1);
        user.setIdUser(id);
        user.setGuid(guid);
        return user;
    }

    private User createOrdinaryUser(int i) {
        return createSimpleUser(
                UUID.randomUUID(),
                "name" + i,
                "email" + i + GMAIL_COM,
                PHONE_NUMBER_PART + 55 + i);
    }

    private User createSimpleUser(UUID guid, String name, String email, String phoneNumber) {
        User user = new User();
        user.setName(name);
        user.setGuid(guid);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);

        return user;
    }
}
