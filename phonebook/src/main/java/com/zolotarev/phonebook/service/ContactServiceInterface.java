package com.zolotarev.phonebook.service;

import com.zolotarev.phonebook.dto.ContactRequest;
import com.zolotarev.phonebook.dto.ContactResponse;
import com.zolotarev.phonebook.entities.Contact;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Интерфейс для управления контактами.
 * Содержит методы для добавления, удаления и получения информации о пользователях.
 */

public interface ContactServiceInterface {
    /**
     * Получает контакт по его идентификатору.
     *
     * @param id контакт по id
     * @return Контакт, если найден
     */
    public Contact findContactById(Long id);
    /**
     * Получает все контакты.
     *
     * @param pageable параметры постраничной навигации
     * @return Контакты, если найдены
     */
    public List<ContactResponse> findAllContacts(Pageable pageable);
    /**
     * Получает все контакты авторизованного пользователя.
     *
     * @param pageable параметры постраничной навигации
     * @param authentication объект, содержащий информацию о пользователе, прошедшем аутентификацию
     * @return Контакты, если найдены
     */
    public List<ContactResponse> findAllUserContacts(Pageable pageable, Authentication authentication);
    /**
     * Создает контакт.
     *
     * @param contact dto запроса на создание(обновление) контакта
     * @param authentication объект, содержащий информацию о пользователе, прошедшем аутентификацию
     * @return Контакт
     */
    public Contact createContact(ContactRequest contact, Authentication authentication);
    /**
     * Обновляет заданный контакт.
     *
     * @param id идентификатор контакта
     * @param contact dto запроса на создание(обновление) контакта
     * @param auth объект, содержащий информацию о пользователе, прошедшем аутентификацию
     * @return dto обновленного контакта
     */
    public Long updateContact(Long id, ContactRequest contact, Authentication auth) throws NoPermissionException;
    /**
     * Удаляет заданный контакт.
     *
     * @param id идентификатор контакта
     * @param auth объект, содержащий информацию о пользователе, прошедшем аутентификацию
     * @return dto удаленного контакта
     */
    public ContactResponse deleteContact(Long id, Authentication auth) throws NoPermissionException;
    /**
     * Получает контакт по заданной электронной почте.
     *
     * @param email электронная почта
     * @param authentication объект, содержащий информацию о пользователе, прошедшем аутентификацию
     * @return Контакт, если найден
     */
    public ContactResponse findContactByEmail(String email, Authentication authentication);
    /**
     * Получает контакт по заданному номеру телефона.
     *
     * @param phone номер телефона
     * @param authentication объект, содержащий информацию о пользователе, прошедшем аутентификацию
     * @return Контакт, если найден
     */
    public ContactResponse findContactByPhone(String phone, Authentication authentication);
    /**
     * Получает контакт по заданному telegram.
     *
     * @param telegram telegram
     * @param authentication объект, содержащий информацию о пользователе, прошедшем аутентификацию
     * @return Контакт, если найден
     */
    public ContactResponse findContactByTelegram(String telegram, Authentication authentication);

}
