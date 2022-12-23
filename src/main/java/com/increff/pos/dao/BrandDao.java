package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

	private static String delete_id = "delete from BrandPojo p where id=:id";
	private static String select_id = "select p from BrandPojo p where id=:id";
	private static String select_all = "select p from BrandPojo p";

	private static String select_name_category = "select p from BrandPojo p where name=:name and category=:category";



	private static String check_brand_cat_duplicate = "select p from BrandPojo p where name=:name and category=:category";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandPojo p) {
		em.persist(p);
	}

//	public int delete(int id) {
//		Query query = em.createQuery(delete_id);
//		query.setParameter("id", id);
//		return query.executeUpdate();
//	}


	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
		return query.getResultList();
	}

	public void update(BrandPojo p) {
	}

	public BrandPojo select(String name, String category) {
		TypedQuery<BrandPojo> query = getQuery(select_name_category, BrandPojo.class);
		query.setParameter("name", name);
		query.setParameter("category", category);

		return getSingle(query);
	}

	public boolean checkBrandCatDuplicateExists(String name, String category){
		TypedQuery<BrandPojo> query = getQuery(check_brand_cat_duplicate,BrandPojo.class);
		query.setParameter("name",name);
		query.setParameter("category",category);


		if(getSingle(query)==null)
			return false;
		return true;
	}



}