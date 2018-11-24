package bookstore1.dao;

import bookstore1.domain.User;

public interface UserDAO {

	/**
	 * �����û�����ȡ User ����
	 * @param username
	 * @return
	 */
	public abstract User getUser(String username);

}

