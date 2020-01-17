package root.demo.repository;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.*;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	public User findByEmail(String email);
	public User findByUsername(String username);


}
