/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.melfore.etherip.protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.melfore.etherip.TestSettings;
import com.melfore.etherip.util.Hexdump;

/** @author Kay Kasemir */
@SuppressWarnings("nls")
public class EncapsulationTest {
	private int body_size = 0;

	@BeforeEach
	public void setup() {
		TestSettings.logAll();
	}

	@Test
	public void testCommand() {
		assertEquals(Encapsulation.Command.ListServices.toString(), "ListServices (0x0004)");
	}

	@Test
	public void testEncode() throws Exception {
		final ByteBuffer send = ByteBuffer.allocate(100);
		send.order(Connection.BYTE_ORDER);

		final Encapsulation encap = new Encapsulation(Encapsulation.Command.ListInterfaces, 0, new ProtocolAdapter() {
			@Override
			public int getRequestSize() {
				return 42;
			}

			@Override
			public void decode(final ByteBuffer buf, final int available, final StringBuilder log) throws Exception {
				EncapsulationTest.this.body_size = available;
			}
		});
		encap.encode(send, null);
		assertEquals(send.position(), Encapsulation.ENCAPSULATION_HEADER_SIZE);
		send.flip();

		final String string = Hexdump.toCompactHexdump(send);
		System.out.println(string);
		assertEquals(string,
				"0000 - 64 00 2A 00 00 00 00 00 00 00 00 00 46 75 6E 73 - d.*.........Funs\n0010 - 74 75 66 66 00 00 00 00 - tuff....");

		ByteBuffer receive = ByteBuffer.allocate(100);
		receive.order(Connection.BYTE_ORDER);

		// On empty buffer, we need at least the encapsulation header
		assertEquals(encap.getResponseSize(receive), 24);

		// Use what was sent as the response
		receive = send;
		receive.position(receive.limit());
		receive.limit(receive.capacity());
		// Now that there's a length in the header, required response includes that
		assertEquals(encap.getResponseSize(receive), 24 + 42);

		// Decode
		receive.flip();
		// Should detect that message is too short
		try {
			encap.decode(receive, receive.remaining(), null);
			fail("Didn't detect that encapsulation had no body");
		} catch (final Exception ex) {
			System.err.println("Caught " + ex.getMessage());
			assertTrue(ex.getMessage().contains("Need"));
		}
		// Add remaining bytes (not decoded by Encapsulation)
		receive.position(receive.limit());
		receive.limit(receive.capacity());
		for (int i = 0; i < 42; ++i) {
			receive.put((byte) i);
		}

		receive.flip();
		encap.decode(receive, receive.remaining(), null);

		assertEquals(this.body_size, 42);
	}
}
