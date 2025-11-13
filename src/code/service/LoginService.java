package code.service;

import code.model.User;
import code.repository.UserRepository;

import java.util.Optional;

public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User authentication(String userId, String password){
        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            if (user.getPassword().equals(password)){
                return user;
            }
        }
        return null; //failed to login user
    }
}
