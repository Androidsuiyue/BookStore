package bookstore1.test;

import static org.junit.Assert.*;

import org.junit.Test;

import bookstore1.dao.AccountDAO;
import bookstore1.dao.impl.AccountDAOIml;
import bookstore1.domain.Account;

public class AccountDAOTest {

	AccountDAO accountDAO = new AccountDAOIml();
	
	@Test
	public void testGet() {
		Account account = accountDAO.get(1);
		System.out.println(account.getBalance()); 
	}

	@Test
	public void testUpdateBalance() {
		accountDAO.updateBalance(1, 50);
	}

}
