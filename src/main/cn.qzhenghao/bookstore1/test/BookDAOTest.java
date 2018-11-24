package bookstore1.test;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

import bookstore1.dao.BookDAO;
import bookstore1.dao.impl.BookDAOImpl;
import bookstore1.db.JDBCUtils;
import bookstore1.domain.Book;
import bookstore1.domain.ShoppingCartItem;
import bookstore1.web.ConnectionContext;
import bookstore1.web.CriteriaBook;
import bookstore1.web.Page;
import org.junit.Test;

public class BookDAOTest {

	private BookDAO bookDAO = new BookDAOImpl();
	
	@Test
	public void testGetBook() {
		Connection connection = JDBCUtils.getConnection();
		ConnectionContext.getInstance().bind(connection);
		
		Book book = bookDAO.getBook(5);
		System.out.println(book); 
	}

	@Test
	public void testGetPage() {
		CriteriaBook cb = new CriteriaBook(50, 60, 90);
		Page<Book> page = bookDAO.getPage(cb);
		
		System.out.println("pageNo: " + page.getPageNo());
		System.out.println("totalPageNumber: " + page.getTotalPageNumber());
		System.out.println("list: " + page.getList());
		System.out.println("prevPage: " + page.getPrevPage());
		System.out.println("nextPage: " + page.getNextPage()); 
	}

	@Test
	public void testGetStoreNumber() {
		int storeNumber = bookDAO.getStoreNumber(5);
		System.out.println(storeNumber); 
	}

	@Test
	public void testBatchUpdateStoreNumberAndSalesAmount(){
		Collection<ShoppingCartItem> items = new ArrayList<>();
		
		Book book = bookDAO.getBook(1);
		ShoppingCartItem sci = new ShoppingCartItem(book);
		sci.setQuantity(10);
		items.add(sci);
		
		book = bookDAO.getBook(2);
		sci = new ShoppingCartItem(book);
		sci.setQuantity(11);
		items.add(sci);
		
		book = bookDAO.getBook(3);
		sci = new ShoppingCartItem(book);
		sci.setQuantity(12);
		items.add(sci);
		
		book = bookDAO.getBook(4);
		sci = new ShoppingCartItem(book);
		sci.setQuantity(14);
		items.add(sci);
		
		bookDAO.batchUpdateStoreNumberAndSalesAmount(items);
	}
}
