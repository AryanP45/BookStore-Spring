package com.dbms.bookstore.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "Roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false, unique = true)
	@NotEmpty
	private String name;

	@ManyToMany(mappedBy = "roles")
	private List<User> users;

	Integer getId() {
		return id;
	}

	void setId(Integer id) {
		this.id = id;
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	List<User> getUsers() {
		return users;
	}

	void setUsers(List<User> users) {
		this.users = users;
	}

}
