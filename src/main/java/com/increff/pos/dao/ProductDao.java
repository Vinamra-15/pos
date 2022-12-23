package com.increff.pos.dao;

import com.increff.pos.pojo.ProductPojo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class ProductDao extends AbstractDao {
    private static String delete_id = "delete from ProductPojo p where id=:id";
    private static String select_id = "select p from ProductPojo p where id=:id";
    private static String select_all = "select p from ProductPojo p";

    private static String check_Product_duplicate = "select p from ProductPojo p where barcode=:barcode";

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void insert(ProductPojo p) {
        em.persist(p);
    }

    public int delete(int id) {
        Query query = em.createQuery(delete_id);
        query.setParameter("id", id);
        return query.executeUpdate();
    }


    public ProductPojo select(int id) {
        TypedQuery<ProductPojo> query = getQuery(select_id, ProductPojo.class);
        query.setParameter("id", id);
        return getSingle(query);
    }

    public List<ProductPojo> selectAll() {
        TypedQuery<ProductPojo> query = getQuery(select_all, ProductPojo.class);
        return query.getResultList();
    }

    public void update(ProductPojo p) {

    }

    public boolean checkProductDuplicateExists(int id, String barcode){
        TypedQuery<ProductPojo> query = getQuery(check_Product_duplicate,ProductPojo.class);
        query.setParameter("barcode",barcode);
        return id != getSingle(query).getId();
    }

    public boolean checkProductDuplicateExists(String barcode){
        TypedQuery<ProductPojo> query = getQuery(check_Product_duplicate,ProductPojo.class);
        query.setParameter("barcode",barcode);

        if(getSingle(query)!=null)
            return true;
        return false;
    }

}
