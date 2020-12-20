package com.laithailibrary.sharelibrary.sqlstatement.support;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 7/1/12
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public enum SQLOperator {
	NullOperator,
	AND,
	OR,

	Equal,
	NotEqual,

	In,
	InWithNull,
	NotIn,
	NotInWithNull,

	GreaterThan,
	GreaterThanOrEqualTo,

	LessThan,
	LessThanOrEqualTo,

	IsNull,
	IsNotNull,

	StartWith,
	EndWith,
	Like2,

	Equal_IgnoreCase,
	Like2_IgnoreCase
}
