package com.laithailibrary.sharelibrary.collection;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: dumkrerng
 * Date: 2/24/12
 * Time: 1:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class GSet<E> extends TreeSet<E> {

	private static final long serialVersionUID = 5;

	public GSet() {}

	public GSet(E e) {
		add(e);
	}

	public GSet(Collection<E> p_collection) {
		super(p_collection);
	}
}
