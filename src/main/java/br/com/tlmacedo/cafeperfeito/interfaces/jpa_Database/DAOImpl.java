package br.com.tlmacedo.cafeperfeito.interfaces.jpa_Database;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

public abstract class DAOImpl<T, I extends Serializable> implements DAO<T, I> {

    private ConnectionFactory conexao;

    @Override
    public T persiste(T entity) {

        T saved = null;
        getEntityManager().getTransaction().begin();
        saved = getEntityManager().merge(entity);
        getEntityManager().getTransaction().commit();
//        conexao.closeEntityManeger();
        return saved;
    }

    @Override
    public void remove(T entity) {
        getEntityManager().getTransaction().begin();
        getEntityManager().remove(entity);
        getEntityManager().getTransaction().commit();
//        conexao.closeEntityManeger();
    }

    @Override
    public T getById(Class<T> classe, I pk) {

        try {
            return getEntityManager().find(classe, pk);
        } catch (NoResultException e) {
            return null;
//        } finally {
//            conexao.closeEntityManeger();
        }
    }

//    @Override
//    public List<T> getMunicipiosUf(Class<T> classe, Long ufId) {
//        Query select = getEntityManager().createQuery("from " + classe.getSimpleName() + " where uf_id = :uf_id order by capital DESC, descricao");
//        List<T> list = select.setParameter("uf_id", ufId).getResultList();
////        conexao.closeEntityManeger();
//        return list;
//    }


//    @Override
//    public T getByPersonalizado(Class<T> classe, String busca) {
//        Query select;
//        switch (classe.getSimpleName()) {
//            case "TelefoneOperadora":
//                select = getEntityManager().createQuery("from " + classe.getSimpleName() + " where codWsPortabilidade = :busca ");
//                select.setParameter("codWsPortabilidade", busca);
//                break;
//            default:
//                select = getEntityManager().createQuery("from " + classe.getSimpleName() );
////                select.setParameter(campo, busca);
//                break;
//        }
////        Query select = getEntityManager().createQuery("from " + classe.getSimpleName() + " where " + campo + " = :busca");
//        try {
//            return (T) select.getSingleResult();
//        } catch (
//                Exception ex) {
//            return null;
//        }
//    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAll(Class<T> classe, String orderBy, String campo, String operador, String busca) {
        Query select;
        String sql = String.format("from %s%s%s",
                classe.getSimpleName(),
                (campo != null && operador != null && busca != null)
                        ? String.format(" where %s %s %s", campo, operador, busca)
                        : "",
                orderBy != null
                        ? String.format(" order by %s", orderBy)
                        : ""
        );
//        if (orderBy == null)
//            select = getEntityManager().createQuery("from " + classe.getSimpleName());
//        else
//            select = getEntityManager().createQuery("from " + classe.getSimpleName() + "  order by " + orderBy);
//        System.out.printf("sql_%s: [%s]\n", classe, sql);
        select = getEntityManager().createQuery(sql);
        List<T> list = select.getResultList();
//        conexao.closeEntityManeger();
        return list;
    }

    @Override
    public EntityManager getEntityManager() {
        if (conexao == null) {
            conexao = new ConnectionFactory();
        }
        return conexao.getEntityManager();
    }

}