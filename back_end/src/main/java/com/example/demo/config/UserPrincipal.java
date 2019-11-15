package com.example.demo.config;

import com.example.demo.model.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
	private Integer id;

	private String first_name;

	private String second_name;

	@JsonIgnore
	private String email;

	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;

	public UserPrincipal(Integer id, String first_name, String second_name, String email, String password, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.first_name = first_name;
		this.second_name = second_name;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}


	public static UserPrincipal create(Employee user) {
		List<GrantedAuthority> authorities = user.getRole().getRolePrivileges().stream().map(privilege ->
				new SimpleGrantedAuthority(privilege.getPrivilege().getPrivilege_name())
		).collect(Collectors.toList());

//		System.out.println("privilges============> "+ authorities.toString());

		return new UserPrincipal(
				user.getEmp_id(),
				user.getFirst_name(),
				user.getSecond_name(),
				user.getEmail(),
				user.getPassword(),
				authorities
		);
	}


	public Integer getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

    public String getFirst_name() {
        return first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}
}
