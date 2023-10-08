package notebook.view;

import notebook.controller.UserController;
import notebook.model.User;
import notebook.model.repository.impl.UserRepository;
import notebook.util.Commands;

import java.util.Scanner;

public class UserView {
    private final UserController userController;

    public UserView(UserController userController) {
        this.userController = userController;
    }

    public void run(){
        Commands com;

        while (true) {
            String command = prompt("Введите команду: ");
            com = Commands.valueOf(command);
            if (com == Commands.EXIT) return;
            switch (com) {
                case CREATE -> {
                    User u = UserController.createUser();
                    userController.saveUser(u);
                }
                case READ -> {
                    String id = prompt("Идентификатор пользователя: ");
                    try {
                        User user = userController.readUser(Long.parseLong(id));
                        System.out.println(user);
                        System.out.println();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                case UPDATE -> {
                    String userId = prompt("Enter user id: ");
                    userController.updateUser(userId, UserController.createUser());
                }
                case LIST -> {
                    System.out.println(userController.getAllUsers());
                }
                case DELETE -> {
                    String id = prompt("Идентификатор пользователя: ");
                    try {
                        userController.deleteUser(Long.parseLong(id));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public static String prompt(String message) {
        Scanner in = new Scanner(System.in);
        System.out.print(message);
        return in.nextLine();
    }

}
