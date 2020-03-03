package br.com.microservices.core.repository;

import br.com.microservices.core.model.ApplicationUser;
import br.com.microservices.core.model.Course;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ApplicationUserRepository extends PagingAndSortingRepository<ApplicationUser, Long> {

    ApplicationUser findByUsername(String username);
}
