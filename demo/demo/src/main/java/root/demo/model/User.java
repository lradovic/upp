package root.demo.model;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class User {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ime;
    private String prezime;
    private String grad;
    private String drzava;
    private String titula;
    @Column(unique=true, nullable = false)
    private String email;
    @Column(unique=true, nullable = false)
    private String username;
    @NotNull
    private String password;
    
    private String nOblasti;
    
    private boolean recenzent;
    
    private boolean aktiviran = false;
    
    private String role;
    
    
   	  	
	public User(Long id, String ime, String prezime, String grad, String drzava, String titula, @NotNull String email,
			@NotNull String username, @NotNull String password, String nOblasti, boolean recenzent, boolean aktiviran,
			String role) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.grad = grad;
		this.drzava = drzava;
		this.titula = titula;
		this.email = email;
		this.username = username;
		this.password = password;
		this.nOblasti = nOblasti;
		this.recenzent = recenzent;
		this.aktiviran = aktiviran;
		this.role = role;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public User() {
		// TODO Auto-generated constructor stub
	}


	public boolean isAktiviran() {
		return aktiviran;
	}

	public void setAktiviran(boolean aktiviran) {
		this.aktiviran = aktiviran;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getIme() {
		return ime;
	}




	public void setIme(String ime) {
		this.ime = ime;
	}




	public String getPrezime() {
		return prezime;
	}




	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}




	public String getGrad() {
		return grad;
	}




	public void setGrad(String grad) {
		this.grad = grad;
	}




	public String getDrzava() {
		return drzava;
	}




	public void setDrzava(String drzava) {
		this.drzava = drzava;
	}




	public String getTitula() {
		return titula;
	}




	public void setTitula(String titula) {
		this.titula = titula;
	}




	public String getEmail() {
		return email;
	}




	public void setEmail(String email) {
		this.email = email;
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




	public boolean isRecenzent() {
		return recenzent;
	}




	public void setRecenzent(boolean recenzent) {
		this.recenzent = recenzent;
	}




	public String getnOblasti() {
		return nOblasti;
	}




	public void setnOblasti(String nOblasti) {
		this.nOblasti = nOblasti;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", ime=" + ime + ", prezime=" + prezime + ", grad=" + grad + ", drzava=" + drzava
				+ ", titula=" + titula + ", email=" + email + ", username=" + username + ", password=" + password
				+ ", nOblasti=" + nOblasti + ", recenzent=" + recenzent + ", aktiviran=" + aktiviran + ", role=" + role
				+ "]";
	}


	
	


}
