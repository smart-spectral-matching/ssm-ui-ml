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
	
	@Value("${fuseki.host}")
	private String fusekiHost;
	
	public String getDatabaseHost() {
		return databaseHost;
	}

	public String getFusekiHost() {
		return fusekiHost;
	}
}
