package gov.ornl.ssm.ml.ui;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Holder for the spring-boot configuration information taken from the
 * application.properties file
 * 
 * @author Robert Smith
 *
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties
public class UIConfiguration {
	
	@Value("${database.host}")
	private String databaseHost;
	
	@Value("${database.port}")
	private String databasePort;
	
	@Value("${database.name}")
	private String databaseName;
	
	@Value("${database.user}")
	private String databaseUser;
	
	@Value("${database.password}")
	private String databasePassword;
	
	@Value("${collection.name}")
	private String collectionName;
	
	@Value("${fuseki.host}")
	private String fusekiHost;
	
	public String getDatabaseHost() {
		return databaseHost;
	}
	
	public String getDatabasePort() {
		return databasePort;
	}
	
	public String getDatabaseName() {
		return databaseName;
	}
	
	public String getDatabaseUser() {
		return databaseUser;
	}
	
	public String getDatabasePassword() {
		return databasePassword;
	}

	public String getCollectionName() {
		return collectionName;
	}
	
	public String getFusekiHost() {
		return fusekiHost;
	}
}
