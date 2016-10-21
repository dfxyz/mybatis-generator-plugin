package output.models;

public class Test1 {
    public Integer id;

    public String username;

    public String password;

    public String description;

    public Test1(Integer id, String username, String password, String description) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.description = description;
    }

    public Test1() {
        super();
    }
}