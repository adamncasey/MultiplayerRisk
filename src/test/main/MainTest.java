package test.main;

import static org.junit.Assert.*;

import org.junit.Test;

import main.Main;

public class MainTest {

	@Test
	public void test() {
		assertEquals("Hello World! FALSE!", Main.test());
	}

}
