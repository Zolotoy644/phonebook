package com.zolotarev.phonebook.service;

import com.zolotarev.phonebook.dto.AuthRequest;
import com.zolotarev.phonebook.dto.AuthResponse;
import com.zolotarev.phonebook.dto.UserRequest;
import com.zolotarev.phonebook.dto.UserResponse;
import com.zolotarev.phonebook.entities.Contact;
import com.zolotarev.phonebook.entities.User;
import com.zolotarev.phonebook.repository.ContactRepository;
import com.zolotarev.phonebook.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
//@AllArgsConstructor
//@NoArgsConstructor
public class UserService /*implements UserDetailsService*/ {

    private final UserRepository userRepository;
    private final ContactRepository contactRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;


    /*@Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(username);
        return user.orElseThrow(() -> new UsernameNotFoundException(username));
    }*/

    public Long createUser(UserRequest userRequest) {
        User user = this.dtoToUser(userRequest);
        Optional<User> userFromDb = this.userRepository.findByUsername(userRequest.getUsername());
        if (userFromDb.isPresent()) {
            throw new NoSuchElementException("User already registered: " + userRequest.getUsername());
        }
        User saved = userRepository.saveAndFlush(user);
        return saved.getId();
    }

    private User dtoToUser(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .password(encoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .build();
    }

    public User deleteUser(Long id) {
        User user = this.userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User not found: " + id));
        // if orphan removal didn't enable
        /*List<Long> userContacts = user.getContacts()
                        .stream()
                        .map(Contact::getId)
                        .toList();
        for (Long contactId : userContacts) {
            contactRepository.deleteById(contactId);
        }*/
        userRepository.delete(user);
        return user;

    }

    public AuthResponse login(AuthRequest authRequest) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getUsername(),
                        authRequest.getPassword()));

        User user = (User) auth.getPrincipal();
        String token = jwtService.generateToken(user.getUsername());
        return new AuthResponse(token, user.getId());
    }

    public UserResponse toResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setId(user.getId());
        userResponse.setRole(user.getRole());
        if (Hibernate.isInitialized(user.getContacts())) {
            List<Contact> contacts = user.getContacts();
            List<String> contactList = new ArrayList<>();
            for (Contact contact : contacts) {
                contactList.add("id: " + contact.getId() +
                        "firstname: " + contact.getFirstname() +
                        "lastname: " + contact.getLastname() +
                        "phone: " + contact.getPhone() +
                        "email: " + contact.getEmail() +
                        "telegram: " + contact.getTelegram());
            }
            userResponse.setContacts(contactList);
        }
        return userResponse;
    }

    public UserResponse updateUser(UserRequest userRequest, Authentication auth) {
        User currentUser = (User) auth.getPrincipal();
        User fromDb = userRepository.findById(currentUser.getId()).orElseThrow(
                () -> new NoSuchElementException("User not found: " + currentUser.getId())
        );
        fromDb.setUsername(userRequest.getUsername());
        fromDb.setPassword(encoder.encode(userRequest.getPassword()));
        fromDb.setRole(userRequest.getRole());
        return this.toResponse(userRepository.save(fromDb));
    }



}
