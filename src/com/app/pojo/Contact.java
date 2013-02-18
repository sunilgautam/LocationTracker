package com.app.pojo;

import java.io.Serializable;

public class Contact implements Serializable
{

    private static final long serialVersionUID = -8583563695061055567L;
    private String Name;
    private String Phone;

    public Contact()
    {

    }

    public Contact(String name, String phone)
    {
	this.Name = name;
	this.Phone = phone;
    }

    public String getName()
    {
	return this.Name;
    }

    public void setName(String name)
    {
	this.Name = name;
    }

    public String getPhone()
    {
	return this.Phone;
    }

    public void setPhone(String phone)
    {
	this.Phone = phone;
    }

}
