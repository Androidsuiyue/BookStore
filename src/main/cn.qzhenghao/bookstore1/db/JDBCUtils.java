package bookstore1.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import bookstore1.exception.DBException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * JDBC	连接工具
 *
 */
public class JDBCUtils {

	private static DataSource dataSource = null;
	
	static{
		dataSource = new ComboPooledDataSource("javawebapp");
	}
	
	//从c3p0连接池中得到数据库连接
	public static Connection getConnection(){  
		try {
			return dataSource.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("????????????!");
		}
	}
 
	//释放连接
	public static void release(Connection connection) {
		try {
			if(connection != null){
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("连接关闭出错！");
		}
	}
	
	// 2.Connection、Statement都是应用程序和数据库服务器的连接资源，使用后一定要关闭
	//关闭的顺序：先关闭后获取的,即先关闭Statement，后关闭Connection
	public static void release(ResultSet rs, Statement statement) {
		try {
			if(rs != null){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("结果集关闭出错!");
		}
		
		try {
			if(statement != null){
				statement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DBException("Statement关闭出错!");
		}
	}
	
}
