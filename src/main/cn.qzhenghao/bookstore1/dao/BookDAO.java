package bookstore1.dao;

import java.util.Collection;
import java.util.List;

import bookstore1.domain.Book;
import bookstore1.domain.ShoppingCartItem;
import bookstore1.web.Page;
import bookstore1.web.CriteriaBook;

public interface BookDAO {

	/**
	 * ���� id ��ȡָ�� Book ����
	 * @param id
	 * @return
	 */
	public abstract Book getBook(int id);

	/**
	 * ���ݴ���� CriteriaBook ���󷵻ض�Ӧ�� Page ����
	 * @param cb
	 * @return
	 */
	public abstract Page<Book> getPage(CriteriaBook cb);

	/**
	 * ���ݴ���� CriteriaBook ���󷵻����Ӧ�ļ�¼��
	 * @param cb
	 * @return
	 */
	public abstract long getTotalBookNumber(CriteriaBook cb);

	/**
	 * ���ݴ���� CriteriaBook �� pageSize ���ص�ǰҳ��Ӧ�� List 
	 * @param cb
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public abstract List<Book> getPageList(CriteriaBook cb,int pageSize);

	/**
	 * ����ָ�� id �� book �� storeNumber �ֶε�ֵ
	 * @param id
	 * @return
	 */
	public abstract int getStoreNumber(Integer id);

	/**
	 * ���ݴ���� ShoppingCartItem �ļ���, 
	 * �������� books ���ݱ�� storenumber �� salesnumber �ֶε�ֵ
	 * @param items
	 */
	public abstract void batchUpdateStoreNumberAndSalesAmount(
			Collection<ShoppingCartItem> items);

}