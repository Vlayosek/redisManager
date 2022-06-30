package com.goit.redis.manager.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.model.NotDestinatarios;
import com.goit.redis.manager.model.NotParametrosGenerales;

import lombok.NonNull;

@Service
public class NotDestinatariosDAO extends BaseDAO<NotDestinatarios,String>{

	protected NotDestinatariosDAO() {
		super(NotDestinatarios.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(NotDestinatarios t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotDestinatarios t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotDestinatarios> find(@NonNull String id) {
		return super.find(id);
	}	
	
	public Optional<NotDestinatarios> findYValidar(String id) throws BOException {
		
		Optional<NotDestinatarios> optNotDestinatarios=find(id);
		
		// Valida que exista
		if (!optNotDestinatarios.isPresent())
			throw new BOException("not.warn.destinatarioNoExiste", new Object[] { id});

		// Valida este activo.
		if (optNotDestinatarios.get().getEstado()==null || !"A".equalsIgnoreCase(optNotDestinatarios.get().getEstado()))
			throw new BOException("not.warn.destinatarioInactivo", new Object[] {id});
				
		return optNotDestinatarios;
	}
	
	public NotDestinatarios findBy(String idNotDestinatario, String idUsuario, String idEmpresa) throws BOException{
		try {	
			return em.createQuery(
						"SELECT usd \n" +
						"  FROM NotDestinatarios usd \n" +
						"WHERE usd.idDestinatario =: idDestinatario \n"+
						"AND usd.idUsuario=: idUsuario \n"+
						"AND usd.idEmpresa =: idEmpresa \n"+
						"AND usd.estado='A' ",NotDestinatarios.class)
						.setParameter("idDestinatario",idNotDestinatario)
						.setParameter("idUsuario",idUsuario)
						.setParameter("idEmpresa",idEmpresa)
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
}