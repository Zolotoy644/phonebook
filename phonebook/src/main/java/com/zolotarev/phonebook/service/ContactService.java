package com.zolotarev.phonebook.service;

import com.zolotarev.phonebook.dto.ContactRequest;
import com.zolotarev.phonebook.dto.ContactResponse;
import com.zolotarev.phonebook.entities.Contact;
import com.zolotarev.phonebook.entities.User;
import com.zolotarev.phonebook.repository.ContactRepository;
import com.zolotarev.phonebook.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "contacts")
public class ContactService implements ContactServiceInterface {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Cacheable(cacheNames = "contact", key = "#id", unless = "#result == null")
    public Contact findContactById(Long id) {
        return contactRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Contact with id " + id + " not found")
        );
    }

    private ContactResponse toResponse(Contact contact) {
        ContactResponse response = new ContactResponse();
        response.setId(contact.getId());
        response.setFirstname(contact.getFirstname());
        response.setLastname(contact.getLastname());
        response.setEmail(contact.getEmail());
        response.setPhone(contact.getPhone());
        response.setTelegram(contact.getTelegram());
        return response;
    }
    @Cacheable(cacheNames = "contacts")
    public List<ContactResponse> findAllContacts(Pageable pageable) {
        List<Contact> contacts = contactRepository.findAll(pageable).getContent();
        return contacts.stream().map(this::toResponse).toList();
    }

    @Cacheable(cacheNames = "contacts")
    public List<ContactResponse> findAllUserContacts(Pageable pageable, Authentication authentication) {
        Long userId = this.extractUserId(authentication);
        List<Contact> contacts = contactRepository.findAllByUserId(userId, pageable).getContent();
        return contacts.stream().map(this::toResponse).toList();
    }

    private Long extractUserId(Authentication authentication) {
        User user = null;
        if (authentication instanceof UsernamePasswordAuthenticationToken token) {
            user = (User) token.getPrincipal();
        }
        assert user != null;
        return user.getId();
    }

    public Contact createContact(ContactRequest contact, Authentication authentication) {
        Long userId = this.extractUserId(authentication);
        User user = this.userRepository.getReferenceById(userId);
        /*User user = this.userRepository.findById(userId).orElseThrow(() ->
                new NoSuchElementException("User not found"));*/
        Contact newContact = this.toContact(contact);
        newContact.setUser(user);
        newContact.setUserId(user.getId());
        return contactRepository.save(newContact);
    }

    private Contact toContact(ContactRequest contact) {
        Contact newContact = new Contact();
        newContact.setFirstname(contact.getFirstname());
        newContact.setLastname(contact.getLastname());
        newContact.setEmail(contact.getEmail());
        newContact.setPhone(contact.getPhone());
        newContact.setTelegram(contact.getTelegram());
        return newContact;
    }

    @CacheEvict(cacheNames = "contacts", allEntries = true)
    public Long updateContact(Long id, ContactRequest contact, Authentication auth) throws NoPermissionException {
        Long userId = this.extractUserId(auth);
        User user = this.userRepository.getReferenceById(userId);
        Contact updatedContact = contactRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Contact with id " + id + " not found")
        );
        Long creatorId = updatedContact.getUser().getId();
        boolean isCreator = user.getId().equals(creatorId);
        if (!isCreator) {
            throw new NoPermissionException("You do not have permission to update this contact");
        }
        updatedContact.setFirstname(contact.getFirstname());
        updatedContact.setLastname(contact.getLastname());
        updatedContact.setEmail(contact.getEmail());
        updatedContact.setPhone(contact.getPhone());
        updatedContact.setTelegram(contact.getTelegram());
        return id;
    }

    @Caching(evict = { @CacheEvict(cacheNames = "contact", key = "#id"),
            @CacheEvict(cacheNames = "contacts", allEntries = true) })
    public ContactResponse deleteContact(Long id, Authentication auth) throws NoPermissionException {
        Long userId = this.extractUserId(auth);
        Contact deletedContact = contactRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Contact with id " + id + " not found")
        );
        boolean isCreator = userId.equals(deletedContact.getUser().getId());
        if (!isCreator) {
            throw new NoPermissionException("You do not have permission to delete this contact");
        }
        contactRepository.delete(deletedContact);
        return toResponse(deletedContact);
    }

    @Cacheable(cacheNames = "contact", key = "#email", unless = "#result == null")
    public ContactResponse findContactByEmail(String email, Authentication authentication) {
        Long userId = this.extractUserId(authentication);
        Contact contact = contactRepository.findByEmail(email, userId).orElseThrow(
                () -> new NoSuchElementException("Contact with email " + email + " not found")
        );
        return toResponse(contact);
    }

    @Cacheable(cacheNames = "contact", key = "#phone", unless = "#result == null")
    public ContactResponse findContactByPhone(String phone, Authentication authentication) {
        Long userId = this.extractUserId(authentication);
        Contact contact = contactRepository.findByPhone(phone, userId).orElseThrow(
                () -> new NoSuchElementException("Contact with phone " + phone + " not found")
        );
        return toResponse(contact);
    }

    @Cacheable(cacheNames = "contact", key = "#telegram", unless = "#result == null")
    public ContactResponse findContactByTelegram(String telegram, Authentication authentication) {
        Long userId = this.extractUserId(authentication);
        Contact contact = contactRepository.findByTelegram(telegram, userId).orElseThrow(
                () -> new NoSuchElementException("Contact with telegram " + telegram + " not found")
        );
        return toResponse(contact);
    }
}
