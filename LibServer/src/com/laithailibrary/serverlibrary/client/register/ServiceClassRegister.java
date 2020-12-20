package com.laithailibrary.serverlibrary.client.register;

import com.laithailibrary.sharelibrary.collection.*;
import com.laithailibrary.sharelibrary.interfaceclass.*;
import exc.*;
import java.util.*;

public class ServiceClassRegister {

	private static Map<String, Class<? extends InterfaceService>> m_mapClass_InterfaceClassName = new GMap<>();

	private static final long serialVersionUID = 1;

	private ServiceClassRegister() {}

	public static void register(Class<? extends InterfaceService> p_classInterface, Class p_class) throws GException {
		try {
			if(!p_classInterface.isInstance(p_class.newInstance())) {
				StringBuilder builder = new StringBuilder();
				builder.append("Class not implement interface!\n");
				builder.append("Class: ").append(p_class.getName()).append('\n');
				builder.append("Interface: ").append(p_classInterface.getName());

				throw new GException(builder.toString());
			}

			m_mapClass_InterfaceClassName.put(p_classInterface.getName(), p_class);

		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static Class<? extends InterfaceService> getClass(String p_strInterfaceClassName) throws GException {
		try {
			if(m_mapClass_InterfaceClassName.containsKey(p_strInterfaceClassName)) {
				return m_mapClass_InterfaceClassName.get(p_strInterfaceClassName);

			} else {
				throw new GException("Not Register " + p_strInterfaceClassName);
			}
		} catch (Exception exception) {
			ExceptionHandler.display(exception);
			throw new GException(exception);
		}
	}

	public static boolean containsClass(String p_strInterfaceClassName) throws GException {
		return m_mapClass_InterfaceClassName.containsKey(p_strInterfaceClassName);
	}

	public static Map<String, Class<? extends InterfaceService>> getMapData() throws GException {
		return m_mapClass_InterfaceClassName;
	}
}
