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
	
	RedmineManager mgr;

	public Principal() {
		// TODO Auto-generated constructor stub
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
		
		mgr = RedmineManagerFactory.createWithApiKey(uri, apiAccessKey);
		mgr.setObjectsPerPage(100);
	}

	//https://github.com/taskadapter/redmine-java-api
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Principal p = new Principal();
		
//		p.prueba();
		
//		p.lambda();
		
//		p.issuesForProject();

		int peticion = 36863;
		System.out.println("Tiempo restante para la petición " + peticion + ": " + p.issueTiempoRestante(peticion));
		
	}

	private void prueba() {
		try {
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
	
	public void lambda () {
		try {
			TimeEntryManager timeEntryManager = mgr.getTimeEntryManager();			
			System.out.println("LAMBDA");
			final Map<String, String> parametros = new HashMap<>();
			final List<TimeEntry> elementos = timeEntryManager.getTimeEntries(parametros).getResults();
			System.out.println("LAMBDA-1");			
			for (TimeEntry el1 : elementos) {
			    System.out.println(el1.toString());
			}		
			System.out.println("LAMBDA-1.1");
			elementos.stream().filter(s -> s.getProjectId().toString().compareTo("35")==0)
							  .filter(s -> s.getIssueId().toString().compareTo("51037")==0)
				  			  .forEach(System.out::println);
			System.out.println("LAMBDA-2");			
			elementos.stream().map(s -> s.getProjectId())
							.filter(s -> s.toString().compareTo("35")==0)	
							.forEach(System.out::println);
			
		} catch (RedmineException e) {
			System.out.println("Se ha producido una RedmineExcepción."+ e.getMessage());
		}		
	}
	
	public void issuesForProject() {
		try {		
			IssueManager issueManager = mgr.getIssueManager();
			final Map<String, String> params = new HashMap<>();
//			params.put("project_id", "475");  // personal
			params.put("project_id", "35");  // giseiel
			params.put("limit", "10");
			params.put("offset", "0");
			params.put("status_id", "*");
			ResultsWrapper<Issue>lista = issueManager.getIssues(params);
			List<Issue>isues = lista.getResults();
			int contador = lista.getTotalFoundOnServer() / 10;

			// creamos lista con todos los issues en el servidor
			for (int j=0; j<=contador; j++) {
				if (j!=0) {
					params.replace("offset", Integer.toString(j*10));
					lista = issueManager.getIssues(params);
					isues.addAll(lista.getResults());
				}				
			}
			
			TimeEntryManager timeEntryManager = mgr.getTimeEntryManager();
			final Map<String, String> params2 = new HashMap<>();
			List<TimeEntry> elements = null;
			params2.put("project_id", "35");
			params2.put("issue_id", "1");
			
			for (Issue i : isues) {
			    System.out.println(i.toString());
				params2.replace("issue_id", i.getId().toString());
				elements = timeEntryManager.getTimeEntries(params2).getResults();	
				for (TimeEntry elemento : elements) {
				    System.out.println(elemento.toString());
				    System.out.println(elemento.getCreatedOn() + " " + elemento.getUpdatedOn()
				    		+ " " + elemento.getSpentOn());  
				}				    
			}							
		} catch (RedmineException e) {
			System.out.println("Se ha producido una RedmineExcepción."+ e.getMessage());
		}				
	}

	/*
	 * Devuelve la diferencia entre el tiempo estimado y el restante de una petición
	 * revisando todas las peticiones hijas abiertas
	 */
	public float issueTiempoRestante(Integer id) {
		try {		
			IssueManager issueManager = mgr.getIssueManager();
			Issue issue = issueManager.getIssueById(id);
			
			final Map<String, String> params = new HashMap<>();
			params.put("parent_id", Integer.toString(id));
			params.put("limit", "10");
			params.put("offset", "0");
			ResultsWrapper<Issue>lista = issueManager.getIssues(params);
			List<Issue>hijos = lista.getResults();
			int contador = lista.getTotalFoundOnServer() / 10;

			// creamos lista con todos los issues hijos en el servidor
			for (int j=0; j<=contador; j++) {
				if (j!=0) {
					params.replace("offset", Integer.toString(j*10));
					lista = issueManager.getIssues(params);
					hijos.addAll(lista.getResults());
				}				
			}
			
			TimeEntryManager timeEntryManager = mgr.getTimeEntryManager();
			final Map<String, String> params2 = new HashMap<>();
			List<TimeEntry> elements = null;
			params2.put("issue_id", id.toString());
			float tiempoTrabajado = 0;
			
			// obtenemos tiempo trabajado y estimado de la propia petición
			elements = timeEntryManager.getTimeEntries(params2).getResults();				
			for (TimeEntry elemento : elements) {
			    tiempoTrabajado += elemento.getHours().floatValue();
			}				    			
			float tiempoEstimadoRestante = (issue.getEstimatedHours()==null)?0:issue.getEstimatedHours();
			
			// buscamos tiempos en las peticiones hijas
			for (Issue i : hijos) {
//			    System.out.println(i.toString() + " " + i.getEstimatedHours() + " " + i.getSpentHours());
			    tiempoEstimadoRestante += (i.getEstimatedHours()==null)?0:i.getEstimatedHours();			    
				params2.replace("issue_id", i.getId().toString());
				elements = timeEntryManager.getTimeEntries(params2).getResults();				
				for (TimeEntry elemento : elements) {
//				    System.out.println(elemento.toString());
//				    System.out.println(elemento.getCreatedOn() + " " + elemento.getUpdatedOn()
//				    		+ " " + elemento.getSpentOn());
				    tiempoTrabajado += elemento.getHours().floatValue();
				}				    
			}						
			
//			System.out.println("Tiempo estimado: " + tiempoEstimadoRestante + " Tiempo trabajado: " + tiempoTrabajado );
			return tiempoEstimadoRestante - tiempoTrabajado;
		} catch (RedmineException e) {
			System.out.println("Se ha producido una RedmineExcepción."+ e.getMessage());
			return Float.parseFloat("0");
		}					
	}
}
