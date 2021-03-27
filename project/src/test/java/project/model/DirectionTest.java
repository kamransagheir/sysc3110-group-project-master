package project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class DirectionTest {

	@Test
	void testLeft() {
		assertEquals("LEFT", Direction.LEFT.name(),
				"should have a left field");
	}
	
	@Test
	void testRight() {
		assertEquals("RIGHT", Direction.RIGHT.name(),
				"should have a right field");
	}
	
	@Test
	void testUp() {
		assertEquals("UP", Direction.UP.name(),
				"should have an up field");
	}
	
	@Test
	void testDown() {
		assertEquals("DOWN", Direction.DOWN.name(),
				"should have a down field");
	}

}
