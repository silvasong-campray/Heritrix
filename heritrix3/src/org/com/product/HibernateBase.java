package org.com.product;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

final public class HibernateBase {
	private static SessionFactory sf;
	public static SessionFactory getSessionFactory()
	{
		Configuration cfg=new Configuration();
		cfg.configure(); //��������
		
		sf=cfg.buildSessionFactory();
		return sf;
	}

}
