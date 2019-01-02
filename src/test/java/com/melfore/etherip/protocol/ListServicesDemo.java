/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.melfore.etherip.protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.melfore.etherip.EtherNetIP;
import com.melfore.etherip.TestSettings;
import com.melfore.etherip.protocol.ListServicesProtocol.Service;

/**
 * JUnit demo of {@link ListServices}
 *
 * @author Kay Kasemir, László Pataki
 */
@SuppressWarnings("nls")
public class ListServicesDemo {
	@Test
	public void testListServices() throws Exception {
		TestSettings.logAll();

		try (EtherNetIP etherNetIP = new EtherNetIP(TestSettings.get("plc"), TestSettings.getInt("slot"));) {
			etherNetIP.connectTcp();

			final Service[] services = etherNetIP.listServices();
			assertNotNull(services);

			// In principle, multiple services could be supported.
			// So far have only seen this one
			assertEquals(services.length, 1);
			assertEquals(services[0].getName(), "Communications..");
		}
	}
}
