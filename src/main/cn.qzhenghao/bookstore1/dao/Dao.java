package bookstore1.dao;

import java.util.List;

/**
 * Dao 接口，定义 Dao 的基本操作，由BaseDao 提供实现。
 * @param <T>: Dao 实际操作的泛型类型。
 */
public interface Dao<T> {
	
	/**
	 *  执行 insert 操作，返回插入后的记录的ID
	 * @param sql: 待执行的 SQL 语句。
	 * @param args: 填充占位符的可变参数。
	 * @return: 插入新记录的ID。
	 */
	long insert(String sql, Object ... args);
	
	/**
	 * 执行 update 操作（insert（但没有返回值）、update、delete）
	 * @param sql:待执行的 SQL 语句。
	 * @param args: 填充占位符的可变参数。
	 */
	void update(String sql, Object ... args);
	
	/**
	 * ִ�е�����¼�Ĳ�ѯ����, �������¼��Ӧ�����һ������
	 * @param sql: 待执行的 SQL 语句。
	 * @param args: 填充占位符的可变参数。
	 * @return: 与记录对应的类的一个对象。
	 */
	T query(String sql, Object ... args);
	
	/**
	 * ִ�ж�����¼�Ĳ�ѯ����, �������¼��Ӧ�����һ�� List
	 * @param sql: 待执行的 SQL 语句。
	 * @param args: 填充占位符的可变参数。
	 * @return: 与记录对应的类的一个 List。
	 */
	List<T> queryForList(String sql, Object ... args);

	/**
	 * 执行一个属性或值得查询，例如查询某一条记录的一个字段或查询某个统计信息，返回要查询的值。
	 * @param sql: 待执行的 SQL 语句。
	 * @param args: 填充占位符的可变参数。
	 * @return: ִ��һ�����Ի�ֵ�Ĳ�ѯ����, �����ѯĳһ����¼��һ���ֶ�, ���ѯĳ��ͳ����Ϣ, ����Ҫ��ѯ��ֵ
	 */
		<V> V getSingleVal(String sql, Object ... args);
	
	/**
	 *执行批量根性操作
	 * @param sql:待执行的 SQL 语句。
	 * @param params: 填充占位符的可变参数。
	 */
	void batch(String sql, Object[]... params);
}