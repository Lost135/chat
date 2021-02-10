package org.lost.domain;

public class NUser {
    private String id;
    private String name;
    private String password;
    private String email;
    private String oldPassword;

    public NUser(String id, String name, String password, String email, String oldPassword) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.oldPassword = oldPassword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
