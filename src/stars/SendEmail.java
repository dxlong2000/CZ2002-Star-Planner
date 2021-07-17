// package stars;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

/**
 * Sends email to a user.
 * @author Group7
 *
 */
public class SendEmail {
	/**
	 * Function to notify a waitlisted student that they have been allocated to a course by email.
	 * @param email
	 * @param courseCode
	 * @param indexNo
	 */
	public static void notify(String email, String courseCode, String indexNo){
		final String username = "regis.course.information.ntu@gmail.com"; // to be added
		final String password = "funnygroup123"; // to be added

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("regis.course.information.ntu@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(email)); // to be added an email addr
			message.setSubject("Course Registration Information");
			message.setText("Dear student,"
					+ "\n\n"+ String.format("We are pleased to inform you that you have been allocated the course %s to index %s!", courseCode, indexNo) 
				+ "\n\n Best regards,"
				+ "\n Course Management Team");

			Transport.send(message);

			// System.out.println("The email was sent to inform course ");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}