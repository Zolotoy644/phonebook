package com.zolotarev.phonebook.controller;

import com.zolotarev.phonebook.dto.ContactRequest;
import com.zolotarev.phonebook.dto.ContactResponse;
import com.zolotarev.phonebook.entities.Contact;
import com.zolotarev.phonebook.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contacts")
@SecurityRequirement(name = "JWT Bearer")
@Tag(name="Контроллер контактов", description="Предназначен для управления пользовательскими контактами")
public class ContactController {
    private final ContactService contactService;

    @GetMapping("/phone/{phone}")
    @Operation(
            summary = "Поиск контакта по номеру телефона",
            description = "Позволяет авторизованному пользователю получить контакт по номеру телефона"
    )
    public ResponseEntity<?> findContactByPhone(@PathVariable String phone,
                                                Authentication auth) {
        ContactResponse contact = contactService.findContactByPhone(phone, auth);
        return ResponseEntity.status(HttpStatus.FOUND).body(contact);
    }

    @GetMapping("/telegram/{telegram}")
    @Operation(
            summary = "Поиск контакта по Telegram",
            description = "Позволяет авторизованному пользователю получить контакт по Telegram"
    )
    public ResponseEntity<?> findContactByTelegram(@PathVariable String telegram,
                                                   Authentication auth) {
        ContactResponse contact = contactService.findContactByTelegram(telegram, auth);
        return ResponseEntity.status(HttpStatus.OK).body(contact);
    }

    @GetMapping("/email/{email}")
    @Operation(
            summary = "Поиск контакта по электронной почте",
            description = "Позволяет авторизованному пользователю получить контакт по электронной почте"
    )
    public ResponseEntity<?> findContactByEmail(@PathVariable String email,
                                                Authentication auth) {
        ContactResponse contact = contactService.findContactByEmail(email, auth);
        return ResponseEntity.status(HttpStatus.OK).body(contact);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Удаление контакта",
            description = "Позволяет авторизованному пользователю удалить контакт по id"
    )
    public ResponseEntity<?> deleteContact(@PathVariable Long id,
                                           Authentication auth) throws NoPermissionException {
        ContactResponse contact = contactService.deleteContact(id, auth);
        return ResponseEntity.status(HttpStatus.OK).body(contact);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "Обновление контакта",
            description = "Позволяет авторизованному пользователю обновить контакт по id"
    )
    public ResponseEntity<?> updateContact(@PathVariable Long id,
                                           @RequestBody ContactRequest contactRequest,
                                           Authentication auth) throws NoPermissionException {
        Long updatedId = contactService.updateContact(id, contactRequest, auth);
        return ResponseEntity.status(HttpStatus.OK).body("Contact with id: " + updatedId + " updated");
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание контакта",
            description = "Позволяет авторизованному пользователю создать контакт"
    )
    public ResponseEntity<?> createContact(@Valid @RequestBody ContactRequest contactRequest,
                                           Authentication auth) throws NoPermissionException {
        Contact contact = contactService.createContact(contactRequest, auth);
        return ResponseEntity.status(HttpStatus.CREATED).body("Contact created with id: " + contact.getId());
    }

    @GetMapping("/all")
    @Operation(
            summary = "Поиск контактов",
            description = "Позволяет авторизованному пользователю получить все контакты. Вывод производится постранично"
    )
    public ResponseEntity<?> findAllUserContacts(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "10") int limit,
                                                 Authentication auth) {
        List<ContactResponse> contacts = contactService.findAllUserContacts(PageRequest.of(page, limit), auth);
        return ResponseEntity.status(HttpStatus.OK).body(contacts);
    }
}
