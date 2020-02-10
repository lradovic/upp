package root.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Rad {
	
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String naslovRada;
    private String kljucniPojmovi;
    private String apstrakt;
    private String naucnaOblast;
    private String autor;
    private String koautori;
    private String pdf;   
    private boolean objavljen = false;
    
    @ManyToOne
    private Casopis casopis;

	public Rad() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Rad(Long id, String naslovRada, String kljucniPojmovi, String apstrakt, String naucnaOblast, String autor,
			String koautori, String pdf, boolean objavljen, Casopis casopis) {
		super();
		this.id = id;
		this.naslovRada = naslovRada;
		this.kljucniPojmovi = kljucniPojmovi;
		this.apstrakt = apstrakt;
		this.naucnaOblast = naucnaOblast;
		this.autor = autor;
		this.koautori = koautori;
		this.pdf = pdf;
		this.objavljen = objavljen;
		this.casopis = casopis;
	}

	


	public String getAutor() {
		return autor;
	}



	public void setAutor(String autor) {
		this.autor = autor;
	}



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNaslovRada() {
		return naslovRada;
	}

	public void setNaslovRada(String naslovRada) {
		this.naslovRada = naslovRada;
	}

	public String getKljucniPojmovi() {
		return kljucniPojmovi;
	}

	public void setKljucniPojmovi(String kljucniPojmovi) {
		this.kljucniPojmovi = kljucniPojmovi;
	}

	public String getApstrakt() {
		return apstrakt;
	}

	public void setApstrakt(String apstrakt) {
		this.apstrakt = apstrakt;
	}

	public String getNaucnaOblast() {
		return naucnaOblast;
	}

	public void setNaucnaOblast(String naucnaOblast) {
		this.naucnaOblast = naucnaOblast;
	}

	public String getKoautori() {
		return koautori;
	}

	public void setKoautori(String koautori) {
		this.koautori = koautori;
	}

	public String getPdf() {
		return pdf;
	}

	public void setPdf(String pdf) {
		this.pdf = pdf;
	}

	public boolean isObjavljen() {
		return objavljen;
	}

	public void setObjavljen(boolean objavljen) {
		this.objavljen = objavljen;
	}

	public Casopis getCasopis() {
		return casopis;
	}

	public void setCasopis(Casopis casopis) {
		this.casopis = casopis;
	}
    
    
}
