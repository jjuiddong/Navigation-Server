package navi.user;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="user")
public class User implements Serializable {

	private static final long serialVersionUID = -4232308627519157134L;
	
	@Id
	public String id;
	public String password;	
	public String firstName;
	public String lastName;
	
	@Enumerated(EnumType.STRING)
	public RoleName roleName;
	
	public String getId() {
		return this.id;
	}
	
	public void setId(String userId) {
		this.id = userId;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public RoleName getRoleName() {
		return this.roleName;
	}
	
	public void setRoleName(RoleName roleName) {
		this.roleName = roleName;
	}	
	
}
