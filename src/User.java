import java.io.IOException;

public class User {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) throws UserException {
        if (name.isBlank()) {
            throw new UserException("Name cannot be blank.");
        }
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws UserException {
        if (password.isBlank()) {
            throw new UserException("Password cannot be blank");
        }
        this.password = password;
    }
}

class UserException extends IOException {
    public UserException() {
        super();
    }

    public UserException(String msg) {
        super(msg);
    }
}
