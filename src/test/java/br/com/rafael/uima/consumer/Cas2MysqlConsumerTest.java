package br.com.rafael.uima.consumer;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.XmiCasDeserializer;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.CasCreationUtils;
import org.junit.Test;

import br.com.rafael.uima.util.AEUtils;

public class Cas2MysqlConsumerTest {

	String typeFile = "tsDescriptor.xml";
	String casFile = "SimpleText.txt.xmi";
	String descFile = "cas-consumer-mysql.xml";

	@Test
	public void Test() {
		boolean success = false;

		URL urlTypeFile = this.getClass().getResource(typeFile);
		URL urlCasFile = this.getClass().getResource(casFile);
		URL urlDescFile = this.getClass().getResource(descFile);

		try {
			String pathTypeFile = new File(urlTypeFile.toURI()).getAbsolutePath();
			String pathCasFile = new File(urlCasFile.toURI()).getAbsolutePath();
			String pathDescfile = new File(urlDescFile.toURI()).getAbsolutePath();
			TypeSystemDescription tsDesc = AEUtils.getTypeSystem(pathTypeFile);

			CAS cas = CasCreationUtils.createCas(tsDesc, null, null);
			XmiCasDeserializer.deserialize(new FileInputStream(pathCasFile), cas, false);

			JCas jcas = cas.getJCas();

			AnalysisEngine ae = AEUtils.createAE(pathDescfile);

			ae.process(jcas);

			ae.destroy();

			success = true;

		} catch (AnalysisEngineProcessException e) {
			System.err.println("Error processing JCAS. Message: " + e.getMessage());
		} catch (ResourceInitializationException e) {
			System.err.println("Error initializing Analysis Engine. Error: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error: " + e.getClass().toString() + ". Message: " + e.getMessage());
		}

		assertEquals(true, success);
	}
}