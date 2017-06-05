package br.com.rafael.uima.model;

public class AnnotationModel {
	private String annotName;
	private String annotCoveredText;
	private int startPos;
	private int endPos;
	
	public String getAnnotName() {
		return annotName;
	}
	public void setAnnotName(String annotName) {
		this.annotName = annotName;
	}
	public String getAnnotCoveredText() {
		return annotCoveredText;
	}
	public void setAnnotCoveredText(String annotCoveredText) {
		this.annotCoveredText = annotCoveredText;
	}
	public int getStartPos() {
		return startPos;
	}
	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}
	public int getEndPos() {
		return endPos;
	}
	public void setEndPos(int endPos) {
		this.endPos = endPos;
	}
	
}
