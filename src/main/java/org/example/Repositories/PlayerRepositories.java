package org.example.Repositories;

import org.example.Entity.Player;
import org.example.Entity.PlayerDB;
import org.example.Services.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayerRepositories {
    private SessionFactory sf = HibernateUtil.getSessionFactory();

    public void incScore(String name) {
        var optPlayer = findPlayerByName(name);
        if (optPlayer.isPresent()) {
            var playerDB = optPlayer.get();
            playerDB.setCountWin(playerDB.getCountWin() + 1);
            updatePlayer(playerDB);
        } else {
            var playerDB = new PlayerDB(name, 1);
            savePlayer(playerDB);
        }
    }

    public List<PlayerDB> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "FROM PlayerDB "; // User - это имя вашего Entity класса
            Query<PlayerDB> query = session.createQuery(hql, PlayerDB.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }

    private Optional<PlayerDB> findPlayerByName(String name) {
        try (Session session = sf.openSession()) {
            Query<PlayerDB> query = session.createQuery("from PlayerDB where name = :name", PlayerDB.class);
            query.setParameter("name", name);
            return Optional.ofNullable(query.uniqueResult());
        }
    }

    private void savePlayer(PlayerDB player) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            session.persist(player);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при сохранении игрока", e);
        }
    }

    private void updatePlayer(PlayerDB player) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();
            session.update(player);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при сохранении игрока", e);
        }
    }
}
