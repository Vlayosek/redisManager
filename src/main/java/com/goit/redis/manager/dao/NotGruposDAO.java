package com.goit.redis.manager.dao;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
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

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.goit.helper.FechasHelper;
import com.goit.helper.enums.FormatoFecha;
import com.goit.redis.manager.dto.CamposFiltradosDTO;
import com.goit.redis.manager.dto.DestinatariosDTO;
import com.goit.redis.manager.dto.GetGruposDestinatariosDTO;
import com.goit.redis.manager.dto.GruposAndDestinatariosDTO;
import com.goit.redis.manager.dto.GruposDTO;
import com.goit.redis.manager.dto.GruposDestinatariosDTO;
import com.goit.redis.manager.dto.ObtenerGrupoDTO;
import com.goit.redis.manager.dto.UsuarioLogin;
import com.goit.redis.manager.exceptions.BOException;
import com.goit.redis.manager.model.NotDestinatarios;
import com.goit.redis.manager.model.NotGrupos;

import lombok.NonNull;

@Service
public class NotGruposDAO extends BaseDAO<NotGrupos,Integer>{

	protected NotGruposDAO() {
		super(NotGrupos.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(NotGrupos t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotGrupos t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotGrupos> find(@NonNull Integer id) {
		return super.find(id);
	}
	
	/**
	 * 
	 * Consulta en la tabla SecParametro y valida si exite y si esta activo
	 * 
	 * @author Bryan Zamora
	 * @param objParametros
	 * @return
	 * @throws BOException 
	 */
	public Optional<NotGrupos> findYValidar(Integer id) throws BOException {
		
		Optional<NotGrupos> optNotGrupos=find(id);
		
		// Valida que exista
		if (!optNotGrupos.isPresent())
			throw new BOException("not.warn.grupoNoExiste", new Object[] { id});

		// Valida este activo.
		if (optNotGrupos.get().getEstado()==null || !"A".equalsIgnoreCase(optNotGrupos.get().getEstado()))
			throw new BOException("not.warn.grupoNInactivo", new Object[] {id});
				
		return optNotGrupos;
	}
	

	public List<ObtenerGrupoDTO> findGroup(String tipo,String idUsuario, String idEmpresa) throws BOException{
		
		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT ng.nombre as nombre, ");
		strJPQL.append(" 		ng.idGrupo as idGrupo, ");
		strJPQL.append(" 		ng.cantidadDestinatarios as cantidadDestinatarios, ");
		strJPQL.append(" 		ng.fechaCreacion as fechaCreacion ");
		strJPQL.append(" FROM 	NotGrupos ng");
		strJPQL.append(" WHERE	 ng.estado='A' ");
		strJPQL.append(" AND	 ng.tipo=:tipo ");
		strJPQL.append(" AND	 ng.idUsuario=:idUsuario ");
		strJPQL.append(" AND	 ng.idEmpresa=:idEmpresa ");

		
		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);
		query.setParameter("tipo", tipo);
		query.setParameter("idUsuario", idUsuario);
		query.setParameter("idEmpresa", idEmpresa);

		
		return query.getResultList().stream().map(tuple -> {
			return ObtenerGrupoDTO.builder()
					.nombre(tuple.get("nombre", String.class))
					.idGrupo(tuple.get("idGrupo", Integer.class))
					.cantidadDestinatarios(tuple.get("cantidadDestinatarios", BigInteger.class))
					.fechaCreacion(tuple.get("fechaCreacion", Date.class))
					.build();
		}).collect(Collectors.toList());
		
	}
	

	/**
	 * consulta la Cantidad de Grupos en la base de MYSQL
	 * @author Vladimir Kozisck
	 * @return
	 * @throws BOException
	 */
	
	public Long consultarGruposCount() throws BOException{
		StringBuilder strJPQL = new StringBuilder();

		try {

			strJPQL.append(" SELECT count(ng)");
			strJPQL.append(" FROM 	NotGrupos ng");
			strJPQL.append(" WHERE	 ng.estado='A' ");

			Query query = em.createQuery(strJPQL.toString());

			Long lonCantQuery = (Long) query.getSingleResult();

			return lonCantQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}

	}
	
	public Long existeGrupo(Integer idGrupo) throws BOException{
		StringBuilder strJPQL = new StringBuilder();

		try {

			strJPQL.append(" SELECT count(ng)");
			strJPQL.append(" FROM 	NotGrupos ng");
			strJPQL.append(" WHERE	 ng.estado='A' ");
			strJPQL.append(" AND ng.idGrupo =:idGrupo ");

			Query query = em.createQuery(strJPQL.toString());
			query.setParameter("idGrupo", idGrupo);

			Long lonCantQuery = (Long) query.getSingleResult();

			return lonCantQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}

	}
	
	/** 
	 * Consulta todos los grupos por canal
	 * 
	 * @author Vladimir Kozisck
	 * @return
	 * @throws BOException
	 */
	public List<GruposDTO> consultarGrupos(List<String> lCanales, String idUsuario, String idEmpresa) throws BOException {

	
			StringBuilder strJPQL = new StringBuilder();

			strJPQL.append(" SELECT ng.idGrupo as idGrupo, ");
			strJPQL.append(" 		ng.cantidadDestinatarios as cantidadDestinatarios, ");
			strJPQL.append(" 		ng.descripcion as descripcion, ");
			strJPQL.append(" 		ng.estado as estado, ");
			strJPQL.append(" 		ng.fechaCreacion as fechaCreacion, ");
			strJPQL.append(" 		ng.nombre as nombre, ");
			strJPQL.append(" 		ng.canal as canal, ");
			strJPQL.append(" 		ng.tipo as tipo ");		
			strJPQL.append(" FROM 	NotGrupos ng");
			strJPQL.append(" WHERE	 ng.estado='A'");
			strJPQL.append(" AND 	 ng.canal in (:lCanales) ");
			strJPQL.append(" AND 	 ng.idUsuario =:idUsuario ");
			strJPQL.append(" AND 	 ng.idEmpresa =:idEmpresa ");
			//strJPQL.append(" ORDER BY sp.idParametro DESC ");
			TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);
			query.setParameter("lCanales", lCanales);	
			query.setParameter("idUsuario", idUsuario);			
			query.setParameter("idEmpresa", idEmpresa);			

			
			return query.getResultList().stream().map(tuple -> {
				return GruposDTO.builder().idGrupo(tuple.get("idGrupo", Integer.class))
						.cantidadDestinatarios(tuple.get("cantidadDestinatarios", BigInteger.class))
						.descripcion(tuple.get("descripcion", String.class))
						.estado(tuple.get("estado", String.class))
						.fechaCreacion(FechasHelper.dateToString(tuple.get("fechaCreacion",Date.class), FormatoFecha.YYYY_MM_DD_HH_MM_SS))
						.nombre(tuple.get("nombre",String.class))
						.tipo(tuple.get("tipo",String.class))
						.canal(tuple.get("canal",String.class))
						.build();
			}).collect(Collectors.toList());
		

	}
	
	public List<NotDestinatarios> consultarGruposDestinatarios(Integer idGrupo) {
		StringBuilder strJPQLBase = new StringBuilder();
		strJPQLBase.append("select gd.notDestinatarios ");
		strJPQLBase.append("from  NotGruposDestinatarios gd ");
		strJPQLBase.append("where gd.notGrupos.idGrupo =:idGrupo ");
		strJPQLBase.append("and   gd.estado='A' ");
		
		Query query = em.createQuery(strJPQLBase.toString());
		query.setParameter("idGrupo", idGrupo);
		return query.getResultList();
	}
	
	public List<DestinatariosDTO> allGruposDestinatarios(Integer idGrupo){
		
		
		StringBuilder strJPQLBase = new StringBuilder();
		strJPQLBase.append("select d.correo as correo, ");
		strJPQLBase.append("	   d.telefonoMovil as telefonoMovil,  ");
		strJPQLBase.append("	   d.primerNombre as primerNombre,  ");
		strJPQLBase.append("	   d.primerApellido as primerApellido,  ");
		strJPQLBase.append("	   d.fechaNacimiento as fechaNacimiento,  ");
		strJPQLBase.append("	   d.fechaCreacion as fechaCreacion  ");
		strJPQLBase.append("from  NotGruposDestinatarios gd ");
		strJPQLBase.append("join  gd.notDestinatarios d ");
		strJPQLBase.append("join  gd.notGrupos g ");
		strJPQLBase.append("where g.idGrupo =:idGrupo ");
		//strJPQLBase.append("and   g.idEmpresa=: idEmpresa ");
		//strJPQLBase.append("and   g.tipo=: tipo ");
		
		TypedQuery<Tuple> query = em.createQuery(strJPQLBase.toString(), Tuple.class);
		query.setParameter("idGrupo", idGrupo);
		
		

		//return query.getResultList();
		
		return query.getResultList().stream().map(tuple -> {
			return DestinatariosDTO.builder()
					.correo(tuple.get("correo", String.class))
					.telefonoMovil(tuple.get("telefonoMovil", String.class))
					.primerNombre(tuple.get("primerNombre", String.class))
					.primerApellido(tuple.get("primerApellido", String.class))
					.fechaNacimiento(tuple.get("fechaNacimiento",String.class))
					.fechaCreacion(tuple.get("fechaCreacion",String.class))
					.build();
		}).collect(Collectors.toList());
		
	}
	
	
	public List<NotDestinatarios> consultarGruposDestinatarios_v2(Integer idGrupo, List<CamposFiltradosDTO> objCriteriosFiltrado) {
		StringBuilder strJPQLBase = new StringBuilder();
		
		String criterio = null;
		List<NotDestinatarios> objLstNotDestinatarios = null;
		
		strJPQLBase.append(" select gd.notDestinatarios ");
		strJPQLBase.append(" from  NotGruposDestinatarios gd ");
		strJPQLBase.append(" where gd.notGrupos.idGrupo =:idGrupo ");
		strJPQLBase.append(" and   gd.notGrupos.estado='A' ");
		
		for (CamposFiltradosDTO camposFiltradosDTO : objCriteriosFiltrado) {
			switch (camposFiltradosDTO.getCriterio()) {
			case "1":
				criterio = " > '"+camposFiltradosDTO.getValorCriterio()+"'";
				break;
				
			case "2":
				criterio = " < '"+camposFiltradosDTO.getValorCriterio()+"'";
				break;
				
			case "3":
				criterio = " = '"+camposFiltradosDTO.getValorCriterio()+"'";
				break;
				
			case "4":
				criterio = " >= ";
				break;
				
			case "5":
				criterio = " <= ";
				break;
				
			case "6":
				criterio = " like '"+camposFiltradosDTO.getValorCriterio()+"%' ";
				break;
				
			case "7":
				criterio = " like '%"+camposFiltradosDTO.getValorCriterio()+"' ";
				break;
				
			case "8":
				criterio = " like '%"+camposFiltradosDTO.getValorCriterio()+"%' ";
				break;

			default:
				break;
			}
			
			strJPQLBase.append(" and   gd.notDestinatarios."+camposFiltradosDTO.getCampo()+" " + criterio);		
			
		}
		
		Query query = em.createQuery(strJPQLBase.toString());
		query.setParameter("idGrupo", idGrupo);
		return objLstNotDestinatarios = query.getResultList();
	}
	
	public NotGrupos findBy(Integer intIdGrupo, String idUsuario, String idEmpresa) throws BOException{
		try {	
			return em.createQuery(
						"SELECT usd \n" +
						"  FROM NotGrupos usd \n" +
						"WHERE usd.idGrupo =: idGrupo \n"+
						"AND usd.idUsuario=: idUsuario \n"+
						"AND usd.idEmpresa =: idEmpresa \n"+
						"AND usd.estado='A' ",NotGrupos.class)
						.setParameter("idGrupo",intIdGrupo)
						.setParameter("idUsuario",idUsuario)
						.setParameter("idEmpresa",idEmpresa)
						.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
}