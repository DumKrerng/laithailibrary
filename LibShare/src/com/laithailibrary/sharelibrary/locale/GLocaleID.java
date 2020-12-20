package com.laithailibrary.sharelibrary.locale;

import com.laithailibrary.sharelibrary.db.dbobject.*;
import exc.*;
import java.io.*;

public class GLocaleID extends DBString {

	private GLocale m_locale = GLocale.English;

	private static final long serialVersionUID = 1;

	public GLocaleID() {}

	public GLocaleID(GLocale p_locale) {
		super(p_locale.getID());

		m_locale = p_locale;
	}

	public void writeExternal(ObjectOutput p_out) throws IOException {
		try {
			super.writeExternal(p_out);

			p_out.writeObject(m_locale);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_out.close();

			throw new IOException(exception);
		}
	}

	public void readExternal(ObjectInput p_in) throws IOException {
		try {
			super.readExternal(p_in);

			m_locale = (GLocale)p_in.readObject();

		} catch (Exception exception) {
			ExceptionHandler.display(exception);

			p_in.close();

			throw new IOException(exception);
		}
	}

	public GLocale getLocale() {
		return m_locale;
	}

	public GLocaleID clone() {
		try {
			return new GLocaleID(m_locale);

		} catch(Exception exception) {
			ExceptionHandler.display(exception);
		}

		return new GLocaleID(null);
	}
}
