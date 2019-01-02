/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.melfore.etherip.protocol;

import static com.melfore.etherip.types.CNPath.Identity;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.melfore.etherip.TestSettings;
import com.melfore.etherip.protocol.Encapsulation.Command;
import com.melfore.etherip.types.CNService;
import com.melfore.etherip.util.Hexdump;

/** @author Kay Kasemir */
@SuppressWarnings("nls")
public class SendRRDataTest {
	final private ByteBuffer buf = TestSettings.getBuffer();

	@BeforeEach
	public void setup() {
		TestSettings.logAll();
	}

	@Test
	public void testSendRRData() throws Exception {
		final MessageRouterProtocol pdu = new MessageRouterProtocol(CNService.Get_Attribute_Single, Identity().attr(7),
				new ProtocolAdapter());
		final SendRRDataProtocol rr_data = new SendRRDataProtocol(pdu);
		final Encapsulation encap = new Encapsulation(Command.SendRRData, 0x12027100, rr_data);
		final StringBuilder log = new StringBuilder();
		encap.encode(this.buf, log);
		System.out.println(log.toString());

		this.buf.flip();
		final String hex = Hexdump.toHexdump(this.buf);
		System.out.println(hex);
		assertEquals(hex,
				"0000 - 6F 00 18 00 00 71 02 12 00 00 00 00 46 75 6E 73 - o....q......Funs\n0010 - 74 75 66 66 00 00 00 00 00 00 00 00 00 00 02 00 - tuff............\n0020 - 00 00 00 00 B2 00 08 00 0E 03 20 01 24 01 30 07 - .......... .$.0.\n");
	}
}
