package com.dailyplanner.entity;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;


@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private boolean enabled;

	@Column(nullable = false, unique = true)
	@Email(message = "Email address should be valid")
	private String email;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String confirmPassword;

	@Column(nullable = false)
	private String address;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/mm/yyyy")
	@Column(nullable = false)
	private Date createdDate;


	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id", updatable = false, insertable = false))
	private List<Role> roles = new ArrayList<>();
}