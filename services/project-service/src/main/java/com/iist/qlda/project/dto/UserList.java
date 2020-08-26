package com.iist.qlda.project.dto;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class UserList {
		@SerializedName("users")
		@Expose
		private List<UserDTO> users = null;

		public List<UserDTO> getUsers() {
		return users;
		}

		public void setusers(List<UserDTO> users) {
		this.users = users;
		}

	
}
