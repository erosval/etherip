/*******************************************************************************
 * Copyright (c) 2012 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package com.melfore.etherip.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;

import org.junit.jupiter.api.Test;

/** @author Kay Kasemir */
@SuppressWarnings("nls")
public class HexdumpTest {
	@Test
	public void hexTest() {
		final String string = Hexdump.toCompactHexdump(ByteBuffer.wrap("Hello!\n".getBytes()));
		System.out.println(string);
		assertEquals(string, "0000 - 48 65 6C 6C 6F 21 0A - Hello!.");
	}
}
