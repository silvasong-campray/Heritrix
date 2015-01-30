package org.com.product;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;

final public class HibernateBase {
	/*private static SessionFactory sf;
	public static SessionFactory getSessionFactory(){
		Configuration cfg=new Configuration();
		cfg.configure(); 
		sf=cfg.buildSessionFactory();
		return sf;
	   }*/
	
	public static void save(Object o){
		Configuration cfg=new Configuration();
		cfg.configure(); 
		SessionFactory sf=cfg.buildSessionFactory();
		Session s=sf.openSession();
    	Transaction tx=s.beginTransaction();
		s.save(o);
        tx.commit();
		s.close();
		sf.close();
	}
	
	public static void update(Object o){
		Configuration cfg=new Configuration();
		cfg.configure(); 
		SessionFactory sf=cfg.buildSessionFactory();
		Session s=sf.openSession();
    	Transaction tx=s.beginTransaction();
		s.update(o);
        tx.commit();
		s.close();
		sf.close();
		
	}
	
	public static Object getById(String hql){
		 Configuration cfg=new Configuration();
		 cfg.configure(); 
		 SessionFactory sf=cfg.buildSessionFactory();
		 Session s=sf.openSession();
		 Query query=s.createQuery(hql);
		 List obj = query.list();
		 s.close();
		 sf.close();
		 if(obj.size() ==0 ){
			 return null;
		 }
		 return obj.get(0);
	}
    
	
		  
}
		  
		 
	       
	  
