package root.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import root.demo.model.Rad;

public interface RadRepository extends JpaRepository<Rad,Long> {
	
	public Rad findByNaslovRada(String naslovRada);

}
