package sk.tuke.gamestudio.service.jpa;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class RatingServiceJPA implements RatingService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) throws RatingException {
        System.out.println("SETTING RATING");
        System.out.println(rating.getRatedOn());
        List<Rating> ratings = entityManager.createQuery("select r from Rating r where player = :name")
                .setParameter("name", rating.getPlayer()).getResultList();
        int count = ratings.size();
        if(count == 0){
            entityManager.persist(rating);
        }
        else{
//            String update = String.format("update rating set rating = %s, ratedOn = %s where player = %s",
//                    rating.getRating(), rating.getRatedOn(), rating.getPlayer());
//            entityManager.createNativeQuery(update).executeUpdate();
            entityManager.createQuery("update Rating r set r.rating = :rating, r.ratedOn = :ratedOn where r.player = :player")
                    .setParameter("rating", rating.getRating()).setParameter("ratedOn", rating.getRatedOn())
                    .setParameter("player", rating.getPlayer()).executeUpdate();
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Object result = entityManager.createQuery("select avg(r.rating) from Rating r").getSingleResult();
        if(result == null){
            return 0;
        }
        return (int) Math.round((double) result);
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        List <Integer> list = entityManager.createQuery("select r.rating from Rating r where player = :name and game = :game")
                .setParameter("name", player).setParameter("game", game).getResultList();
        if(list.size() == 0){
            return 0;
        }
        else{
            return list.get(0);
        }
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNativeQuery("delete from rating").executeUpdate();
    }
}
