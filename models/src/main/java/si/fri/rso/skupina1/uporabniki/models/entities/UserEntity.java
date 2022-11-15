package si.fri.rso.skupina1.uporabniki.models.entities;

import javax.persistence.*;

@Entity
@Table(name = "Appuser")
@NamedQueries(value = {
		@NamedQuery(name = "UserEntity.getAll", query = "SELECT u FROM UserEntity u")
})
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "surname")
	private String surname;

	@Column(name = "city")
	private String city;

	@Column(name = "postalCode")
	private Integer postalCode;

	@Column(name = "address")
	private String address;

	@Column(name = "canDeliver")
	private Boolean canDeliver;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(Integer postalCode) {
		this.postalCode = postalCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getCanDeliver() {
		return canDeliver;
	}

	public void setCanDeliver(Boolean canDeliver) {
		this.canDeliver = canDeliver;
	}
}