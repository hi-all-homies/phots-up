package main.dao.comment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import main.model.entities.Comment;

@Service
public class CommentDaoImpl implements CommentDao{
	private final CommentRepo commentRepo;

	public CommentDaoImpl(CommentRepo commentRepo) {
		this.commentRepo = commentRepo;
	}

	@Override
	public Comment saveComment(Comment comment) {
		return this.commentRepo.save(comment);
	}

	@Override
	@Transactional
	public int updateComment(Comment comment) {
		return this.commentRepo.updateById(comment.getContent(), comment.getId());
	}

	@Override
	public void deleteComment(Long commentId) {
		this.commentRepo.deleteById(commentId);
	}
	
	
	
}
