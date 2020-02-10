package root.demo.model;

import java.io.Serializable;

public class FormMultiple implements Serializable{
	
	String fieldId;
	Object fieldValue;
	
	
	
	public FormMultiple() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public FormMultiple(String fieldId, Object fieldValue) {
		super();
		this.fieldId = fieldId;
		this.fieldValue = fieldValue;
	}

	

	@Override
	public String toString() {
		return "FormMultiple [fieldId=" + fieldId + ", fieldValue=" + fieldValue + "]";
	}


	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public Object getFieldValue() {
		return fieldValue;
	}
	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}
	
	
}
