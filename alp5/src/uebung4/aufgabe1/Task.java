package uebung4.aufgabe1;

import java.io.Serializable;

public interface Task<Argument, Result> extends Serializable {

	Result exec(Argument a);

}