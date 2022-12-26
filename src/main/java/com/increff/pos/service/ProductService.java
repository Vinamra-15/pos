package com.increff.pos.service;

import com.increff.pos.dao.ProductDao;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;


	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo p) throws ApiException {

		if(dao.checkProductDuplicateExists(p.getBarcode())){
			throw new ApiException("Product with barcode: " + p.getBarcode()+ " already exists.");
		}
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<ProductPojo> getAll() {
		return dao.selectAll();
	}

	public ProductPojo getByBarcode(String barcode){
		return dao.selectByBarcode(barcode);
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, ProductPojo p) throws ApiException {


		if(dao.checkProductDuplicateExists(id,p.getBarcode())){
			throw new ApiException("Product with barcode: " + p.getBarcode()+ " already exists.");
		}


		ProductPojo ex = getCheck(id);

		ex.setName(p.getName());

		ex.setBarcode(p.getBarcode());

		ex.setMrp(p.getMrp());

		ex.setBrandId(p.getBrandId());


		dao.update(ex);
	}

	@Transactional
	public ProductPojo getCheck(int id) throws ApiException {
		ProductPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Product with given ID does not exist, id: " + id);
		}
		return p;
	}


}
