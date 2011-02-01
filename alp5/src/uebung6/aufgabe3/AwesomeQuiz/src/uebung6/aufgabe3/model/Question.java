package uebung6.aufgabe3.model;

public class Question {

	private final String question;
	private final String[] answers;
	private final Integer solution;

	public Question(String question, String[] answers, Integer solution) {
		this.question = question;
		this.answers = answers;
		this.solution = solution;
	}

	@Override
	public String toString() {
		return question;
	}
	
	public String getQuestion() {
		return question;
	}

	public String[] getAnswers() {
		return answers;
	}

	public Integer getSolution() {
		return solution;
	}
}
