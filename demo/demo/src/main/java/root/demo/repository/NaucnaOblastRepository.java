package root.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.Casopis;
import root.demo.model.NaucnaOblast;

@Repository
public interface NaucnaOblastRepository extends JpaRepository<NaucnaOblast,Long> {
	
	public NaucnaOblast findByNaucnaOblast(String naucnaOblast);
	public Optional<NaucnaOblast> findById(Long id);

}


