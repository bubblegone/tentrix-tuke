package sk.tuke.gamestudio.service.file;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RatingServiceFile implements RatingService {

    private static final String RATING_FILE = "rating.bin";
    private List<Rating> ratings = new ArrayList<>();

    public RatingServiceFile() {
        if (new File(RATING_FILE).exists()) {
            load();
        } else {
            save();
        }
    }

    @Override
    public void setRating(Rating rating) throws RatingException {
        List<Rating> ratingList = ratings.stream()
                .filter(s -> s.getPlayer().equals(rating.getPlayer()) && s.getGame().equals(rating.getGame()))
                .collect(Collectors.toList());
        if (!ratingList.isEmpty()) {
            this.ratings.remove(ratingList.get(0));
        }
        this.ratings.add(rating);
        save();
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        List<Rating> ratingList = ratings.stream().filter(s -> s.getGame().equals(game)).collect(Collectors.toList());
        int ratingsSum = 0;
        for (Rating rating : ratingList) {
            ratingsSum += rating.getRating();
        }
        return ratingsSum / ratingList.size();
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        List<Rating> ratingList = ratings.stream().filter(s -> s.getPlayer().equals(player) && s.getGame().equals(game))
                .collect(Collectors.toList());
        if (!ratingList.isEmpty()) {
            return ratingList.get(0).getRating();
        }
        return 0;
    }

    @Override
    public void reset() throws RatingException {
        ratings = new ArrayList<>();
        save();
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(RATING_FILE))) {
            oos.writeObject(ratings);
        } catch (IOException e) {
            throw new GamestudioException("Error saving comment ", e);
        }
    }

    private void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(RATING_FILE))) {
            ratings = (List<Rating>) ois.readObject();
        } catch (Exception e) {
            throw new GamestudioException("Error loading comments ", e);
        }
    }
}
