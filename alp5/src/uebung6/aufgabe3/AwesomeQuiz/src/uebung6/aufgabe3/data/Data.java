package uebung6.aufgabe3.data;

import uebung6.aufgabe3.model.Question;

public class Data {

	final public static Question[] questions = {
			new Question(
					"1. Choose the statement that best describes the relationship between JSP and servlets:",

					new String[] {
							"Servlets are built on JSP semantics and all servlets are compiled to JSP pages for runtime usage.",
							"JSP and servlets are unrelated technologies.",
							"Servlets and JSP are competing technologies for handling web requests. Servlets are being superseded by JSP, which is preferred. The two technologies are not useful in combination.",
							"JSPs are built on servlet semantics and all JSPs are compiled to servlets for runtime usage." },
					3),

			new Question(
					"2. What is a benefit of using JavaBeans to separate business logic from presentation markup within the JSP environment ?",

					new String[] {
							"It allows the JSP to access middleware.",
							"It provides a dynamic markup environment, such that JavaBeans are integrated seamlessly with the template presentation content, in order to create the dynamic display for the client.",
							"It provides the developer with full access to the Java 2 Platform Enterprise Edition (J2EE), which is unavailable from outside the JavaBean environment.",
							"None of these" }, 2),

			new Question(
					"3. Why use RequestDispatcher to forward a request to another resource, instead of using a sendRedirect ?",

					new String[] {
							"Redirects are no longer supported in the current servlet API.",
							"Redirects are not a cross-platform portable mechanism.",
							"The RequestDispatcher does not use the reflection API.",
							"The RequestDispatcher does not require a round trip to the client, and thus is more efficient and allows the server to maintain request state." },
					3),

			new Question(
					"4. What alternatives exist to embedding Java code directly within the HTML markup of your JSP page ?",

					new String[] {
							"Moving the code into your session manager.",
							"Moving the code into scriptlets.",
							"Moving the code into JavaBeans and servlets.",
							"Moving the code into a transaction manager." }, 2),

			new Question(
					"5. What type of scriptlet code is better-suited to being factored forward into a servlet ?",

					new String[] {
							"Code that deals with logic that is common across requests.",
							"Code that deals with logic that is vendor specific.",
							"Code that deals with logic that relates to database access.",
							"Code that deals with logic that relates to client scope." },
					0),

			new Question(
					"6. Choose the statement that best describes how to connect JSP pages and Enterprise JavaBeans (EJBs):",

					new String[] {
							"Lookup the EJBs from within a JSP, but use the EJBs from within a basic JavaBean.",
							"Lookup and use the EJBs from a separate business delegate. The JavaBeans that work with JSP pages are clients to these business delegates and know nothing about EJB specifics.",
							"Lookup the EJBs from within a servlet, delegating usage to specific JSP pages.",
							"Lookup and use the EJBs from within a JSP page, but only as remote references." },
					2),

			new Question(
					"7. Are custom tags available in JSP 1.0? If not, how else might you implement iteration from within a JSP ?",

					new String[] {
							"Yes, but the only tags available relate to database access.",
							"Yes, but custom tags will not help developers create tags for use in iterating over a collection.",
							"No. To iterate over a collection of values, one must use scriptlet code.",
							"No, but there is a standard tag that may be used." },
					1),

			new Question(
					"8. What is the initial contact point for handling a web request in a Page-Centric architecture ?",

					new String[] { "A JSP page.", "A JavaBean.", "A servlet.",
							"A session manager." }, 0),

			new Question(
					"9. What is the difference between doing an include or a forward with a RequestDispatcher ?",

					new String[] {
							"The two methods provide the same functionality, but with different levels of persistence.",
							"The forward method is deprecated as of JSP 1.1 and the include method should be used in order to substitute portions of a dynamic display at runtime.",
							"The include method transfers control to a dynamic resource, while the forward method allows for dynamic substitution of another JPS pages output, returning control to the calling resource.",
							"The forward method transfers control to the designated resource, while the include method invokes the designated resource, substitutes its output dynamically in the display, and returns control to the calling page." },
					3),

			new Question(
					"10. Which of the following can the JSP include action include output from ?",

					new String[] { "Another JSP", "Servlet", "Plain text file",
							"All of the above" }, 3) };
}
