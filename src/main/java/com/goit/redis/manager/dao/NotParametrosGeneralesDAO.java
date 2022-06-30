package com.goit.redis.manager.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.springframework.util.ObjectUtils;

import com.goit.helper.enums.NemonicoParametros;
import com.goit.redis.manager.dto.GeneralParametrosGeneralesDTO;
import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.model.NotParametrosGenerales;

import lombok.NonNull;

public class NotParametrosGeneralesDAO extends BaseDAO<NotParametrosGenerales,Integer>{
	
	protected NotParametrosGeneralesDAO(){super(NotParametrosGenerales.class);}

    @PersistenceContext
    private EntityManager em;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void persist(NotParametrosGenerales t) throws PersistenceException {
        super.persist(t);
    }

    @Override
    public void update(NotParametrosGenerales t) throws PersistenceException {
        super.update(t);
    }

    @Override
    public Optional<NotParametrosGenerales> find(@NonNull Integer id) {
        return super.find(id);
    }
    
	public NotParametrosGenerales findYValidar(NemonicoParametros objParametros) throws BOException {

		NotParametrosGenerales objNotParametrosGenerales = findByName(objParametros.getName());

		Optional<NotParametrosGenerales> optNotParametrosGenerales= null;
		// Valida que exista
		if (ObjectUtils.isEmpty(objNotParametrosGenerales))
			throw new BOException("seg.warn.parametroNoExiste", new Object[] { objParametros.getName() });

		// Valida este activo.
		if (objNotParametrosGenerales.getEstado() == null
				|| !"A".equalsIgnoreCase(objNotParametrosGenerales.getEstado()))
			throw new BOException("seg.warn.parametroInactivo", new Object[] { objParametros.getName() });

		//optNotParametrosGenerales.get().setDescripcion(objNotParametrosGenerales.getDescripcion());;
		
		return objNotParametrosGenerales;
	}
		
	public NotParametrosGenerales findByName(@NonNull String id) {
		
		try {	
			return em.createQuery(
						"SELECT usd \n" +
						"  FROM NotParametrosGenerales usd \n" +
						"WHERE upper(usd.nombre)=upper(:nombre) \n"+
						"AND usd.estado='A' ",NotParametrosGenerales.class)
						.setParameter("nombre",id.toLowerCase())
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
		
	}
	
    public Optional<NotParametrosGenerales> findWithValidacionCampo(Integer id) throws BOException {

        Optional<NotParametrosGenerales> objNotParametrosGenerales=find(id);

        // Valida que exista
        if (!objNotParametrosGenerales.isPresent())
            throw new BOException("not.warn.campoNoExiste", new Object[] { "not.campo.idParametrosGenerales" });

        // Valida este activo.
        if (objNotParametrosGenerales.get().getEstado()==null || !"A".equalsIgnoreCase(objNotParametrosGenerales.get().getEstado()))
            throw new BOException("not.warn.campoInactivo", new Object[] { "not.campo.idParametrosGenerales" });

        return objNotParametrosGenerales;
    }
    
    public Optional<GeneralParametrosGeneralesDTO> findGroup(String strParametroGeneral) throws BOException{
		
    	StringBuilder strJPQL = new StringBuilder();
		
		Optional<GeneralParametrosGeneralesDTO> optObtenerGrupo = null;

		try {

			strJPQL.append(" SELECT ng.idParametroGeneral as idParametroGeneral, ");
			strJPQL.append(" 		ng.descripcion as descripcion,");
			strJPQL.append(" 		ng.estado as estado,");
			strJPQL.append(" 		ng.nombre as nombre,");
			strJPQL.append(" 		ng.tipoValor as tipoValor,");
			strJPQL.append(" 		ng.valor as valor");
			strJPQL.append(" FROM 	NotParametrosGenerales ng");
			strJPQL.append(" WHERE	 ng.estado='A' ");
			strJPQL.append(" AND	 ng.nombre=:nombre");

			Query query = em.createQuery(strJPQL.toString());
			query.setParameter("nombre", strParametroGeneral);

			optObtenerGrupo = (Optional<GeneralParametrosGeneralesDTO>) query.getSingleResult();

			return optObtenerGrupo;

		} catch (NoResultException e) {
			return null;
		}
	}
    
    public Long getCountParametrosGenerales() {
    	StringBuilder strJPQL = new StringBuilder();

		try {

			strJPQL.append(" SELECT count(ap)");
			strJPQL.append(" FROM 	NotParametrosGenerales ap");
			strJPQL.append(" WHERE	 ap.estado='A' ");

			Query query = em.createQuery(strJPQL.toString());

			Long lonCantQuery = (Long) query.getSingleResult();

			return lonCantQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}
    	
    }
    
    public List<GeneralParametrosGeneralesDTO> getParametrosGenerales(){
    	StringBuilder strJPQL = new StringBuilder();
    	strJPQL.append(" SELECT ap.idParametroGeneral as idParametroGeneral, ap.descripcion as descripcion,");
		strJPQL.append(" 		ap.nombre as nombre,");
		strJPQL.append(" 		ap.tipoValor as tipoValor,");
		strJPQL.append(" 		ap.estado as estado,");
		strJPQL.append(" 		ap.valor as valor");
		strJPQL.append(" FROM 	NotParametrosGenerales ap");
		strJPQL.append(" WHERE	 ap.estado='A' ");
		
		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);
		
		return query.getResultList().stream().map(tuple -> {
			return GeneralParametrosGeneralesDTO.builder()
					.idParametroGeneral(tuple.get("idParametroGeneral",Integer.class))
					.descripcion(tuple.get("descripcion", String.class))
					.nombre(tuple.get("nombre", String.class))
					.tipoValor(tuple.get("tipoValor", String.class))
					.estado(tuple.get("estado", String.class))
					.valor(tuple.get("valor", String.class))
					.build();
		}).collect(Collectors.toList());
    }

}
