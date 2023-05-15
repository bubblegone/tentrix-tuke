package sk.tuke.gamestudio.server.auth;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class AuthServiceJPA {
    @PersistenceContext
    private EntityManager entityManager;

    public boolean createUser(Player user) {
        List<Player> list = entityManager.createQuery("select u from Player u where u.name = :name")
                .setParameter("name", user.getName()).getResultList();
        if (list.size() == 0) {
            entityManager.persist(user);
            return true;
        } else {
            return false;
        }
    }

    public boolean verifyUser(Player user) {
        List<Player> list = entityManager.createQuery("select u from Player u where u.name = :name")
                .setParameter("name", user.getName()).getResultList();
        if (list.size() == 0) {
            return false;
        } else if (list.size() > 1) {
            System.out.println("SERVER ERROR");
            for (Player player :
                    list) {
                System.out.printf("%s -> %s\n", player.getName(), player.getPassword());
            }
            return false;
        } else {
            return user.getPassword().equals(list.get(0).getPassword());
        }
    }
}
