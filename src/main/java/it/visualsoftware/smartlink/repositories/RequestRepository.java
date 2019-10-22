package it.visualsoftware.smartlink.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import it.visualsoftware.smartlink.models.Request;

public interface RequestRepository extends MongoRepository<Request, String> {
	/**
	 * trova Customer per uUId
	 * @param uUId
	 * @return Customer
	 */
	Request findByUUId(String uUId);
}
