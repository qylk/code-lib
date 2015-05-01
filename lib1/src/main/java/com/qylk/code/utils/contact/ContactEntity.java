package com.qylk.code.utils.contact;

import java.util.List;

/**
 * Created by qylk on 15/4/23.
 */
public class ContactEntity {
    private int id;
    private String displayName;
    private String email;
    private List<String> phones;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}
