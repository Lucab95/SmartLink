package it.visualsoftware.smartlink.repositories;
import it.visualsoftware.smartlink.models.Request;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RequestRepository extends MongoRepository<Request, String> {
	/**
	 * trova Customer per uUId
	 * @param uUId
	 * @return Customer
	 */
	Request findByUUId(String uUId);
}
