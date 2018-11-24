package bookstore1.test;

import bookstore1.domain.User;
import org.junit.Test;

import bookstore1.dao.UserDAO;
import bookstore1.dao.impl.UserDAOImpl;

public class UserDAOTest {

	private UserDAO userDAO = new UserDAOImpl();
	
	@Test
	public void testGetUser() {
		User user = userDAO.getUser("AAA");
		System.out.println(user); 
	}

}
