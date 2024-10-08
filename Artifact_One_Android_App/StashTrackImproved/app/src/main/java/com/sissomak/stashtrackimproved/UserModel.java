package com.sissomak.sqldemo;

/**
 * UserModel Class
 * Functionality:
 *      - Template for user information in the Users database
 *
 * @author Aaron Sissom
 * @course CS-499 Computer Science Capstone
 * @school Southern New Hampshire University
 */

public class UserModel
{
    private int id;
    private String name;
    private String password;
    private String phone;
    private String email;

    public UserModel()
    {
    }

    public UserModel(String name, String password, String phone, String email)
    {
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public UserModel(int id, String name, String password, String phone, String email)
    {
        this.id = id;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }


    // toString method
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
