package msg;


import exc.ExcDialog;
import exc.GException;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 4/1/12
 * Time: 11:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class MsgDialog extends ExcDialog {

	public MsgDialog(String p_strMassage) {
		super(p_strMassage);
	}

	public MsgDialog(String p_strMassage, MessageType p_messagetype) {
		super(p_strMassage, p_messagetype);
	}

	public MsgDialog(String p_strHeader, String p_strMassage) throws GException {
		super(p_strHeader, p_strMassage);
	}

	public MsgDialog(String p_strHeader, String p_strMassage, MessageType p_messagetype) throws GException {
		super(p_strHeader, p_strMassage, p_messagetype);
	}
}
