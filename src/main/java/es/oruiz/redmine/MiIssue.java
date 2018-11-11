package es.oruiz.redmine;

import java.util.Collection;

import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.Journal;

public class MiIssue extends Issue {

	private Issue is;
	
	private Collection<Journal> listaJournals;
	
	public Issue getIs() {
		return is;
	}

	public void setIs(Issue is) {
		this.is = is;
	}

	public Collection<Journal> getListaJournals() {
		return listaJournals;
	}

	public void setListaJournals(Collection<Journal> listaJournals) {
		this.listaJournals = listaJournals;
	}

	public MiIssue(IssueManager issueManager, Issue pIssue) {
		try { 
			is = pIssue;
			listaJournals = issueManager.getIssueById(is.getId(), Include.journals).getJournals();
		} catch (RedmineException e) {
			System.out.println("Se ha producido una RedmineExcepción."+ e.getMessage());
		}		
	}

}
