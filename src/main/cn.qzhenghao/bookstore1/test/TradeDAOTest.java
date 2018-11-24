package bookstore1.test;

import static org.junit.Assert.fail;

import java.sql.Date;
import java.util.Set;

import org.junit.Test;

import bookstore1.dao.TradeDAO;
import bookstore1.dao.impl.TradeDAOImpl;
import bookstore1.domain.Trade;

public class TradeDAOTest {

	private TradeDAO tradeDAO = new TradeDAOImpl();
	
	@Test
	public void testInsertTrade() {
		Trade trade = new Trade();
		trade.setUserId(3);
		trade.setTradeTime(new Date(new java.util.Date().getTime()));
		
		tradeDAO.insert(trade);
	}

	@Test
	public void testGetTradesWithUserId() {
		Set<Trade> trades = tradeDAO.getTradesWithUserId(2);
		System.out.println(trades);
	}

}
