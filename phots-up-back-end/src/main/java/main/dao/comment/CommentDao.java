package main.dao.comment;

import main.model.entities.Comment;

public interface CommentDao {
	
	public Comment saveComment(Comment comment);
	
	public int updateComment(Comment comment);
	
	public void deleteComment(Long commentId);
}
