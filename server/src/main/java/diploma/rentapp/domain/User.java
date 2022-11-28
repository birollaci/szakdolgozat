package diploma.rentapp.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import diploma.rentapp.annotation.Password;

@Entity
@Table(	name = "users", 
uniqueConstraints = { 
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email") 
})
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Size(min= 3, max = 24)
	private String username;

	@NotBlank
	@Password
	private String password;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String firstName;
	
	@NotBlank
	private String lastName;
	
    @NotBlank
    private String phoneNumber;

    @NotNull
    @OneToOne
    private Address homeAddress;

	@OneToOne
    private Address billingAddress;

	@NotNull
	@OneToOne
	private Contract contract;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
	}

	public User(String username, String password, String firstName, String lastName, String email,  String phoneNumber, Address homeAddress, Address billingAddress) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.homeAddress = homeAddress;
		this.billingAddress = billingAddress;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Address getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(Address homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

	public User copyValidValuesFrom(User user){
		if(user.username != null)
			this.username = user.username;
		if(user.password != null)
			this.password = user.password;
		if(user.firstName != null)
			this.firstName = user.firstName;
		if(user.lastName != null)
			this.lastName = user.lastName;
		if(user.email != null)
			this.email = user.email;
		if(user.phoneNumber != null)
			this.phoneNumber = user.phoneNumber;
		
		if(user.homeAddress != null)
			this.homeAddress.copyValidValuesFrom(user.homeAddress);
		if(user.billingAddress != null) {
			if(this.billingAddress != null) {
				this.billingAddress.copyValidValuesFrom(user.billingAddress);
			}
		}
		if(user.contract != null)
			this.contract.copyValidValuesFrom(user.contract);

		return this;
	}
}

// { 
//     "firstName": "Teszt1",
//     "lastName": "Elek",
//     "username": "elek",
//     "email": "elek@gmail.com",
//     "password": "12345",
//     "phoneNumber": "0612345678",
//     "homeAddress": {
//         "city": "Budapest",
//         "country": "Magyarorszag",
//         "county": "Lagymanyos",
//         "streetName": "Baross", 
//         "number": 1, 
//         "zipCode": "1117",
//         "door": "A1"
//     }
// }

// login
// {
//     "username": "admin",
//     "password": "admin"
// }