package com.increff.pos.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.BrandCategoryPojo;

@Repository
public class BrandCategoryDao extends AbstractDao {

	private static String delete_id = "delete from BrandCategoryPojo p where id=:id";
	private static String select_id = "select p from BrandCategoryPojo p where id=:id";
	private static String select_all = "select p from BrandCategoryPojo p";
	private static String select_brand_category = "select p from BrandCategoryPojo p where brand=:brand and category=:category";
	private static String check_brand_cat_duplicate = "select p from BrandCategoryPojo p where brand=:brand and category=:category";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandCategoryPojo p) {
		em.persist(p);
	}

	public BrandCategoryPojo select(Integer id) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_id, BrandCategoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<BrandCategoryPojo> selectAll() {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_all, BrandCategoryPojo.class);
		return query.getResultList();
	}

	public void update(BrandCategoryPojo p) {
	}

	public BrandCategoryPojo select(String brand, String category) {
		TypedQuery<BrandCategoryPojo> query = getQuery(select_brand_category, BrandCategoryPojo.class);
		query.setParameter("brand", brand);
		query.setParameter("category", category);

		return getSingle(query);
	}

	public boolean checkBrandCatDuplicateExists(String brand, String category){
		TypedQuery<BrandCategoryPojo> query = getQuery(check_brand_cat_duplicate, BrandCategoryPojo.class);
		query.setParameter("brand",brand);
		query.setParameter("category",category);


		if(getSingle(query)==null)
			return false;
		return true;
	}



}
