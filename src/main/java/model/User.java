/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.google.cloud.firestore.DocumentSnapshot;

/**
 *
 * @author thuy
 */
public class User extends ModelBase {

    private String username;
    private String password;
    private String group;
    private String code;
    private String dob;
    private String faculty;
    private String fullname;
    private String phone;
    private String role;
    private String token;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getGroup() {
        return group;
    }

    public String getCode() {
        return code;
    }

    public String getDob() {
        return dob;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getFullname() {
        return fullname;
    }

    public String getPhone() {
        return phone;
    }

    public String getRole() {
        return role;
    }

    public String getToken() {
        return token;
    }
    
    public String getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void setToken(String token) {
        this.token = token;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public void fromQueryDocument(DocumentSnapshot doc) {
        super.fromQueryDocument(doc);
        username = doc.getString("username");
        password = doc.getString("password");
        group = doc.getString("class");
        code = doc.getString("code");
        dob = doc.getString("dob");
        faculty = doc.getString("faculty");
        fullname = doc.getString("fullname");
        phone = doc.getString("phone");
        role = doc.getString("role");
    }

    public String toString() {
        return "id: " + id + ", username: " + username + ", password: " + password;
    }
}
