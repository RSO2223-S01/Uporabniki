package si.fri.rso.skupina1.uporabniki.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import si.fri.rso.skupina1.uporabniki.lib.User;
import si.fri.rso.skupina1.uporabniki.models.converters.UserConverter;
import si.fri.rso.skupina1.uporabniki.models.entities.UserEntity;
import si.fri.rso.skupina1.uporabniki.services.config.RestProperties;

@RequestScoped
public class UserBean {

	private Logger log = Logger.getLogger(UserBean.class.getName());

	@Inject
	private EntityManager em;

	@Inject
	private RestProperties restProperties;


	@Timeout(value = 2, unit = ChronoUnit.SECONDS)
	@CircuitBreaker(requestVolumeThreshold = 5, successThreshold = 2, failureRatio = 0.5)
	public List<User> getUsers() {

		if (restProperties.getFault()) {
			int a = 1/0;
		}

		TypedQuery<UserEntity> query = em.createNamedQuery(
				"UserEntity.getAll", UserEntity.class);

		List<UserEntity> resultList = query.getResultList();

		return resultList.stream().map(UserConverter::toDto).collect(Collectors.toList());

	}


	public List<User> getUsersFallback(UriInfo uriInfo) {
		return new ArrayList<>();
	}

	@Fallback(fallbackMethod = "getUsersFallback")
	public List<User> getUserFilter(UriInfo uriInfo) {

		if (restProperties.getFault()) {
			int a = 1/0;
		}

		QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
				.build();

		return JPAUtils.queryEntities(em, UserEntity.class, queryParameters).stream()
				.map(UserConverter::toDto).collect(Collectors.toList());
	}

	public User getUser(Integer id) {

		UserEntity userEntity = em.find(UserEntity.class, id);

		if (userEntity == null) {
			throw new NotFoundException();
		}

		User user = UserConverter.toDto(userEntity);

		return user;
	}

	public User createUser(User user) {

		UserEntity userEntity = UserConverter.toEntity(user);
		userEntity.setCanDeliver(false);
		try {
			beginTx();
			em.persist(userEntity);
			commitTx();
		} catch (Exception e) {
			rollbackTx();
		}

		if (userEntity.getId() == null) {
			throw new RuntimeException("Entity was not persisted");
		}

		return UserConverter.toDto(userEntity);
	}

	public User putUser(Integer id, User user) {

		UserEntity c = em.find(UserEntity.class, id);

		if (c == null) {
			return null;
		}

		UserEntity updatedUserEntity = UserConverter.toEntity(user);

		try {
			beginTx();
			updatedUserEntity.setId(c.getId());
			updatedUserEntity = em.merge(updatedUserEntity);
			commitTx();
		} catch (Exception e) {
			rollbackTx();
		}

		return UserConverter.toDto(updatedUserEntity);
	}

	public boolean deleteUser(Integer id) {

		UserEntity user = em.find(UserEntity.class, id);

		if (user != null) {
			try {
				beginTx();
				em.remove(user);
				commitTx();
			} catch (Exception e) {
				rollbackTx();
			}
		} else {
			return false;
		}

		return true;
	}

	private void beginTx() {
		if (!em.getTransaction().isActive()) {
			em.getTransaction().begin();
		}
	}

	private void commitTx() {
		if (em.getTransaction().isActive()) {
			em.getTransaction().commit();
		}
	}

	private void rollbackTx() {
		if (em.getTransaction().isActive()) {
			em.getTransaction().rollback();
		}
	}
}
