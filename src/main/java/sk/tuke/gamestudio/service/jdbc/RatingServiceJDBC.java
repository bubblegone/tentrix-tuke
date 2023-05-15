package sk.tuke.gamestudio.service.jdbc;

import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingException;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreException;

import java.sql.*;

public class RatingServiceJDBC implements RatingService {
    public static final String URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String USER = "postgres";
    public static final String PASSWORD = "postgres";
    public static final String GETRATING = "SELECT rating FROM rating WHERE game = ? and player = ?";
    public static final String GETAVARAGERATING = "SELECT avg(rating) FROM rating WHERE game = ?";
    public static final String CHECKUPDATE = "SELECT count(*) FROM rating WHERE player = ?";
    public static final String INSERT = "INSERT INTO rating (game, player, rating, ratedOn) VALUES (?, ?, ?, ?)";
    public static final String UPDATE = "UPDATE rating set rating = ?, ratedOn = ? where player = ? AND game = ?";
    public static final String DELETE = "DELETE FROM rating";

    @Override
    public void setRating(Rating rating) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statementCHECKUPDATE = connection.prepareStatement(CHECKUPDATE);
             PreparedStatement statementUPDATE = connection.prepareStatement(UPDATE);
             PreparedStatement statementINSERT = connection.prepareStatement(INSERT)
        ) {
            statementCHECKUPDATE.setString(1, rating.getPlayer());
            try (ResultSet rs = statementCHECKUPDATE.executeQuery()) {
                rs.next();
                if (rs.getInt(1) == 0) {
                    statementINSERT.setString(1, rating.getGame());
                    statementINSERT.setString(2, rating.getPlayer());
                    statementINSERT.setInt(3, rating.getRating());
                    statementINSERT.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
                    statementINSERT.executeUpdate();
                } else {
                    statementUPDATE.setInt(1, rating.getRating());
                    statementUPDATE.setTimestamp(2, new Timestamp(rating.getRatedOn().getTime()));
                    statementUPDATE.setString(3, rating.getPlayer());
                    statementUPDATE.setString(4, rating.getGame());
                    statementUPDATE.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem inserting rating", e);
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(GETAVARAGERATING)
        ) {
            statement.setString(1, game);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting rating by player", e);
        }
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(GETRATING)
        ) {
            statement.setString(1, game);
            statement.setString(2, player);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            throw new ScoreException("Problem selecting rating by player", e);
        }
    }

    @Override
    public void reset() throws RatingException {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE);
        } catch (SQLException e) {
            throw new ScoreException("Problem deleting rating", e);
        }
    }
}
