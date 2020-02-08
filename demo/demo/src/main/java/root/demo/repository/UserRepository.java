package root.demo.repository;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import root.demo.model.*;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
	
	public User findByEmail(String email);
	public User findByUsername(String username);
	public List<User> findByRole(String role);


}
