package com.Chatapi.servises;

import com.Chatapi.Entities.User;
import com.Chatapi.repos.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo repo;

    public User findUser(String login) throws UsernameNotFoundException {
        return repo.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(""));
    }

    public User findUser(Long userId) throws UsernameNotFoundException {
        return repo.findById(userId).orElseThrow(() -> new UsernameNotFoundException(""));
    }

    public void updateUser(String name, String middleName, String surname, String avatar, String login) {
        repo.updateUser(name, middleName, surname, avatar, repo.findByLogin(login).get().getUserId());
    }

    public void saveUser(User user) {
        repo.save(user);
    }

    public UserService(UserRepo repo) {
        this.repo = repo;
    }
}