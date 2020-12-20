package com.laithailibrary.sharelibrary.field.pfield;

import java.lang.annotation.*;
import com.laithailibrary.sharelibrary.field.*;
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
public @interface PDataFieldUSERID {
	String FieldName();
	String FieldLabel() default "";
	int ColumnWidth() default 100;
	Alignment ColumnAlignment() default Alignment.Left;
	int DataSize() default DataField.DataSize_USERID;
	boolean Mandatory() default true;
	boolean Updateable() default true;
	boolean Index() default true;
}
