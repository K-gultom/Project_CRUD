package com.example.projek;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataModel {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty address;
    private final StringProperty telp;

    public DataModel(String id, String name, String address, String telp) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
        this.telp = new SimpleStringProperty(telp);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getTelp() {
        return telp.get();
    }

    public StringProperty telpProperty() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp.set(telp);
    }
}
