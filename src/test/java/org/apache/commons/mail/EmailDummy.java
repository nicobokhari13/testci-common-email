package org.apache.commons.mail;

public class EmailDummy extends Email {
	
	
	@Override
	public Email setMsg(String msg) throws EmailException {
		this.content = msg; 
		this.contentType = EmailConstants.TEXT_PLAIN;
		return this;
	}

}
