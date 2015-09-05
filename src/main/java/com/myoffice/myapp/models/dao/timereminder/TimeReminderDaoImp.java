package com.myoffice.myapp.models.dao.timereminder;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.myoffice.myapp.models.dao.common.AbstractDao;
import com.myoffice.myapp.models.dto.TimeReminder;

@Repository
public class TimeReminderDaoImp extends AbstractDao implements TimeReminderDao {

	@Override
	public TimeReminder findTimeReminderById(Integer timeId) {
		return (TimeReminder) getSession().get(TimeReminder.class, timeId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimeReminder> findAllTimeReminder() {
		Criteria criteria = getSession().createCriteria(TimeReminder.class);
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TimeReminder> findActiveTimeReminder(Date toDay) {
		Criteria criteria = getSession().createCriteria(TimeReminder.class);
		criteria.add(Restrictions.eq("completed", false));
		criteria.add(Restrictions.and(Restrictions.le("remindTime", toDay)));
		criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		return criteria.list();
	}

	@Override
	public void saveTimeReminder(TimeReminder timeReminder) {
		persist(timeReminder);
	}
	
	

}
