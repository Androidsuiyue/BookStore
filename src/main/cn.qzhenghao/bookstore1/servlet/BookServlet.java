package bookstore1.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bookstore1.domain.Book;
import bookstore1.domain.ShoppingCart;
import bookstore1.domain.ShoppingCartItem;
import bookstore1.domain.User;
import bookstore1.service.AccountService;
import bookstore1.service.BookService;
import bookstore1.service.UserService;
import bookstore1.web.CriteriaBook;
import bookstore1.web.Page;
import bookstore1.domain.Account;
import bookstore1.web.BookStoreWebUtils;
import com.google.gson.Gson;

public class BookServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	private BookService bookService = new BookService();
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String methodName = request.getParameter("method");
		
		try {
			Method method = getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
			method.setAccessible(true);
			method.invoke(this, request, response);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private UserService userService = new UserService();
	
	protected void cash(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. ����֤: ��֤�����ֵ�Ƿ���ϻ����Ĺ淶: �Ƿ�Ϊ��, �Ƿ����תΪ int ����, �Ƿ���һ�� email. ����Ҫ���в�ѯ
		//���ݿ������κε�ҵ�񷽷�.
		String username = request.getParameter("username");
		String accountId = request.getParameter("accountId");
		
		StringBuffer errors = validateFormField(username, accountId);
		
		//����֤ͨ���� 
		if(errors.toString().equals("")){
			errors = validateUser(username, accountId);
			
			//�û������˺���֤ͨ��
			if(errors.toString().equals("")){
				errors = validateBookStoreNumber(request);
				
				//�����֤ͨ��
				if(errors.toString().equals("")){
					errors = validateBalance(request, accountId);
				}
			}
		}
		
		if(!errors.toString().equals("")){
			request.setAttribute("errors", errors);
			request.getRequestDispatcher("/WEB-INF/pages/cash.jsp").forward(request, response);
			return;
		}
		
		//��֤ͨ��ִ�о�����߼�����
		bookService.cash(BookStoreWebUtils.getShoppingCart(request), username, accountId); 
		response.sendRedirect(request.getContextPath() + "/success.jsp");
	}
	
	private AccountService accountService = new AccountService();
	
	//��֤����Ƿ����
	public StringBuffer validateBalance(HttpServletRequest request, String accountId){
		
		StringBuffer errors = new StringBuffer("");
		ShoppingCart cart = BookStoreWebUtils.getShoppingCart(request);
		
		Account account = accountService.getAccount(Integer.parseInt(accountId));
		if(cart.getTotalMoney() > account.getBalance()){
			errors.append("����!");
		}
		
		return errors;
	}
	
	//��֤����Ƿ����
	public StringBuffer validateBookStoreNumber(HttpServletRequest request){
		
		StringBuffer errors = new StringBuffer("");
		ShoppingCart cart = BookStoreWebUtils.getShoppingCart(request);
		
		for(ShoppingCartItem sci: cart.getItems()){
			int quantity = sci.getQuantity();
			int storeNumber = bookService.getBook(sci.getBook().getId()).getStoreNumber();
			
			if(quantity > storeNumber){
				errors.append(sci.getBook().getTitle() + "��治��<br>");
			}
		}
		
		return errors;
	}
	
	//��֤�û������˺��Ƿ�ƥ��
	public StringBuffer validateUser(String username, String accountId){
		boolean flag = false;
		User user = userService.getUserByUserName(username);
		if(user != null){
			int accountId2 = user.getAccountId();
			if(accountId.trim().equals("" + accountId2)){
				flag = true;
			}
		}
		
		StringBuffer errors2 = new StringBuffer("");
		if(!flag){
			errors2.append("�û������˺Ų�ƥ��");
		}
		
		return errors2;
	}
	
	//��֤�����Ƿ���ϻ����Ĺ���: �Ƿ�Ϊ��. 
	public StringBuffer validateFormField(String username, String accountId){
		StringBuffer errors = new StringBuffer("");
		
		if(username == null || username.trim().equals("")){
			errors.append("�û�������Ϊ��<br>");
		}
		
		if(accountId == null || accountId.trim().equals("")){
			errors.append("�˺Ų���Ϊ��");			
		}
		
		return errors;
	}

	protected void updateItemQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//4. �� updateItemQuantity ������, ��ȡ quanity, id, �ٻ�ȡ���ﳵ����, ���� service �ķ������޸�
		String idStr = request.getParameter("id");
		String quantityStr = request.getParameter("quantity");
		
		ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
		
		int id = -1;
		int quantity = -1;
		
		try {
			id = Integer.parseInt(idStr);
			quantity = Integer.parseInt(quantityStr);
		} catch (Exception e) {}
		
		if(id > 0 && quantity > 0)
			bookService.updateItemQuantity(sc, id, quantity);
		
		//5. ���� JSON ����: bookNumber:xx, totalMoney
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("bookNumber", sc.getBookNumber());
		result.put("totalMoney", sc.getTotalMoney());
		
		Gson gson = new Gson();
		String jsonStr = gson.toJson(result);
		response.setContentType("text/javascript");
		response.getWriter().print(jsonStr);
	}

	protected void clear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
		bookService.clearShoppingCart(sc);
		
		request.getRequestDispatcher("/WEB-INF/pages/emptycart.jsp").forward(request, response);
	}
	
	protected void remove(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idStr = request.getParameter("id");
		
		int id = -1;
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {}
		
		ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
		bookService.removeItemFromShoppingCart(sc, id);
		
		if(sc.isEmpty()){
			request.getRequestDispatcher("/WEB-INF/pages/emptycart.jsp").forward(request, response);
			return;
		}
		
		request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
	}
	
	protected void forwardPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String page = request.getParameter("page");
		request.getRequestDispatcher("/WEB-INF/pages/" + page + ".jsp").forward(request, response);
	}

	protected void addToCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//1. ��ȡ��Ʒ�� id
		String idStr = request.getParameter("id");
		int id = -1;
		boolean flag = false;
		
		try {
			id = Integer.parseInt(idStr);
		} catch (Exception e) {}
		
		if(id > 0){
			//2. ��ȡ���ﳵ����
			ShoppingCart sc = BookStoreWebUtils.getShoppingCart(request);
			
			//3. ���� BookService �� addToCart() ��������Ʒ�ŵ����ﳵ��
			flag = bookService.addToCart(id, sc);
		}
		
		if(flag){
			//4. ֱ�ӵ��� getBooks() ����. 
			getBooks(request, response);
			return;
		}
		
		response.sendRedirect(request.getContextPath() + "/error-1.jsp");
	}

	protected void getBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String idStr = request.getParameter("id");
		int id = -1;

		Book book = null;
		
		try {
			id = Integer.parseInt(idStr);
		} catch (NumberFormatException e) {}
		
		if(id > 0)
			book = bookService.getBook(id);
		
		if(book == null){
			response.sendRedirect(request.getContextPath() + "/error-1.jsp");
			return;
		}
		
		request.setAttribute("book", book);
		request.getRequestDispatcher("/WEB-INF/pages/book.jsp").forward(request, response);
	}
	
	protected void getBooks(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageNoStr = request.getParameter("pageNo");
		String minPriceStr = request.getParameter("minPrice");
		String maxPriceStr = request.getParameter("maxPrice");
		
		int pageNo = 1;
		int minPrice= 0;
		int maxPrice = Integer.MAX_VALUE;
		
		try {
			pageNo = Integer.parseInt(pageNoStr);
		} catch (NumberFormatException e) {}
		
		try {
			minPrice = Integer.parseInt(minPriceStr);
		} catch (NumberFormatException e) {}
		
		try {
			maxPrice = Integer.parseInt(maxPriceStr);
		} catch (NumberFormatException e) {}
		
		CriteriaBook criteriaBook = new CriteriaBook(minPrice, maxPrice, pageNo);
		Page<Book> page = bookService.getPage(criteriaBook);
		
		request.setAttribute("bookpage", page);
		
		request.getRequestDispatcher("/WEB-INF/pages/books.jsp").forward(request, response);
	}
	
	

}
