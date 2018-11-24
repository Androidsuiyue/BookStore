package bookstore1.service;

import bookstore1.dao.AccountDAO;
import bookstore1.dao.impl.AccountDAOIml;
import bookstore1.domain.Account;

public class AccountService {
	
	private AccountDAO accountDAO = new AccountDAOIml();
	
	public Account getAccount(int accountId){
		return accountDAO.get(accountId);
	}
	
}
