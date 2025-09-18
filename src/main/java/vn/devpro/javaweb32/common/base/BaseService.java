package vn.devpro.javaweb32.common.base;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

@Service
public abstract class BaseService <Model extends BaseEntity> {

    @PersistenceContext
    EntityManager entityManager;

    public abstract Class<Model> clazz();

    //Lay mot ban ghi theo id
    public Model getById(Long id) {
        return entityManager.find(clazz(), id);
    }

    //Lay tat ca cac ban ghi trong mot table
    @SuppressWarnings("unchecked")
    public List<Model> findAll() {
        Table table = clazz().getAnnotation(Table.class);
        return (List<Model>) entityManager.createNativeQuery(
                "SELECT * FROM " + table.name(),
                clazz()).getResultList();
    }

    //Them moi hoac sua mot ban ghi
    @Transactional
    public Model saveOrUpdate(Model entity) {
        if (entity.getId() == null || entity.getId() <= 0) { //Add new entity
            entityManager.persist(entity);
            return entity;
        }
        else { //update entity
            return entityManager.merge(entity);
        }
    }

    //Xoa 1 ban ghi theo entity
    @Transactional
    public void delete(Model entity) {
        entityManager.remove(entity);
    }
    //Delete theo id
    @Transactional
    public void deleteById(Long id) {
        Model entity = this.getById(id);
        delete(entity);
    }

    // Execute native SQL and return list of entities
    @SuppressWarnings("unchecked")
    protected List<Model> executeNativeSql(String sql) {
        return (List<Model>) entityManager.createNativeQuery(sql, clazz()).getResultList();
    }
}