package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.BrandCategoryDao;
import com.increff.pos.pojo.BrandCategoryPojo;

@Service
public class BrandCategoryService {

	@Autowired
	private BrandCategoryDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(BrandCategoryPojo p) throws ApiException {
		if(dao.checkBrandCatDuplicateExists(p.getBrand(),p.getCategory())){
			throw new ApiException("Brand: " + p.getBrand() + " in the category: " + p.getCategory() + " already exists.");
		}
		dao.insert(p);
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandCategoryPojo get(Integer id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<BrandCategoryPojo> getAll() {
		return dao.selectAll();
	}
	@Transactional(rollbackOn  = ApiException.class)
	public void update(Integer id, BrandCategoryPojo p) throws ApiException {
		if(dao.checkBrandCatDuplicateExists(p.getBrand(),p.getCategory())){
			throw new ApiException("Brand: " + p.getBrand() + " in the category: " + p.getCategory() + " already exists.");
		}
		BrandCategoryPojo ex = getCheck(id);
		ex.setCategory(p.getCategory());
		ex.setBrand(p.getBrand());
		dao.update(ex);
	}
	@Transactional
	public BrandCategoryPojo getCheck(Integer id) throws ApiException {
		BrandCategoryPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Brand with given ID does not exist, id: " + id);
		}
		return p;
	}
	@Transactional
	public BrandCategoryPojo getCheckWithBrandCategory(String brand, String category) throws ApiException{
		BrandCategoryPojo p = dao.select(brand,category);
		if (p == null) {
			throw new ApiException("Brand with brand: " + brand + " and category: " + category+" does not exist!");
		}
		return p;
	}

}
