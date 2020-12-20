package com.laithailibrary.sharelibrary.field.pfield;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/6/12
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PDataFieldPrimaryKeyID {
	String FieldName();
}
