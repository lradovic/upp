package root.demo.repository;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.*;

@Repository
public interface CasopisRepository extends JpaRepository<Casopis,Long> {
	
	public Casopis findByNazivMagazina(String nazivMagazina);
	public Casopis findByISSNbroj(String ISSNbroj);

}
