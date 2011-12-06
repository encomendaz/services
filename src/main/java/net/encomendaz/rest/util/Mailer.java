package net.encomendaz.rest.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsync;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClient;

public class Mailer {

	public static AmazonSimpleEmailServiceAsync client() {
		String accessKey = Configuration.getInstance().getAwsAccessKey();
		String secretKey = Configuration.getInstance().getAwsSecretKey();

		AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		return new AmazonSimpleEmailServiceAsyncClient(credentials);
	}
}
