package notebook.controller;

import notebook.model.User;
import notebook.model.repository.GBRepository;
import notebook.view.UserView;

import java.util.List;
import java.util.Objects;

public class UserController {
    private final GBRepository repository;

    public UserController(GBRepository repository) {
        this.repository = repository;
    }

    public void saveUser(User user) {
        repository.create(user);
    }
    public static User createUser() {
        String firstName = UserView.prompt("Имя: ");
        if(firstName.isEmpty()){
            throw new RuntimeException("Имя не может быть пустым");
        }
        String lastName = UserView.prompt("Фамилия: ");
        if(lastName.isEmpty()){
            throw new RuntimeException("Фамилия не может быть пустым");
        }
        String phone = UserView.prompt("Номер телефона: ");
        if(phone.isEmpty()){
            throw new RuntimeException("Номер телефона не может быть пустым");
        }
        return new User(firstName.replaceAll(" ", ""), lastName.replaceAll(" ", ""), phone);
    }

    public User readUser(Long userId) throws Exception {
        List<User> users = repository.findAll();
        for (User user : users) {
            if (Objects.equals(user.getId(), userId)) {
                return user;
            }
        }

        throw new RuntimeException("User not found");
    }
    public void deleteUser (Long userId){
        repository.delete(userId);
    }


    public void updateUser(String userId, User update) {
        update.setId(Long.parseLong(userId));
        repository.update(Long.parseLong(userId), update);
    }
    public List<User> getAllUsers(){
        return this.repository.findAll();
    }
}
