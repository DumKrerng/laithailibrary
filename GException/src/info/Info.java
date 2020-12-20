package info;


/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/15/11
 * Time: 9:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Info {

	public static void infoMessage(String p_strMessage) {
		System.out.println("[Info Message]: ".concat(p_strMessage));
	}

	public static void infoError(String p_strMessage) {
		System.err.println("[Error Message]: ".concat(p_strMessage));
	}

	private static class PrintInfoError implements Runnable {

		String i_strMessage;

		protected PrintInfoError(String p_strMessage) {
			i_strMessage = p_strMessage;
		}

		public void run() {
			System.err.println("[Error Message]: ".concat(i_strMessage));
		}
	}
}
