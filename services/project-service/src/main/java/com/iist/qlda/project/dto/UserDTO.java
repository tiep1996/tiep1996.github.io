package com.iist.qlda.project.dto;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDTO implements Serializable {
//    @SerializedName("id")
    @Expose
    private int id;
//    @SerializedName("login")
    @Expose
    private String login;
//    @SerializedName("first_name")
    @Expose
    private String firstname;
//    @SerializedName("last_name")
    @Expose
    private String lastname;
    
//    @SerializedName("mail")
    @Expose
    private String mail;

//    @SerializedName("created_on")
    @Expose
    private String created_on;

//    @SerializedName("last_login_on")
    @Expose
    private String last_login_on;

	

	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getLogin() {
		return login;
	}



	public void setLogin(String login) {
		this.login = login;
	}



	public String getFirstname() {
		return firstname;
	}



	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}



	public String getLastname() {
		return lastname;
	}



	public void setLastname(String lastname) {
		this.lastname = lastname;
	}



	public String getMail() {
		return mail;
	}



	public void setMail(String mail) {
		this.mail = mail;
	}



	public String getCreated_on() {
		return created_on;
	}



	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}



	public String getLast_login_on() {
		return last_login_on;
	}



	public void setLast_login_on(String last_login_on) {
		this.last_login_on = last_login_on;
	}



	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", login=" + login + ", first_name=" + firstname + ", last_name=" + lastname
				+ ", mail=" + mail + ", created_on=" + created_on + ", last_login_on=" + last_login_on + "]";
	}

	
	
}
