package sk.tuke.gamestudio.service.file;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentException;
import sk.tuke.gamestudio.service.CommentService;


import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


public class CommentServiceFile implements CommentService {

    private static final String COMMENT_FILE = "comment.bin";
    private List<Comment> comments = new ArrayList<>();

    public CommentServiceFile() {
        if (new File(COMMENT_FILE).exists()) {
            load();
        } else {
            save();
        }
    }

    @Override
    public void addComment(Comment comment) throws CommentException {
        comments.add(comment);
        save();
    }

    @Override
    public List<Comment> getComments(String game) throws CommentException {
        load();
        return comments.stream().sorted(Comparator.comparing(Comment::getCommentedOn).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void reset() throws CommentException {
        comments = new ArrayList<>();
        save();
    }

    private void save() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(COMMENT_FILE))) {
            oos.writeObject(comments);
        } catch (IOException e) {
            throw new GamestudioException("Error saving comment ", e);
        }
    }

    private void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(COMMENT_FILE))) {
            comments = (List<Comment>) ois.readObject();
        } catch (Exception e) {
            throw new GamestudioException("Error loading comments ", e);
        }
    }

}