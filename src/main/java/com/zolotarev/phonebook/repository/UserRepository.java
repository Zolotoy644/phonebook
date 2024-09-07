package com.zolotarev.phonebook.repository;

import com.zolotarev.phonebook.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для управления базой данных пользователей.
 * Содержит методы для получения информации о пользователях.
 */

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Получает сущность пользователя по его идентификатору.
     *
     * @param username имя пользователя
     * @return объект пользователя, если найден
     */
    Optional<User> findByUsername(String username);

    /**
     * Получает сущность пользователя по его идентификатору.
     *
     * @param aLong идентификатор пользователя
     * @return объект пользователя, если найден
     */
    @Override
    Optional<User> findById(Long aLong);
}
