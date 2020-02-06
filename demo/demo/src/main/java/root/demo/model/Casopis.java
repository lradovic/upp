package root.demo.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class Casopis {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nazivMagazina;
    private String iSSNbroj;
    private String recenzent1;
    private String recenzent2;
    private String urednik1;
    private String urednik2;
    
    private boolean placanje;
    
    private String nOblasti;
    
    private boolean aktiviran = false;
    
    private String glavniUrednik;
    
    @OneToMany(mappedBy = "casopis")
    private Set<Rad> radovi;
    
    public Casopis() {}

 
	@Override
	public String toString() {
		return "Casopis [id=" + id + ", nazivMagazina=" + nazivMagazina + ", iSSNbroj=" + iSSNbroj + ", recenzent1="
				+ recenzent1 + ", recenzent2=" + recenzent2 + ", urednik1=" + urednik1 + ", urednik2=" + urednik2
				+ ", placanje=" + placanje + ", nOblasti=" + nOblasti + ", aktiviran=" + aktiviran + ", glavniUrednik="
				+ glavniUrednik + "]";
	}




	



	public Set<Rad> getRadovi() {
		return radovi;
	}


	public void setRadovi(Set<Rad> radovi) {
		this.radovi = radovi;
	}


	public Casopis(Long id, String nazivMagazina, String iSSNbroj, String recenzent1, String recenzent2,
			String urednik1, String urednik2, boolean placanje, String nOblasti, boolean aktiviran,
			String glavniUrednik, Set<Rad> radovi) {
		super();
		this.id = id;
		this.nazivMagazina = nazivMagazina;
		this.iSSNbroj = iSSNbroj;
		this.recenzent1 = recenzent1;
		this.recenzent2 = recenzent2;
		this.urednik1 = urednik1;
		this.urednik2 = urednik2;
		this.placanje = placanje;
		this.nOblasti = nOblasti;
		this.aktiviran = aktiviran;
		this.glavniUrednik = glavniUrednik;
		this.radovi = radovi;
	}


	public String getGlavniUrednik() {
		return glavniUrednik;
	}




	public void setGlavniUrednik(String glavniUrednik) {
		this.glavniUrednik = glavniUrednik;
	}




	public String getnOblasti() {
		return nOblasti;
	}



	public void setnOblasti(String nOblasti) {
		this.nOblasti = nOblasti;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNazivMagazina() {
		return nazivMagazina;
	}

	public void setNazivMagazina(String nazivMagazina) {
		this.nazivMagazina = nazivMagazina;
	}

	
	public String getiSSNbroj() {
		return iSSNbroj;
	}






	public void setiSSNbroj(String iSSNbroj) {
		this.iSSNbroj = iSSNbroj;
	}






	public String getRecenzent1() {
		return recenzent1;
	}

	public void setRecenzent1(String recenzent1) {
		this.recenzent1 = recenzent1;
	}

	public String getRecenzent2() {
		return recenzent2;
	}

	public void setRecenzent2(String recenzent2) {
		this.recenzent2 = recenzent2;
	}

	public String getUrednik1() {
		return urednik1;
	}

	public void setUrednik1(String urednik1) {
		this.urednik1 = urednik1;
	}

	public String getUrednik2() {
		return urednik2;
	}

	public void setUrednik2(String urednik2) {
		this.urednik2 = urednik2;
	}

	public boolean isPlacanje() {
		return placanje;
	}

	public void setPlacanje(boolean placanje) {
		this.placanje = placanje;
	}

	public boolean isAktiviran() {
		return aktiviran;
	}

	public void setAktiviran(boolean aktiviran) {
		this.aktiviran = aktiviran;
	}

    
}
