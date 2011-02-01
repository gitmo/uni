package uebung6.aufgabe3.beans;

import java.util.HashSet;
import java.util.Set;

public class HallOfFame {
	private Set<HighscoreBean> hallOfFame = new HashSet<HighscoreBean>();

	public void enter(HighscoreBean entry) {
		hallOfFame.add(entry);
	}

	public void setHallOfFame(Set<HighscoreBean> hallOfFame) {
		this.hallOfFame = hallOfFame;
	}

	public Set<HighscoreBean> getHallOfFame() {
		return hallOfFame;
	}

}
