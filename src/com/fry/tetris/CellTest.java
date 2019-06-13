package com.fry.tetris;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CellTest {
	private static Cell cell=new Cell();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		cell.setCol(5);
		cell.setRow(5);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMoveRight() {
		//fail("尚未实现");
		cell.moveRight();
		assertEquals(6,cell.getCol());
	}

	@Test
	public void testMoveLeft() {
		//fail("尚未实现");
		cell.moveLeft();
		assertEquals(4,cell.getCol());
	}

	@Test
	public void testMoveDown() {
		cell.moveDown();
		//fail("尚未实现");
		assertEquals(6,cell.getRow());
	}

}
