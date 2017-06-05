package br.com.rafael.uima.util;

import java.io.File;
import java.io.IOException;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.InvalidXMLException;
import org.apache.uima.util.XMLInputSource;
import org.apache.uima.util.XMLParser;

public class AEUtils {

	private static XMLParser xmlParser = UIMAFramework.getXMLParser();

	public static final JCas processDocument(JCas jcas, AnalysisEngine ae,
		String docText) throws AnalysisEngineProcessException {
		if (docText != null) {
			jcas.setDocumentText(docText);
			ae.process(jcas);
		} else {
			System.err
				.println("There is no text to analyse - null document");
		}
		return jcas;
	}



	public static final AnalysisEngine createAE(String aePath) throws ResourceInitializationException, IOException, InvalidXMLException {
		System.out.println("CreateAE Entered");
		try {
			File descFile = new File(aePath);
			XMLInputSource in = new XMLInputSource(descFile);
			org.apache.uima.resource.ResourceSpecifier specifier = xmlParser.parseResourceSpecifier( in );
			return org.apache.uima.UIMAFramework
				.produceAnalysisEngine(specifier);
		} catch (ResourceInitializationException e) {
			System.err.println("Resource Initialization Exception creating AE");
			throw e;
		} catch (IOException e) {
			System.err.println("IO Error creating AE");
			throw e;
		} catch (InvalidXMLException e) {
			System.err.println("Invalid XML. Error creating AE");
			throw e;
		}
	}

	public static final TypeSystemDescription getTypeSystem(String tsDescriptorPath) throws IOException, InvalidXMLException {

		try {
			XMLInputSource xmlIn = new XMLInputSource(tsDescriptorPath);
			return xmlParser.parseTypeSystemDescription(xmlIn);
		} catch (InvalidXMLException e) {
			System.err.println("Invalid Type System descriptor");
			throw e;
		} catch (IOException e) {
			System.err.println("Invalid path to Type System descriptor");
			throw e;
		}
	}
}