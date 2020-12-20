package com.laithailibrary.clientlibrary.ui.support;

import java.math.*;
import java.util.*;

/**
 * Created by dumkrerng on 6/6/2558.
 */
public class GCalculator {

	public static final String OPERATOR = "+-*/";

	public static final Character BRACKET_BEGIN = '(';
	public static final Character BRACKET_END = ')';

	private static final int SCALE = 32;

	private static final String ERROR = "Error Statement!!!";

	private GCalculator() {}

	public static String calculate(String p_strStatement) {
		return calculate(p_strStatement, 6);
	}

	public static String calculate(String p_strStatement, int p_intScale) {
		p_strStatement = p_strStatement.replace(" ", "");

		String strResult = verifyStatement(p_strStatement);

		if(strResult.length() > 0) {
			return strResult;
		}

		String[] strElements = getElements(p_strStatement);

		strResult = calculate(strElements);

		if(isNumeric(strResult)) {
			BigDecimal bdValue = new BigDecimal(strResult);
			strResult = bdValue.setScale(p_intScale, BigDecimal.ROUND_HALF_UP).toString();
		}

		return strResult;
	}

	private static String verifyStatement(String p_strStatement) {
		String strResult = verifyBracket(p_strStatement);

		if(strResult.length() > 0) {
			return strResult;
		}

		return strResult;
	}

	private static String verifyBracket(String p_strStatement) {
		Stack<Character> stkBracket = new Stack<Character>();

		for(Character character : p_strStatement.toCharArray()) {
			if(character.compareTo(BRACKET_BEGIN) == 0) {
				stkBracket.push(character);

			} else if(character.compareTo(BRACKET_END) == 0) {
				Character charPop = stkBracket.pop();

				if(charPop == null) {
					return ERROR;
				}
			}
		}

		if(stkBracket.size() > 0) {
			return ERROR;
		}

		return "";
	}

	private static String[] getElements(String p_strStatement) {
		List<String> lsReturns = new ArrayList<String>();
		Character charStatement_Previous = null;
		String strElement = "";

		for(Character charStatement : p_strStatement.toCharArray()) {
			if(OPERATOR.contains(charStatement.toString())) {
				if(charStatement.compareTo('-') == 0) {
					boolean isOperator = true;

					if(charStatement_Previous != null) {
						if(OPERATOR.contains(charStatement_Previous.toString())
							|| charStatement_Previous.compareTo(BRACKET_BEGIN) == 0) {

							isOperator = false;
						}
					} else {
						isOperator = false;
					}

					if(isOperator) {
						if(strElement.length() > 0) {
							lsReturns.add(strElement);
							strElement = "";
						}

						lsReturns.add(charStatement.toString());

					} else {
						strElement += charStatement;
					}
				} else {
					if(strElement.length() > 0) {
						lsReturns.add(strElement);
						strElement = "";
					}

					lsReturns.add(charStatement.toString());
				}
			} else if(charStatement.compareTo(BRACKET_BEGIN) == 0) {
				if(strElement.length() > 0) {
					lsReturns.add(strElement);
					strElement = "";
				}

				lsReturns.add(BRACKET_BEGIN.toString());

			} else if(charStatement.compareTo(BRACKET_END) == 0) {
				if(strElement.length() > 0) {
					lsReturns.add(strElement);
					strElement = "";
				}

				lsReturns.add(BRACKET_END.toString());

			} else {
				strElement += charStatement;
			}

			charStatement_Previous = charStatement;
		}

		if(strElement.length() > 0) {
			lsReturns.add(strElement);
		}

		String[] strReturns = new String[lsReturns.size()];

		return lsReturns.toArray(strReturns);
	}

	private static String calculate(String[] p_strElements) {
		try {
			GStackValue stackValue = new GStackValue();
			GStackOperator stackOperator = new GStackOperator();

			for(String strElement : p_strElements) {
				if(isValue(strElement)) {
					stackValue.push(toBigDecimal(strElement));

				} else {
					stackOperator.push(strElement.charAt(0));
				}

				if(stackOperator.isBracketEnd_TopOfStack()) {
					String[] strSubElements = getSubElements(stackValue, stackOperator);

					String strValue = calculate(strSubElements);

					if(!isNumeric(strValue)) {
						return strValue;
					}

					stackValue.push(toBigDecimal(strValue));
				}

				if(isValue(strElement)
					&& !stackOperator.isEmpty()) {

					BigDecimal bdResult = null;

					if(stackOperator.isSubtraction_TopOfStack()) {
						stackOperator.pop();
						stackOperator.push('+');

						BigDecimal dbValue = stackValue.pop();
						bdResult = dbValue.multiply(BigDecimal.valueOf(-1));

					} else if(stackOperator.isMultiplication_TopOfStack()) {
						stackOperator.pop();

						BigDecimal bdValueB = stackValue.pop();
						BigDecimal bdValueA = stackValue.pop();

						BigDecimal bdValue = bdValueA.multiply(bdValueB);
						bdResult = bdValue.setScale(SCALE, BigDecimal.ROUND_HALF_UP);

					} else if(stackOperator.isDivision_TopOfStack()) {
						stackOperator.pop();

						BigDecimal bdValueB = stackValue.pop();
						BigDecimal bdValueA = stackValue.pop();

						bdResult = bdValueA.divide(bdValueB, SCALE, BigDecimal.ROUND_HALF_UP);
					}

					if(bdResult != null) {
						stackValue.push(bdResult);
					}
				}
			}

			if((stackValue.size() - 1) != stackOperator.size()) {
				return ERROR;
			}

			if(stackOperator.hasSubtraction()
				|| stackOperator.hasMultiplication()
				|| stackOperator.hasDivision()) {

				String[] strSubElements = getSubElements(stackValue, stackOperator);

				String strValue = calculate(strSubElements);

				if(!isNumeric(strValue)) {
					return strValue;
				}

				stackValue.push(toBigDecimal(strValue));
			}

			while(stackValue.size() > 1) {
				stackOperator.pop();

				BigDecimal bdValueB = stackValue.pop();
				BigDecimal bdValueA = stackValue.pop();

				BigDecimal bdValue = bdValueA.add(bdValueB);
				bdValue = bdValue.setScale(SCALE, BigDecimal.ROUND_HALF_UP);

				stackValue.push(bdValue);
			}

			if(stackOperator.size() > 0) {
				return ERROR;
			}

			BigDecimal bdResult = stackValue.pop();
			bdResult = bdResult.setScale(SCALE, BigDecimal.ROUND_HALF_UP);

			return bdResult.toString();

		} catch(NumberFormatException p_exception) {
			return ERROR;

		} catch(Exception p_exception) {
			String strErrorMessage = p_exception.getMessage();

			if(strErrorMessage.startsWith("Non-terminating")) {
				strErrorMessage = "Non-terminating";
			}

			return "Error: " + strErrorMessage;
		}
	}

	private static String[] getSubElements(GStackValue p_stackValue, GStackOperator p_stackOperator) {
		Stack<String> stackSubElements = new Stack<String>();

		if(p_stackValue.size() > 0
			&& p_stackOperator.size() > 0) {

			if(!p_stackOperator.isBracketEnd_TopOfStack()) {
				p_stackOperator.push(BRACKET_END);
			}

			Character charOperator = p_stackOperator.pop();

			while(charOperator.compareTo(BRACKET_BEGIN) != 0) {
				if(charOperator.compareTo(BRACKET_END) != 0) {
					stackSubElements.push(charOperator.toString());
				}

				BigDecimal bdValue = p_stackValue.pop();
				stackSubElements.push(bdValue.toString());

				if(p_stackOperator.isEmpty()) {
					charOperator = BRACKET_BEGIN;

				} else {
					charOperator = p_stackOperator.pop();
				}
			}
		}

		if(p_stackOperator.isBracketBegin_TopOfStack()) {
			p_stackOperator.pop();
		}

		String[] strSubElements = new String[stackSubElements.size()];
		int index = -1;

		while(!stackSubElements.isEmpty()) {
			index++;

			String strSubElement = stackSubElements.pop();
			strSubElements[index] = strSubElement;
		}

		return strSubElements;
	}

	private static boolean isValue(String p_strElement) {
		if(OPERATOR.contains(p_strElement)
			|| p_strElement.compareTo(BRACKET_BEGIN.toString()) == 0
			|| p_strElement.compareTo(BRACKET_END.toString()) == 0) {

			return false;
		}

		return true;
	}

	private static BigDecimal toBigDecimal(String p_strValue) {
		BigDecimal bdValue = new BigDecimal(p_strValue);
		bdValue = bdValue.setScale(SCALE, BigDecimal.ROUND_HALF_UP);

		return bdValue;
	}

	public static boolean isNumeric(String p_strValue) {
		try {
			Double.parseDouble(p_strValue);

		} catch(NumberFormatException exception) {
			return false;
		}

		return true;
	}
}
