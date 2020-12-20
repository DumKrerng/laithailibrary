package com.laithailibrary.sharelibrary.db.dbobject.support;

import java.util.*;
import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;

public enum DayID {
	SUN(1, "Sunday", "อาทิตย์"),
	MON(2, "Monday", "จันทร์"),
	TUE(3, "Tuesday", "อังคาร"),
	WED(4, "Wednesday", "พุธ"),
	THU(5, "Thursday", "พฤหัสบดี"),
	FRI(6, "Friday", "ศุกร์"),
	SAT(7, "Saturday", "เสาร์");

	private int m_intID = -1;
	private String m_strName_ENG = "";
	private String m_strName_THA = "";

	private DayID(int p_intID, String p_strName_ENG, String p_strName_THA) {
		m_intID = p_intID;
		m_strName_ENG = p_strName_ENG;
		m_strName_THA = p_strName_THA;
	}

	public int getID() {
		return m_intID;
	}

	public String getID_String() {
		return Integer.toString(m_intID);
	}

	public String getName_ENG() {
		return m_strName_ENG;
	}

	public String getName_THA() {
		return m_strName_THA;
	}

	public static DayID getDayID(DBInteger p_dbintDayID) throws GException {
		try {
			if(p_dbintDayID.getIntValue()  == SUN.getID()) {
				return SUN;

			} else if(p_dbintDayID.getIntValue()  == MON.getID()) {
				return MON;

			} else if(p_dbintDayID.getIntValue()  == TUE.getID()) {
				return TUE;

			} else if(p_dbintDayID.getIntValue()  == WED.getID()) {
				return WED;

			} else if(p_dbintDayID.getIntValue()  == THU.getID()) {
				return THU;

			} else if(p_dbintDayID.getIntValue()  == FRI.getID()) {
				return FRI;

			} else if(p_dbintDayID.getIntValue()  == SAT.getID()) {
				return SAT;
			}

			throw new GException("Invalid DayID(" + p_dbintDayID + ").");

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static DayID getDayID_ENGDayLabel(String p_strDayLabel) throws GException {
		try {
			if(p_strDayLabel.compareTo(SUN.getName_ENG()) == 0) {
				return SUN;

			} else if(p_strDayLabel.compareTo(MON.getName_ENG()) == 0) {
				return MON;

			} else if(p_strDayLabel.compareTo(TUE.getName_ENG()) == 0) {
				return TUE;

			} else if(p_strDayLabel.compareTo(WED.getName_ENG()) == 0) {
				return WED;

			} else if(p_strDayLabel.compareTo(THU.getName_ENG()) == 0) {
				return THU;

			} else if(p_strDayLabel.compareTo(FRI.getName_ENG()) == 0) {
				return FRI;

			} else if(p_strDayLabel.compareTo(SAT.getName_ENG()) == 0) {
				return SAT;
			}

			throw new GException("Invalid Day(" + p_strDayLabel + ").");

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static List<DayID> getDayIDs_WithoutSUNSAT() throws GException {
		try {
			List<DayID> lsDayIDs = new GList<>();
			lsDayIDs.add(MON);
			lsDayIDs.add(TUE);
			lsDayIDs.add(WED);
			lsDayIDs.add(THU);
			lsDayIDs.add(FRI);

			return lsDayIDs;

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static DayID getDayID_CurrentDate() throws GException {
		return DBDate.getCurrentDate().getDayID();
	}
}
