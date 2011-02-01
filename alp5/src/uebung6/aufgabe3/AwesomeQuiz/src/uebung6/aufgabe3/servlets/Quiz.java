package uebung6.aufgabe3.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import uebung6.aufgabe3.beans.AnswerBean;
import uebung6.aufgabe3.beans.BeanBag;

/**
 * Servlet implementation class Quiz
 */
@WebServlet("/Quiz")
public class Quiz extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private HttpSession session;
	private BeanBag beanBag;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Quiz() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Session holen
		session = request.getSession(true);
		beanBag = (BeanBag) session.getAttribute("quizstate");
		if (beanBag == null) {
			beanBag = new BeanBag();
			session.setAttribute("quizstate", beanBag);
		}

		// Parameter holen
		AnswerBean a = (AnswerBean) request.getAttribute("aBean");
		beanBag.addAnswer(a);

		getServletConfig().getServletContext()
				.getRequestDispatcher("/Question.jsp")
				.forward(request, response);
	}

}
