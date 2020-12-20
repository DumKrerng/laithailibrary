package com.laithailibrary.clientlibrary.ui.support;

import java.util.*;

/**
 * Created by dumkrerng on 13/2/2559.
 */
public class GStackOperator extends Stack<Character> {

	private static final long serialVersionUID = 1;

	public GStackOperator() {}

	public boolean hasSubtraction() {
		if(size() > 0) {
			if(this.contains('-')) {
				return true;
			}
		}

		return false;
	}

	public boolean hasMultiplication() {
		if(size() > 0) {
			if(this.contains('*')) {
				return true;
			}
		}

		return false;
	}

	public boolean hasDivision() {
		if(size() > 0) {
			if(this.contains('/')) {
				return true;
			}
		}

		return false;
	}

	public boolean isAddition_TopOfStack() {
		if(size() > 0) {
			Character character = peek();

			if(character.compareTo('+') == 0) {
				return true;
			}
		}

		return false;
	}

	public boolean isSubtraction_TopOfStack() {
		if(size() > 0) {
			Character character = peek();

			if(character.compareTo('-') == 0) {
				return true;
			}
		}

		return false;
	}

	public boolean isMultiplication_TopOfStack() {
		if(size() > 0) {
			Character character = peek();

			if(character.compareTo('*') == 0) {
				return true;
			}
		}

		return false;
	}

	public boolean isDivision_TopOfStack() {
		if(size() > 0) {
			Character character = peek();

			if(character.compareTo('/') == 0) {
				return true;
			}
		}

		return false;
	}

	public boolean isBracketBegin_TopOfStack() {
		if(size() > 0) {
			Character character = peek();

			if(character.compareTo('(') == 0) {
				return true;
			}
		}

		return false;
	}

	public boolean isBracketEnd_TopOfStack() {
		if(size() > 0) {
			Character character = peek();

			if(character.compareTo(')') == 0) {
				return true;
			}
		}

		return false;
	}
}
