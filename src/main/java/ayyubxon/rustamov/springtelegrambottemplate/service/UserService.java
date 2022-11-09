package ayyubxon.rustamov.springtelegrambottemplate.service;

import ayyubxon.rustamov.springtelegrambottemplate.dto.ServiceResponse;
import ayyubxon.rustamov.springtelegrambottemplate.entity.User;
import ayyubxon.rustamov.springtelegrambottemplate.entity.enums.State;
import ayyubxon.rustamov.springtelegrambottemplate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    final UserRepository userRepository;

    public ServiceResponse<User> save(User user) {
        User save = userRepository.save(user);
        return new ServiceResponse<>("Here", true, save);
    }


    public ServiceResponse<User> getOne(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.map(user -> new ServiceResponse<>("Here", true, user)).orElseGet(() ->
                new ServiceResponse<>("User not found!", false));
    }

    public ServiceResponse<List<User>> getAll() {
        List<User> all = userRepository.findAll();
        return new ServiceResponse<>("Here", true, all);
    }

    public ServiceResponse<User> editState(Long id, State state) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User edited = optionalUser.get();
            edited.setState(state);

            User save = userRepository.save(edited);

            return new ServiceResponse<>("Updated!", true, save);
        }
        return new ServiceResponse<>("User not found!", false);
    }
}
