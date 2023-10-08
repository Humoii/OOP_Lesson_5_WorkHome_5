package notebook.model.repository.impl;

import notebook.model.dao.impl.FileOperation;
import notebook.util.DBConnector;
import notebook.util.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.model.repository.GBRepository;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository implements GBRepository {
    private final UserMapper mapper;
    private final FileOperation operation;

    public UserRepository(FileOperation operation) {
        this.mapper = new UserMapper();
        this.operation = operation;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = operation.readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id){
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        write(users);
        return user;
    }

    @Override
//    Создали метод findByID дял поиска по ID
    public Optional<User> findById(Long id, List<User> users) {
//        List<User> users = findAll();
        User user = users.stream()
                .filter(u -> u.getId()
                        .equals(id))
                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        return Optional.of(user);
//        return Optional.empty();
    }

    @Override
//    Сократили метод убрав поиск по ID
    public Optional<User> update(Long userId, User update) {
        List<User> users = findAll();
//        User editUser = users.stream()
//                .filter(u -> u.getId()
//                        .equals(userId))
//                .findFirst().orElseThrow(() -> new RuntimeException("User not found"));
        User editUser = findById(userId, users).get();
        editUser.setFirstName(update.getFirstName());
        editUser.setLastName(update.getLastName());
        editUser.setPhone(update.getPhone());
        write(users);
        return Optional.of(update);
    }

    @Override
    public boolean delete(Long userId) {
        List<User> users = findAll();
        int a = Integer.parseInt(String.valueOf(userId));
        users.remove(a-1);
        for (User u : users){
            u.setId((long) (users.indexOf(u) + 1));
        }
        write(users);
        return false;
    }

    private void write(List<User> users) {
        List<String> lines = new ArrayList<>();
        for (User u: users) {
            lines.add(mapper.toInput(u));
        }
        operation.saveAll(lines);
    }

}
