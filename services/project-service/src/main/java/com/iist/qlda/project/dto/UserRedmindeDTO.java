package com.iist.qlda.project.dto;

import java.util.List;

public class UserRedmindeDTO {
private List<UserDTO> users;
private Integer total_count;
private Integer offset;
private Integer limit;

public List<UserDTO> getUsers() {
	return users;
}

public void setUsers(List<UserDTO> users) {
	this.users = users;
}

public Integer getTotal_count() {
	return total_count;
}

public void setTotal_count(Integer total_count) {
	this.total_count = total_count;
}

public Integer getOffset() {
	return offset;
}

public void setOffset(Integer offset) {
	this.offset = offset;
}

public Integer getLimit() {
	return limit;
}

public void setLimit(Integer limit) {
	this.limit = limit;
}


}
