package com.devodata.hrm.services;

import com.devodata.hrm.data.models.Role;
import com.devodata.hrm.data.models.UserEntity;
import com.devodata.hrm.data.repository.UserRepository;
import com.devodata.hrm.dtos.UpdateUserRequest;
import com.devodata.hrm.dtos.UserResponse;
import com.devodata.hrm.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;


    public UserResponse getUser(String username) throws UserNotFoundException {
        UserEntity userEntity = getUserByUsername(username);

        String rolesAsString = userEntity.getRoles().stream()
                .map(Role::toString)
                .collect(Collectors.joining(", "));

        return new UserResponse(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.getAge(),
                rolesAsString
        );
    }

    @Override
    public String updateUser(UpdateUserRequest updateUser) {
        UserEntity user = getUserByUsername(updateUser.getUsername());

        updateIfNotNull(user::setFirstName, updateUser.getFirstName());
        updateIfNotNull(user::setLastName, updateUser.getLastName());
        updateIfNotNull(user::setAge, updateUser.getAge());
        return "Update Successful";
    }

    @Override
    public String deleteUser(String username) throws UserNotFoundException {
        UserEntity user = getUserByUsername(username);
        String userFullName = getFullName(user);
        userRepository.delete(user);
        return String.format("%s deleted successfully", userFullName);
    }
    private UserEntity getUserByUsername(String username) throws UserNotFoundException {
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        return optionalUser.orElseThrow(() ->  new UserNotFoundException(String.format("User not found with username: %s", username)));
    }

    private static String getFullName(UserEntity user) {
        return String.format("%s %s", user.getFirstName(), user.getLastName());
    }

    private <T> void updateIfNotNull(Consumer<T> setter, T value){
        if(value != null){
            setter.accept(value);
        }
    }
}
