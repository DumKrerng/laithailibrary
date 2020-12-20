package exc;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 6/4/11
 * Time: 11:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class GThrowable extends Throwable {

	public GThrowable(String p_strMessage) {
		super(p_strMessage);

		try{
			if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
				new ExcDialog(p_strMessage);
			}
		} catch (Exception exception) {
		  exception.printStackTrace();
		}
	}

	public GThrowable(Exception p_exception) {
		super(p_exception);

		try{
//			if(p_exception instanceof SQLException) {
//				SQLException sqlException = (SQLException)p_exception;
//
//				StringBuilder builderSQLState = new StringBuilder();
//				builderSQLState.append("SQL :").append(sqlException.toString());
//
//				Info.infoError(builderSQLState.toString());
//			}


			if(ExcDialog.getDisplay() == ExcDialog.Display.Yes) {
				new ExcDialog(p_exception);
			}
		} catch (Exception exception) {
		  exception.printStackTrace();
		}
	}
}
