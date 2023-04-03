package org.apache.commons.mail;

import static org.apache.commons.mail.EmailConstants.MAIL_HOST;
import static org.junit.Assert.*;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

// Author: Nico Bokhari
// Date Completed: March 19th, 2023

public class EmailTest {
	
	private static final String[] TEST_EMAIL_LIST = {"abc@def.com", "nico@bokhari.com", "test@junit.com"};
	private static final String TEST_EMAIL = "nico@bokhari.com";
	private static final String TEST_NAME = "TestDev";
	private static final String TEST_VALUE = "Nico Bokhari";
	private static final String DUMMY_MSG = "Dummy Default Message";
	private static final int TEST_TIMEOUT_VAL = 1000;
	private static final int TEST_YEAR = 2002;
	private static final int TEST_MONTH = 0;
	private static final int TEST_DAY = 21;
	private static final int TEST_HR = 6;
	private static final int TEST_MIN = 36;
	private static final int TEST_SEC = 21;
	
	private Email email; 
	@Before
	public void setUpEmailTest() {
		email = new EmailDummy();
	}
	@After
	public void tearDownEmailTest() {
		
	}

	@Test
	public void testAddBcc() throws Exception{
		//This tests the addBcc(String...emails) method 
		//It tests whether the addBcc() correctly adds all the emails in TEST_EMAIL_LIST
		email.addBcc(TEST_EMAIL_LIST);
		assertEquals(TEST_EMAIL_LIST.length, email.getBccAddresses().size());

	}
	
	@Test(expected = EmailException.class)
	public void testAddBccWithNull() throws Exception{
		//This tests the addBcc(String...emails) method 
		//It tests whether the addBcc() correctly throws an EmailException when given a null argument
		String[] nullEmails = null;
		email.addBcc(nullEmails);
	}
	
	@Test(expected = EmailException.class)
	public void testAddBccWithEmptyList() throws Exception{
		//This tests the addBcc(String...emails) method 
		//It tests whether the addBcc() correctly throws an EmailException when given an empty String array
		String[] noEmails = {};
		email.addBcc(noEmails);
	}
	
	@Test
	public void testAddCc() throws Exception{
		//This tests the addCc(String email) method 
		//It tests whether the addCc() correctly adds TEST_EMAIL to the Email object
		email.addCc(TEST_EMAIL);
		assertEquals(TEST_EMAIL, email.getCcAddresses().get(0).getAddress());
	}

	@Test 
	public void testAddHeader(){
		//This tests the addHeader(String name, String value) method 
		//It tests whether the addHeader() correctly adds the key value pair "TestDev", "Nico Bokhari" to the header
		email.addHeader(TEST_NAME, TEST_VALUE);
		String result = email.headers.get(TEST_NAME);
		assertEquals(TEST_VALUE, result);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddHeaderEmptyName() {
		//This tests the addHeader(String name, String value) method 
		// It tests whether the addHeader() correctly throws an IllegalArgumentException for an empty Name key
		email.addHeader("", TEST_VALUE);
		
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testAddHeaderEmptyValue() {
		//This tests the addHeader(String name, String value) method 
		// It tests whether the addHeader() correctly throws an IllegalArgumentException for an empty value
		email.addHeader(TEST_NAME, "");
	}
	
	@Test
	public void testAddReplyTo() throws Exception{
		//This tests the addReplyto(String email, String name) method
		//It tests whether the addReplyTo() correctly adds the email to the replyList
		email.addReplyTo(TEST_EMAIL, TEST_NAME);
		boolean hasEmailAndName = false; 
		InternetAddress firstReply = email.replyList.get(0);
		if(firstReply.getAddress().equals(TEST_EMAIL) && firstReply.getPersonal().equals(TEST_NAME)) {
			hasEmailAndName = true; 
		}
		assertTrue(hasEmailAndName);	
	}
	
	@Test 
	public void testGetHostNameNoSession() {
		//This tests the getHostName() method
		//It tests whether the getHostName() correctly returns hostname TEST_NAME
		email.setHostName(TEST_NAME);
		assertEquals(TEST_NAME, email.getHostName());
	}
	
	@Test
	public void testGetHostNameWithSession() {
		//This tests the getHostName() method
		//It tests whether the getHostName() correctly returns hostname TEST_NAME, which is set by the Session's properties
		Properties props = new Properties();
		props.setProperty(MAIL_HOST, TEST_NAME);
		Session session = Session.getInstance(props);
		email.setMailSession(session);
		assertEquals(TEST_NAME, email.getHostName());
	}
	@Test
	public void testGetHostNameWithEmptyString() {
		//This tests the getHostName() method
		//It tests whether the getHostName() returns null when the hostname is set to an empty string
		email.setHostName("");
		assertEquals(null, email.getHostName());
	}
	@Test
	public void testGetMailSession() throws Exception{
		//This tests the getMailSession() method
		//It tests whether the session created and set is returned
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props);
		email.setMailSession(session);
		Session result = email.getMailSession();
		assertEquals(session, result);
	}
	@Test
	public void testGetMailSessionWithHostName() throws Exception{
		//This tests the getMailSession() method
		//It tests whether the Session returned was created with the preset hostname TEST_NAME
		email.setHostName(TEST_NAME);
		Session result = email.getMailSession();
		assertEquals(email.getHostName(), result.getProperty(MAIL_HOST));
	}
	@Test (expected = EmailException.class)
	public void testGetMailSessionNullHostName() throws Exception{
		//This tests the getMailSession() method
		//It tests whether the method correctly throws an EmailException when it tries to get a Session with a null hostname
		email.setHostName(null);
		Session result = email.getMailSession();
	}
	@Test
	public void testSetFrom() throws Exception{
		//This tests the setFrom() method
		//It tests whether the method sets the "From" address to TEST_EMAIL
		email.setFrom(TEST_EMAIL);
		String resultFrom = email.getFromAddress().getAddress();
		assertEquals(TEST_EMAIL, resultFrom);
	}
	@Test (expected = EmailException.class)
	public void testSetFromWithEmptyFromField() throws Exception{
		//This tests the setFrom() method
		//It tests whether the method throws an EmailException for an empty From email
		email.setFrom("");
	}
	@Test
	public void testGetSocketConncetionTimeout() throws Exception{
		//This tests the getSocketConnectionTimeout() method
		//It tests whether the method returns the set timeout value TEST_TIMEOUT_VAL 
		email.setSocketConnectionTimeout(TEST_TIMEOUT_VAL);
		int result = email.getSocketConnectionTimeout();
		assertEquals(TEST_TIMEOUT_VAL, result);
	}
	@Test
	public void testGetSentDate() throws Exception{
		//This tests the getSentDate() method
		//It tests whether the returned Date is the current Date/Time
		Date result = email.getSentDate();
		Date expected = new Date();
		assertEquals(expected, result);
	}
	@Test
	public void testGetSentDateWithTestDate() throws Exception{
		// This tests the getSentDate() method
		//It tests whether the returned Date is the Date defined by TEST_YEAR, TEST_MONTH .. and TEST_SEC
		Date expected = new Date(TEST_YEAR, TEST_MONTH, TEST_DAY, TEST_HR, TEST_MIN, TEST_SEC);
		email.setSentDate(expected);
		Date result = email.getSentDate();
		assertEquals(expected, result);
	}
	public void startEmailMessage() {
		//This method is used to create a session with hostname TEST_NAME
		//It is used to for each testcase that tests buildMimeMessage() method
		
		//Create a new session with a specific hostname	
		Properties props = new Properties();
		props.setProperty(MAIL_HOST, TEST_NAME);
		Session session = Session.getInstance(props);
		//set email Session to Session with properties 
		email.setMailSession(session);
	}
	@Test (expected = EmailException.class)
	public void testBuildMimeMessageNoFromEmail() throws Exception{
		// This tests  the buildMimeMessage() method
		// it tests whether the Mime Message throws Email Exception when no "From" email is defined
		
		startEmailMessage();
		
		//Run method
		email.buildMimeMessage();
	}
	@Test (expected = EmailException.class)
	public void testBuildMimeMessageNoToEmail() throws Exception{
		// This tests  the buildMimeMessage() method
		// it tests whether the Mime Message throws Email Exception when no "To" email is defined
		
		startEmailMessage();
		
		//Set From Email
		email.setFrom(TEST_EMAIL);
		
		//Run method
		email.buildMimeMessage();
	}
	@Test 
	public void testBuildMimeMessageWithTestDate() throws Exception{
		// This tests the buildMimeMessage() method
		// it tests whether the Mime Message correctly sets the message Date to the Date defined by TEST_YEAR, TEST_MONTH...TEST_SEC
		
		startEmailMessage();
		
		//Set From Email
		email.setFrom(TEST_EMAIL);
		//Set To Email
		email.addTo(TEST_EMAIL);
		
		//Set SentDate
		Date expected = new Date(TEST_YEAR, TEST_MONTH, TEST_DAY, TEST_HR, TEST_MIN, TEST_SEC);
		email.setSentDate(expected);
		
		//Run method
		email.buildMimeMessage();

		//System.out.println(email.headers);
		Date result = email.message.getSentDate();
		assertEquals(expected, result);
	}
	@Test
	public void testBuildMimeMessageWithCCEmail() throws Exception{
		// This tests  the buildMimeMessage() method
		// it tests whether the Mime Message correctly adds TEST_EMAIL to the message's CC List
		
		startEmailMessage();
		
		//Set From Email
		email.setFrom(TEST_EMAIL);
		//Set To Email
		email.addTo(TEST_EMAIL);
		
		//Add CC Email
		email.addCc(TEST_EMAIL);
		
		//Run method
		email.buildMimeMessage();
		
		//Get CC List
		InternetAddress[] CCList = (InternetAddress[]) email.message.getRecipients(Message.RecipientType.CC);
		//Get String CC Email address
		String result = CCList[0].getAddress();
		
		assertEquals(TEST_EMAIL, result);
	}
	@Test
	public void testBuildMimeMessageWithBCCEmail() throws Exception{
		// This tests the buildMimeMessage() method
		// it tests whether the Mime Message correctly adds TEST_EMAIL to the message's BCC List
		
		startEmailMessage();
		
		//Set From Email
		email.setFrom(TEST_EMAIL);
		//Set To Email
		email.addTo(TEST_EMAIL);
		
		//Add BCC Email
		email.addBcc(TEST_EMAIL);
		
		//Run method
		email.buildMimeMessage();
		
		//Get CC List
		InternetAddress[] CCList = (InternetAddress[]) email.message.getRecipients(Message.RecipientType.BCC);
		//Get String CC Email address
		String result = CCList[0].getAddress();
		
		assertEquals(TEST_EMAIL, result);
	}
	@Test
	public void testBuildMimeMessageWithReplyToEmail() throws Exception{
		// This tests the buildMimeMessage() method
		// it tests whether the Mime Message correctly adds TEST_EMAIL to the message's ReplyTo List
		
		startEmailMessage();
		
		//Set From Email
		email.setFrom(TEST_EMAIL);
		//Set To Email
		email.addTo(TEST_EMAIL);
		
		//Add ReplyTo Email
		email.addReplyTo(TEST_EMAIL);
		
		//Run method
		email.buildMimeMessage();
		
		//Get ReplyTo List
		InternetAddress[] replyToList = (InternetAddress[]) email.message.getReplyTo();
		//Get String ReplyTo Email address
		String result = replyToList[0].getAddress();
		
		assertEquals(TEST_EMAIL, result);
	}
	@Test
	public void testBuildMimeMessageWithHeader() throws Exception{
		// This tests the buildMimeMessage() method
		// it tests whether the Mime Message correctly sets a Header key, value pair to TEST_NAME, TEST_VALUE
		
		startEmailMessage();
		
		//Set From Email
		email.setFrom(TEST_EMAIL);
		//Set To Email
		email.addTo(TEST_EMAIL);
		
		//Add Header Value
		email.addHeader(TEST_NAME, TEST_VALUE);
		
		//Run method
		email.buildMimeMessage();
		
		//Get Header list
		String[] headers = email.message.getHeader(TEST_NAME);
		
		//Get String with value
		String result = headers[0];
		
		assertEquals(TEST_VALUE, result);
	}
	@Test
	public void testBuildMimeMessageWithSubject() throws Exception{
		// This tests the buildMimeMessage() method
		// it tests whether the Mime Message correctly sets the message subject to TEST_VALUE
		
		startEmailMessage();
		
		//Set From Email
		email.setFrom(TEST_EMAIL);
		//Set To Email
		email.addTo(TEST_EMAIL);
		
		//Add Subject to email
		email.setSubject(TEST_VALUE);
		
		//Run method
		email.buildMimeMessage();
		
		//Get Message subject
		String result = email.message.getSubject();
		
		assertEquals(TEST_VALUE, result);
	}
	@Test
	public void testBuildMimeMessageWithContent() throws Exception{
		// This tests the buildMimeMessage() method
		// it tests whether the Mime Message correctly sets the message content to DUMMY_MSG
			//setMsg defined in EmailDummy.java
		
		startEmailMessage();
		
		//Set From Email
		email.setFrom(TEST_EMAIL);
		//Set To Email
		email.addTo(TEST_EMAIL);
		
		//Add Content to email, and set ContentType to TEXT_PLAIN
		email = email.setMsg(DUMMY_MSG);
		
		//Run method
		email.buildMimeMessage();
		
		//Get Message content, and cast to String
		String result = (String) email.message.getContent();
		
		assertEquals(DUMMY_MSG, result);
	}
}
