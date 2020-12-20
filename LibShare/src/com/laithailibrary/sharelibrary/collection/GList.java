package com.laithailibrary.sharelibrary.collection;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 2/24/12
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class GList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 3;

	public GList() {}

	public GList(int p_intCapacity) {
		super(p_intCapacity);
	}

	public GList(E e) {
		add(e);
	}

	public GList(Collection<E> p_collection) {
		super(p_collection);
	}
}
