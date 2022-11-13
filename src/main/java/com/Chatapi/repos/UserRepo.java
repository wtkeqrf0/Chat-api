package com.Chatapi.repos;



import com.Chatapi.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@EnableJpaRepositories
public interface UserRepo extends JpaRepository<User,Long> {
    Optional<User> findByLogin(String login);

    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET name = :name , middle_name = :middleName ," +
            "surname = :surname , avatar = :avatar WHERE user_id = :userId ;", nativeQuery = true)
    void updateUser(String name, String middleName, String surname, String avatar, Long userId);
}