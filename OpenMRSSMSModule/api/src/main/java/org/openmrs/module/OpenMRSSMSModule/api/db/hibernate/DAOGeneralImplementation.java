package org.openmrs.module.OpenMRSSMSModule.api.db.hibernate;

import java.util.List;

import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.openmrs.module.OpenMRSSMSModule.api.db.DAOGeneral;

public class DAOGeneralImplementation implements DAOGeneral {
	
	public static int	MAX_OUTBOUND_FETCH_PER_GO		= 6;
	public static int	MAX_OUTBOUND_SPAM_DUPLICATE_BOUNDARY		= 2;
	public static int	MAX_OUTBOUND_SPAM_SMS_ALLOWED_BOUNDARY		= 6;
	public static int	MAX_OUTBOUND_REVERT_TRIES		= 3;
	SessionFactory sessionFactory;
	public void setSessionFactory(SessionFactory sessionFactory) { 
		this.sessionFactory = sessionFactory;
	}
	@Override
	public List slqQuery() {
		
		return sessionFactory.getCurrentSession().createSQLQuery("select o.outboundid, o.recipient, A.count Acount,B.count Bcount from outboundmessage as o " +
							" left join (select text,recipient, count(*) as count from outboundmessage " +
							"	where date(sentdate) = curdate() and status in ('sent','unknown') " +
							"	group by recipient) as A on A.recipient = o.recipient and A.text=o.text " +
							" left join (select recipient, count(*) as count from outboundmessage " +
							"	where date(sentdate) = curdate() and status in ('sent','unknown') " +
							"	group by recipient) as B on B.recipient = o.recipient " +
							" where o.status = 'pending' " +
							" having Acount > "+MAX_OUTBOUND_SPAM_DUPLICATE_BOUNDARY+" " +
									" OR Bcount > "+MAX_OUTBOUND_SPAM_SMS_ALLOWED_BOUNDARY+" " +
							" order by recipient").setFirstResult(0).setMaxResults(100).list();
	}
	@Override
	public SQLQuery updateQuery(String query) {
		return sessionFactory.getCurrentSession().createSQLQuery(query);
		
	}

}
