package br.com.rafael.uima.consumer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import br.com.rafael.uima.db.dao.AnnotationModelDao;
import br.com.rafael.uima.model.AnnotationModel;

public class Cas2MysqlConsumer extends JCasAnnotator_ImplBase {

	//Error messages
	public static final String MESSAGE_DIGEST = "br.com.rafael.uima.consumer.Cas2MysqlConsumer_Messages";

	// Global Variables
	private Logger logger = null;
	private JCas jcas = null;

	// Database class
	private AnnotationModelDao annotDb = null;

	// JDBC Parameters
	private String database = null;
	private String url = null;
	private String driver = null;
	private String table = null;

	@Override
	public void initialize(UimaContext aContext) throws ResourceInitializationException {
		super.initialize(aContext);

		logger = getContext().getLogger();

		logger.log(Level.INFO, "Retrieving parameters");

		database = (String) aContext.getConfigParameterValue("database");
		url = (String) aContext.getConfigParameterValue("urlConnection");
		table = (String) aContext.getConfigParameterValue("table");
		driver = (String) aContext.getConfigParameterValue("driver");

	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {

		logger.log(Level.INFO, "Process Jcas");
		this.jcas = jcas;
		try {
			intiDB();

			// if you need, create your table
			annotDb.createTable();
			extractAndInsertAnnotations();

		} catch (ClassNotFoundException e) {
			throw new AnalysisEngineProcessException(MESSAGE_DIGEST, "driver_not_found", new Object[]{e.getMessage()});
		} catch (SQLException e) {
			throw new AnalysisEngineProcessException(MESSAGE_DIGEST, "database_error", new Object[]{e.getMessage()});
		}

		logger.log(Level.INFO, "Finished processing");
	}

	private void intiDB() throws ClassNotFoundException, SQLException {
		logger.log(Level.INFO, "Analysis Engine initDb");
		annotDb = new AnnotationModelDao(driver, database, url, table);
		annotDb.setLogger(logger);
		annotDb.initConnection();
	}

	private void extractAndInsertAnnotations() throws SQLException {
		logger.log(Level.INFO, "Analysis Engine extractAnnotations");

		String doc = getDocumentName();

		List<AnnotationModel> annotations = new ArrayList<AnnotationModel>();
		Iterator<Annotation> it = jcas.getAnnotationIndex().iterator();

		while (it.hasNext()) {
			Annotation a = it.next();

			AnnotationModel aModel = new AnnotationModel();

			aModel.setAnnotName(a.getType().getShortName());
			aModel.setAnnotCoveredText(a.getCoveredText());
			aModel.setStartPos(a.getBegin());
			aModel.setEndPos(a.getEnd());
			

			annotations.add(aModel);
		}

		annotDb.insertAnnotations(annotations, doc);
	}

	private String getDocumentName() {
		// TODO: Apply logic to extract the document name
		return "doc";
	}

}
