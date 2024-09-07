package com.zolotarev.phonebook.repository;

import com.zolotarev.phonebook.entities.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Locale;
import java.util.Optional;

/**
 * Интерфейс для управления базой данных контактов.
 * Содержит методы для получения контактов из базы данных.
 */

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    /**
     * Получает контакты пользователя по его имени и фамилии.
     *
     * @param firstname имя пользователя
     * @param lastname фамилия пользователя
     * @return список контактов пользователя, если найдены
     */
    Contact findByFirstnameAndLastname(String firstname, String lastname);
    /**
     * Получает все контакты.
     *
     * @param pageable параметры постраничной навигации
     * @return список контактов пользователя, если найдены
     */
    Page<Contact> findAll(Pageable pageable);
    /**
     * Получает контакты пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @param pageable параметры постраничной навигации
     * @return список контактов пользователя, если найдены
     */
    Page<Contact> findAllByUserId(Long id, Pageable pageable);

    /**
     * Получает сущность пользователя по его электронной почте.
     *
     * @param email электронная почта пользователя
     * @param userId идентификатор пользователя
     * @return список контактов пользователя, если найдены
     */
    @Query("SELECT c FROM Contact c WHERE c.userId = ?2 AND c.email = ?1")
    Optional<Contact> findByEmail(String email, Long userId);
    /**
     * Получает сущность пользователя по его телефону.
     *
     * @param phone телефон пользователя
     * @param userId идентификатор пользователя
     * @return список контактов пользователя, если найдены
     */
    @Query("SELECT c FROM Contact c WHERE c.userId = ?2 AND c.phone = ?1")
    Optional<Contact> findByPhone(String phone, Long userId);
    /**
     * Получает сущность пользователя по его Telegram.
     *
     * @param telegram Telegram пользователя
     * @param userId идентификатор пользователя
     * @return список контактов пользователя, если найдены
     */
    @Query("SELECT c FROM Contact c WHERE c.userId = ?2 AND c.telegram = ?1")
    Optional<Contact> findByTelegram(String telegram, Long userId);
}

