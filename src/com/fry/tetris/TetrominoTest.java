package com.fry.tetris;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fry.tetris.Tetromino.State;

public class TetrominoTest {
	
	private static Tetromino tetromino=new Tetromino();//创建对象



	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		tetromino.cells[0]=new Cell(2,4,Tetris.J);
		tetromino.cells[1]=new Cell(2,3,Tetris.J);
		tetromino.cells[2]=new Cell(2,5,Tetris.J);
		tetromino.cells[3]=new Cell(3,5,Tetris.J);
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSoftDrop() {
		//fail("尚未实现");
		tetromino.softDrop();
		assertEquals(3,tetromino.cells[0].getRow());
		assertEquals(3,tetromino.cells[1].getRow());
		assertEquals(3,tetromino.cells[2].getRow());
		assertEquals(4,tetromino.cells[3].getRow());
	}

	@Test
	public void testMoveRight() {
		//fail("尚未实现");
		tetromino.moveRight();
		assertEquals(5,tetromino.cells[0].getCol());
		assertEquals(4,tetromino.cells[1].getCol());
		assertEquals(6,tetromino.cells[2].getCol());
		assertEquals(6,tetromino.cells[3].getCol());
	}

	@Test
	public void testMoveLeft() {
		//fail("尚未实现");
		tetromino.moveLeft();
		assertEquals(3,tetromino.cells[0].getCol());
		assertEquals(2,tetromino.cells[1].getCol());
		assertEquals(4,tetromino.cells[2].getCol());
		assertEquals(4,tetromino.cells[3].getCol());
		
	}

	@Test
	public void testRotateRight() {

		//State  s = tetromino.states[1 ];
		
		tetromino.rotateRight();
	   
 		assertEquals(2,tetromino.cells[0].getRow());
		assertEquals(4,tetromino.cells[0].getCol());
		assertEquals(1,tetromino.cells[1].getRow());
		assertEquals(4,tetromino.cells[1].getCol());
		assertEquals(3,tetromino.cells[2].getRow());
		assertEquals(4,tetromino.cells[2].getCol());
		assertEquals(3,tetromino.cells[3].getRow());
		assertEquals(3,tetromino.cells[3].getCol());
		
	}

	
	
}
