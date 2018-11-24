package bookstore1.dao.impl;

import bookstore1.domain.User;
import bookstore1.dao.UserDAO;

public class UserDAOImpl extends BaseDAO<User> implements UserDAO {

	@Override
	public User getUser(String username) {
		String sql = "SELECT userId, username, accountId " +
				"FROM userinfo WHERE username = ?";
		return query(sql, username); 
	}

}
