package es.oruiz.redmine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.management.relation.Relation;

import com.taskadapter.redmineapi.Include;
import com.taskadapter.redmineapi.IssueManager;
import com.taskadapter.redmineapi.Params;
import com.taskadapter.redmineapi.RedmineException;
import com.taskadapter.redmineapi.RedmineManager;
import com.taskadapter.redmineapi.RedmineManagerFactory;
import com.taskadapter.redmineapi.TimeEntryManager;
import com.taskadapter.redmineapi.bean.Attachment;
import com.taskadapter.redmineapi.bean.Changeset;
import com.taskadapter.redmineapi.bean.CustomField;
import com.taskadapter.redmineapi.bean.Issue;
import com.taskadapter.redmineapi.bean.IssueRelation;
import com.taskadapter.redmineapi.bean.Journal;
import com.taskadapter.redmineapi.bean.Project;
import com.taskadapter.redmineapi.bean.TimeEntry;
import com.taskadapter.redmineapi.bean.Watcher;
import com.taskadapter.redmineapi.internal.ResultsWrapper;

public class Principal {

	public Principal() {
		// TODO Auto-generated constructor stub
	}

	//https://github.com/taskadapter/redmine-java-api
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Principal p = new Principal();
		
		p.prueba();
		
	}

	private void prueba() {
		String uri = null; 
		String apiAccessKey = null; 
		
		Properties prop = new Properties();
		InputStream input = null;
		try { 
			input = new FileInputStream("config.properties");
			
			prop.load(input);
			
			uri = prop.getProperty("URI");
			apiAccessKey = prop.getProperty("token");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
System.out.println("1");
		try {
			RedmineManager mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
			System.out.println("2");			
			
			/*  para filtrar las peticiones que se buscan
				Params params = new Params()
				            .add("set_filter", "1")
				            .add("f[]", "summary")
				            .add("op[summary]", "~")
				            .add("v[summary]", "another")
				            .add("f[]", "description")
				            .add("op[description]", "~")
				            .add("v[description][]", "abc");
				
				result = issueManager.getIssues(params);			
			
			*/
			
			/* mostrar proyectos */
			List<Project> projects = mgr.getProjectManager().getProjects();		
			for (Project project : projects) {
			    System.out.println(project.toString()); 
			}			
			
			System.out.println("busqueda por id de proyecto: " + mgr.getProjectManager().getProjectByKey("77").getName());
			
			/* Mostrar relations */
			IssueManager issueManager = mgr.getIssueManager();
			Issue issue = issueManager.getIssueById(26681, Include.journals, Include.relations, Include.attachments, 
                    Include.changesets, Include.watchers );
			
			/* Mostrar journals : actuaciones*/
			/*
System.out.println("journals");					
			Collection<Journal> journals =  issue.getJournals();			
			Iterator<Journal> itj = journals.iterator(); 
			while (itj.hasNext()) {
				System.out.println(itj.next().getId() + " " + itj.next().getNotes() + " " + itj.next().toString());
			}
*/			
System.out.println(issue.getCategory() + " " + issue.toString() + " " + issue.getStatusName());
System.out.println(issue.getTracker().getName() + " " + issue.getTracker().getId());
System.out.println("campos personalizados");			
			/* Mostrar relations */
			Collection<CustomField> campos =  issue.getCustomFields();			
			Iterator<CustomField> itc = campos.iterator(); 
			while (itc.hasNext()) {
				System.out.println(itc.next().toString());
			}			

System.out.println("relaciones");			
			/* Mostrar relations */
			Collection<IssueRelation> relations =  issue.getRelations();			
			Iterator<IssueRelation> it = relations.iterator(); 
			while (it.hasNext()) {
				System.out.println(it.next().toString());
			}			
System.out.println("childrens");			
			/* Mostrar subtareas */
			Collection<Issue> childrens =  issue.getChildren();			
			Iterator<Issue> child = childrens.iterator(); 
System.out.println("nº hijos: "+childrens.size());			
			while (child.hasNext()) {
				System.out.println(child.next().toString());
			}						
System.out.println("attachments");			
			/* Mostrar subtareas */
			Collection<Attachment> att =  issue.getAttachments();			
			Iterator<Attachment> a = att.iterator(); 
System.out.println("nº hijos: "+att.size());			
			while (a.hasNext()) {
				System.out.println(a.next().toString());
			}			
System.out.println("changesets");			
			/* Mostrar subtareas */
			Collection<Changeset> chsets =  issue.getChangesets();			
			Iterator<Changeset> ch = chsets.iterator(); 
System.out.println("nº hijos: "+chsets.size());			
			while (ch.hasNext()) {
				System.out.println(ch.next().toString());
			}						
System.out.println("watchers");			
			/* Mostrar subtareas */
			Collection<Watcher> watchers =  issue.getWatchers();			
			Iterator<Watcher> watcher = watchers.iterator(); 
System.out.println("nº hijos: "+watchers.size());			
			while (watcher.hasNext()) {
				System.out.println(watcher.next().toString());
			}									
			/* recorrer todas las peticiones:  es muy lento al obtenerlas todas
			List<Issue> issues = mgr.getIssueManager().getIssues(projectKey, queryId);
			System.out.println("3");
			for (Issue issue : issues) {
			    System.out.println(issue.toString());
			}
			*/	
			System.out.println("Facturación");
			
			TimeEntryManager timeEntryManager = mgr.getTimeEntryManager();
			final Map<String, String> params = new HashMap<>();
			params.put("project_id", "475");
			params.put("issue_id", "49881");
			//params.put("activity_id", "49881");
			final List<TimeEntry> elements = timeEntryManager.getTimeEntries(params).getResults();	
			for (TimeEntry elemento : elements) {
			    System.out.println(elemento.toString());
			    System.out.println(elemento.getCreatedOn() + " " + elemento.getUpdatedOn()
			    		+ " " + elemento.getSpentOn());  
			}	
		
		} catch (RedmineException e) {
			System.out.println("Se ha producido una RedmineExcepción."+ e.getMessage());
		}
	}
	
}
