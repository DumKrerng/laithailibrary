package com.laithailibrary.sharelibrary.field.pfield;

import java.lang.annotation.*;
import com.laithailibrary.sharelibrary.support.*;

/**
 * Created with IntelliJ IDEA.
 * User: dumkrerng
 * Date: 11/6/12
 * Time: 11:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface PDataFieldDocumentUSERID {
	String FieldName();
	String FieldLabel() default "Document ID";
	int ColumnWidth() default 100;
	Alignment ColumnAlignment() default Alignment.Left;
	int DataSize() default 50;
}
