package pp;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 4/1/12
 * Time: 11:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProgramExit {
	public static final int Normal = 0;
	public static final int DatabaseNotCompatibleApplication = 1;
	public static final int CanNotConnectToDatabase = 2;
	public static final int DuplicateRegisterDBDataTable = 3;
	public static final int InvalidTableName = 4;
	public static final int InvalidFieldName = 5;
	public static final int ReadingPropertiesError = 6;
	public static final int CreateDBComplete = 7;
	public static final int CanNotCreateNewDB = 8;
	public static final int ReadingConfigPropertiesError = 9;
	public static final int InvalidDBType = 10;
	public static final int InvalidDBConnectionString = 11;
	public static final int InvalidSessionID = 12;
	public static final int CanNotConnectToServer = 13;
	public static final int NotExistLoggingConfigurationFile = 14;
	public static final int Other = 90;
}
