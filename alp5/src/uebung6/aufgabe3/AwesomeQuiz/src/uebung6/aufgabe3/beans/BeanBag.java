package uebung6.aufgabe3.beans;

import java.util.ArrayList;
import java.util.List;

import uebung6.aufgabe3.data.Data;

public class BeanBag {

	private long startTime = System.currentTimeMillis();
	private int currentQuestion = 0;
	private List<AnswerBean> answers = new ArrayList<AnswerBean>();
	private List<Long> times = new ArrayList<Long>();

	public void addAnswer(AnswerBean a) {
		answers.add(a);
		times.add(System.currentTimeMillis() - startTime);
		// Reset timer for next question
		startTime = System.currentTimeMillis();
		currentQuestion++;
	}

	public int getScore() {
		int correct = 0, given, known;
		for (int i = 0; i < answers.size(); i++) {
			try {
				given = Integer.parseInt(answers.get(i).getAnswer());
				known = Data.questions[i].getSolution();
				if (given == known)
					correct++;
			} catch (NumberFormatException e) {
				continue;
			}
		}
		return correct;
	}

	public float getTotalTimeSec() {
		long total = 0;
		for (long t : times)
			total += t;
		// One digit in the fraction
		return (total / 100) / 10.0f;
	}

	public int answerGivenFor(int questionNo) {
		try {
			return Integer.parseInt(answers.get(questionNo).getAnswer());
		} catch (Exception e) {
			return -1;
		}
	}

	public long getStartTime() {
		return startTime;
	}

	public List<AnswerBean> getAnswers() {
		return answers;
	}

	public List<Long> getTimes() {
		return times;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setAnswers(List<AnswerBean> Answers) {
		this.answers = Answers;
	}

	public void setTimes(List<Long> times) {
		this.times = times;
	}

	public void setCurrentQuestion(int currentQuestion) {
		this.currentQuestion = currentQuestion;
	}

	public int getCurrentQuestion() {
		return currentQuestion;
	}
}
