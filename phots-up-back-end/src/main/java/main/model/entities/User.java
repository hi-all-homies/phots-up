package main.model.entities;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = {"id", "username"})
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;
	private String username;
	
	@JsonIgnore
	private String password;
	
	@JsonIgnore
	@Enumerated(EnumType.STRING)
	@ElementCollection(fetch = FetchType.LAZY)
	@Column(name = "role")
	private Set<UserRole> roles = new HashSet<>();
	
	public User (Long id, String username, String password) {
		this.id = id;
		this.username= username;
		this.password = password;
	}
}
