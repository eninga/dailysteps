package edb.eningabiye.dailysteps.model;

public class User {
    private String phoneId;
    private String name;

    public User(String name, String phoneId) {
        this.name = name;
        this.phoneId = phoneId;
    }
    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }
    @Override
    public String toString() {
        return name;
    }
}
