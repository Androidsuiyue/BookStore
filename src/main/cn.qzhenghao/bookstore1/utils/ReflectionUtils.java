 package bookstore1.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * ����� Utils �������� �ṩ����˽�б���, ��ȡ�������� Class, ��ȡ������Ԫ�����Ե� Utils ����
 * 
 * @author Administrator
 *
 */
public class ReflectionUtils {

	/**
	 * ͨ通过反射,获得指定类的父类的泛型参数的实际类型 如: public EmployeeDao extends
	 * BaseDao<Employee, String>
	 * 
	 * @param clazz
	 * @param index
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Class getSuperClassGenricType(Class clazz, int index) {
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}

		if (!(params[index] instanceof Class)) {
			return Object.class;
		}

		return (Class) params[index];
	}

	/**
	 * ͨ通过反射,获得指定类的父类的第一个泛型参数的实际类型. 如BuyerServiceBean extends
	 * BaseDao<Employee, String>
	 * 
	 * @param <T>
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getSuperGenericType(Class clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 *循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问. 如向上转型到Object仍无法找到, 返回null.
	 * 匹配函数名+参数类型。
	 * 
	 * @param object
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {

		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				// superClass.getMethod(methodName, parameterTypes);
				return superClass.getDeclaredMethod(methodName, parameterTypes);
			} catch (NoSuchMethodException e) {
				// Method ���ڵ�ǰ�ඨ��, ��������ת��
			}
			// ..
		}

		return null;
	}

	/**
	 * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
	 * 
	 * @param field
	 */
	public static void makeAccessible(Field field) {
		if (!Modifier.isPublic(field.getModifiers())) {
			field.setAccessible(true);
		}
	}

	/**
	 * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问
	 * 
	 * @param object
	 * @param filedName
	 * @return
	 */
	public static Field getDeclaredField(Object object, String filedName) {

		for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(filedName);
			} catch (NoSuchFieldException e) {
				//  Field不在当前类定义,继续向上转型
			}
		}
		return null;
	}

	/***
	 * 直接调用对象方法, 无视private/protected修饰符.
	 * 用于一次性调用的情况，否则应使用getAccessibleMethod()函数获得Method后反复调用
	 * 
	 * @param object
	 *            �������
	 * @param methodName
	 *            方法名
	 * @param parameterTypes
	 *            参数类型
	 * @param parameters
	 *            参数集合
	 * @return ���෽����ִ�н��
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters)
			throws InvocationTargetException {

		// //���� ���󡢷������Ͷ�Ӧ�ķ������� ͨ������ ��������� ������ȡ Method ����
		Method method = getDeclaredMethod(object, methodName, parameterTypes);

		if (method == null) {
				throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
		}
		// ����Java�Է������м��,��Ҫ�����˽�з�������
		method.setAccessible(true);
		// ����object �� method ������ķ������䷽�� �Ĳ����� parameters
		try {
			return method.invoke(object, parameters);
		} catch (IllegalAccessException e) {
			System.out.println("�������׳����쳣");
		}

		return null;
	}

	/**
	 * 直接设置对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 * 
	 * @param object
	 * @param fieldName
	 * @param value
	 */
	public static void setFieldValue(Object object, String fieldName, Object value) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			System.out.println("不可能抛出的异常:{}");
		}
	}

	/**
	 * 直接读取对象属性值, 无视private/protected修饰符, 不经过setter函数.
	 *
	 * 
	 * @param object
	 * @param fieldName
	 * @return
	 */
	public static Object getFieldValue(Object object, String fieldName) {
		Field field = getDeclaredField(object, fieldName);

		if (field == null)
			throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");

		makeAccessible(field);

		Object result = null;

		try {
			result = field.get(object);
		} catch (IllegalAccessException e) {
			System.out.println("不可能抛出的异常:{}");
		}

		return result;
	}
}
