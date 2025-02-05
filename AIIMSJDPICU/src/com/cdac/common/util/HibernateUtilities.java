/*******************************************************************************
 * � 2018-2019 Infosys Limited, Bangalore, India. All Rights Reserved. 
 * Version: 1.0.0.0
 *
 * This Program is protected by copyright laws, international treaties and other pending or existing intellectual property rights in India, the United States and other countries. Except as expressly permitted, any unauthorized reproduction, storage, transmission in any form or by any means (including without limitation electronic, mechanical, printing, photocopying, recording or otherwise), or any distribution of this Program, or any portion of it, may result in severe civil and criminal penalties, and will be prosecuted to the maximum extent possible under the law. 
 *******************************************************************************/
package com.cdac.common.util;

import java.io.File;
import java.net.URLDecoder;
import java.util.logging.Logger;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.cdac.framework.SystemConfiguration;

/*import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;*/

// TODO: Auto-generated Javadoc
/**
 * The Class HibernateUtilities.
 */
public class HibernateUtilities {

	/** The Constant CONFIGURATION_LOCATION. */
	//private static final String CONFIGURATION_LOCATION = "hibernate-util/hibernate.cfg.xml";
	private static final String CONGIG_FILE_NAME = "hibernate.cfg.xml";
	private static final Logger LOGGER = Logger.getLogger(HibernateUtilities.class.getName());

	/** The session factory. */
    private static SessionFactory sessionFactory = null;

	/** The service registry. */
    private static ServiceRegistry serviceRegistry;

	/**
	 * Gets the session factory.
	 *
	 * @return the session factory
	 */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

	/**
	 * Creates the session factory.
	 *
	 * @return the session factory
	 * @throws Exception
	 *             the exception
	 */
    public synchronized static SessionFactory createSessionFactory() throws Exception {
		if (sessionFactory == null)
        {
            try {
				LOGGER.info("Logger Name: " + LOGGER.getName());

				// Step1 : Loading the configuration details from
				// hibernate.cfg.xml

				String path = SystemConfiguration.class.getProtectionDomain().getCodeSource().getLocation().toURI()
				        .getPath();
				String decodedPath = URLDecoder.decode(path, "UTF-8");

				if(sessionFactory==null)
				{
					File f = new File((new File(decodedPath)).getParentFile().getPath() + File.separator + "config"
					        + File.separator + CONGIG_FILE_NAME);

					Configuration configuration = new Configuration().configure(f);
					// Step2 : Creating ServiceRegistry using the
					// StandardServiceRegistryBuilder and Configuration defined in
					// Step 1

					serviceRegistry = new StandardServiceRegistryBuilder().configure(f).build();

					// Step3 : Creating the SessionFactory using the Configuration
					// and serviceRegistry.
					sessionFactory = configuration.buildSessionFactory(serviceRegistry);
				}

            } catch (Exception e) {
				throw e;
            }
        }
        return sessionFactory;
    }

	/**
	 * Close session factory.
	 */
    public static void closeSessionFactory()
    {
        if(sessionFactory!=null)
        {
            sessionFactory.close();

        }
    }

}
