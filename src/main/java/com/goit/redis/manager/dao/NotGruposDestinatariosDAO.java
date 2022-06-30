package com.goit.redis.manager.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import com.goit.redis.manager.dto.GruposDestinatariosDTO;
import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.model.NotDestinatarios;
import com.goit.redis.manager.model.NotGrupos;
import com.goit.redis.manager.model.NotGruposDestinatarios;

import lombok.NonNull;

@Service
public class NotGruposDestinatariosDAO extends BaseDAO<NotGruposDestinatarios,Integer>{
    protected NotGruposDestinatariosDAO() {
        super(NotGruposDestinatarios.class);
    }

    @PersistenceContext
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void persist(NotGruposDestinatarios t) throws PersistenceException {
        super.persist(t);
    }

    @Override
    public void update(NotGruposDestinatarios t) throws PersistenceException {
        super.update(t);
    }

    @Override
    public Optional<NotGruposDestinatarios> find(@NonNull Integer id) {
        return super.find(id);
    }
    
    public List<NotGruposDestinatarios> consultarGruposDestinatarios(Integer idGrupo){
    	
    	
		return null;
    	
    }
    
    /**
	 * 
	 * Consulta en la tabla NotGruposDestinatarios y valida si exite y si esta activo
	 * 
	 * @author Vladimir Kozisck
	 * @param id
	 * @return
	 * @throws BOException 
	 */
	public Optional<NotGruposDestinatarios> findYValidar(Integer id) throws BOException {
		
		Optional<NotGruposDestinatarios> optNotGruposDestinatarios=find(id);
		
		// Valida que exista
		if (!optNotGruposDestinatarios.isPresent())
			throw new BOException("not.warn.grupoNoExiste", new Object[] { id });

		// Valida este activo.
		if (optNotGruposDestinatarios.get().getEstado()==null || !"A".equalsIgnoreCase(optNotGruposDestinatarios.get().getEstado()))
			throw new BOException("not.warn.grupoNInactivo", new Object[] { id });
				
		return optNotGruposDestinatarios;
	}

    
    
	/**
	 * Obtener Data de Grupo y Destinatarios por el idGrupo
	 * 
	 * @author Vladimir Kozisck
	 * @param id
	 * @return
	 */
	public List<GruposDestinatariosDTO> getDestinatariosXGrupo(Integer id) {

		try {

			StringBuilder strJPQLBase = new StringBuilder();
			strJPQLBase.append("select gd.idGrupoDestinatario as idGrupoDestinatario, ");
			strJPQLBase.append("	   gd.notGrupos as notGrupos,  ");
			strJPQLBase.append("	   gd.notDestinatarios as notDestinatarios,  ");
			strJPQLBase.append("	   gd.estado as estado  ");
			strJPQLBase.append("from  NotGruposDestinatarios gd ");
			strJPQLBase.append("join  gd.notDestinatarios d ");
			strJPQLBase.append("join  gd.notGrupos g ");
			strJPQLBase.append("where g.idGrupo=:idGrupo ");
			strJPQLBase.append("and   gd.estado='A' ");

			TypedQuery<Tuple> query = em.createQuery(strJPQLBase.toString(), Tuple.class);
			query.setParameter("idGrupo", id);
			
			return query.getResultList().stream()
					.map(tuple -> GruposDestinatariosDTO.builder()
							.estado(tuple.get("estado", String.class))
							.idGrupoDestinatario(tuple.get("idGrupoDestinatario", Integer.class))
							.notGrupos(tuple.get("notGrupos", NotGrupos.class))
							.notDestinatarios(tuple.get("notDestinatarios", NotDestinatarios.class))
							.build())
					.collect(Collectors.toList());
			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<GruposDestinatariosDTO> getDestinatariosXGrupo_v2(Integer id) {

		try {

			StringBuilder strJPQLBase = new StringBuilder();
			strJPQLBase.append("select gd.notDestinatarios as notDestinatarios ");
			strJPQLBase.append("from  NotGruposDestinatarios gd ");
			strJPQLBase.append("join  gd.notDestinatarios d ");
			strJPQLBase.append("where gd.notGrupos.idGrupo=:idGrupo ");
			strJPQLBase.append("and   gd.estado='A' ");

			TypedQuery<Tuple> query = em.createQuery(strJPQLBase.toString(), Tuple.class);
			query.setParameter("idGrupo", id);
			
			return query.getResultList().stream()
					.map(tuple -> GruposDestinatariosDTO.builder()
							.notDestinatarios(tuple.get("notDestinatarios", NotDestinatarios.class))
							.build())
					.collect(Collectors.toList());
			
		} catch (NoResultException e) {
			return null;
		}
	}
	   
}
