package com.fry.tetris;

import java.util.Arrays;
import java.util.Random;

public class Tetromino
{
  protected Cell[] cells = new Cell[4];
  protected State[] states;
  public static  int type;
  public static Tetromino randomTetromino()
  {
    Random r = new Random();
    type = r.nextInt(7);
    switch (type)
    {
    case 0: 
      return new I();
    case 1: 
      return new I();
    case 2: 
      return new J();
    case 3: 
      return new L();
    case 4: 
      return new O();
    case 5: 
      return new S();
    case 6: 
      return new Z();
    }
    return null;
  }
  
  public Cell[] getCells()
  {
    return this.cells;
  }
  
  
  public void softDrop()
  {
    for (int i = 0; i < this.cells.length; i++) {
      this.cells[i].moveDown();
    }
  }
  
  public void moveRight()
  {
    for (int i = 0; i < this.cells.length; i++) {
      this.cells[i].moveRight();
    }
  }
  
  public void moveLeft()
  {
    for (int i = 0; i < this.cells.length; i++) {
      this.cells[i].moveLeft();
    }
  }
  
 int index = 100000;
  
  public void rotateRight()
  {
	  
	 int r1,r2,r3,c1,c2,c3;
	  r1=this.cells[0].getRow()-this.cells[0].getCol()+this.cells[1].getCol();
	  r2=this.cells[0].getRow()-this.cells[0].getCol()+this.cells[2].getCol();
	  r3=this.cells[0].getRow()-this.cells[0].getCol()+this.cells[3].getCol();
	  
	  c1=this.cells[0].getRow()+this.cells[0].getCol()-this.cells[1].getRow();
	  c2=this.cells[0].getRow()+this.cells[0].getCol()-this.cells[2].getRow();
	  c3=this.cells[0].getRow()+this.cells[0].getCol()-this.cells[3].getRow();
	  
	    this.cells[1].setRow(r1);
	    this.cells[1].setCol(c1);
	    this.cells[2].setRow(r2);
	    this.cells[2].setCol(c2);
	    this.cells[3].setRow(r3);
	    this.cells[3].setCol(c3);
	
	  
	  
	  
  /*  this.index += 1;
    
    State  s = this.states[(this.index % this.states.length)];
   
    Cell o = this.cells[0];
    
    
    
    this.cells[1].setRow(o.getRow() + s.row1);
    this.cells[1].setCol(o.getCol() + s.col1);
    this.cells[2].setRow(o.getRow() + s.row2);
    this.cells[2].setCol(o.getCol() + s.col2);
    this.cells[3].setRow(o.getRow() + s.row3);
    this.cells[3].setCol(o.getCol() + s.col3);
        */
  }
  
  /*public void rotateLeft()
  {
    this.index -= 1;
    
    State s = this.states[(this.index % this.states.length)];
    
    Cell o = this.cells[0];
    
    this.cells[1].setRow(o.getRow() + s.row1);
    this.cells[1].setCol(o.getCol() + s.col1);
    this.cells[2].setRow(o.getRow() + s.row2);
    this.cells[2].setCol(o.getCol() + s.col2);
    this.cells[3].setRow(o.getRow() + s.row3);
    this.cells[3].setCol(o.getCol() + s.col3);
    
  }*/
  
  public String toString()
  {
    return Arrays.toString(this.cells);
  }
  
  protected class State
  {
    int row0;
    int col0;
    int row1;
    int col1;
    int row2;
    int col2;
    int row3;
    int col3;
    
    public State(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3)
    {
      this.row0 = row0;
      this.col0 = col0;
      this.row1 = row1;
      this.col1 = col1;
      this.row2 = row2;
      this.col2 = col2;
      this.row3 = row3;
      this.col3 = col3;
    }

  }
}//ÀàµÄ½áÊø

class T extends Tetromino {
	public T() {
		this.cells[0] = new Cell(0, 4, Tetris.T);
		this.cells[1] = new Cell(0, 3, Tetris.T);
		this.cells[2] = new Cell(0, 5, Tetris.T);
		this.cells[3] = new Cell(1, 4, Tetris.T);
		this.states = new Tetromino.State[] {
				new Tetromino.State(0, 0, 0, -1, 0, 1, 1, 0),
				new Tetromino.State(0, 0, -1, 0, 1, 0, 0, -1),
				new Tetromino.State(0, 0, 0, 1, 0, -1, -1, 0),
				new Tetromino.State(0, 0, 1, 0, -1, 0, 0, 1) };
	}
}

class L extends Tetromino {
	public L() {
		this.cells[0] = new Cell(0, 4, Tetris.L);
		this.cells[1] = new Cell(0, 3, Tetris.L);
		this.cells[2] = new Cell(0, 5, Tetris.L);
		this.cells[3] = new Cell(1, 3, Tetris.L);
		this.states = new Tetromino.State[] {
				new Tetromino.State(0, 0, 0, -1, 0, 1, 1, -1),
				new Tetromino.State(0, 0, -1, 0, 1, 0, -1, -1), 
				new Tetromino.State(0, 0, 0, 1, 0, -1, -1, 1),
				new Tetromino.State(0, 0, 1, 0, -1, 0, 1, 1) };
	}
}


class I extends Tetromino {
	public I() {
		this.cells[0] = new Cell(0, 4, Tetris.I);
		this.cells[1] = new Cell(0, 3, Tetris.I);
		this.cells[2] = new Cell(0, 5, Tetris.I);
		this.cells[3] = new Cell(0, 6, Tetris.I);
		this.states = new Tetromino.State[] {
				new Tetromino.State(0, 0, 0, 1, 0, -1, 0, -2),
				new Tetromino.State(0, 0, -1, 0, 1, 0, 2, 0) };
	}
}

class S extends Tetromino {
	public S() {
		this.cells[0] = new Cell(0, 4, Tetris.S);
		this.cells[1] = new Cell(0, 5, Tetris.S);
		this.cells[2] = new Cell(1, 3, Tetris.S);
		this.cells[3] = new Cell(1, 4, Tetris.S);
		this.states = new Tetromino.State[] { new Tetromino.State(0, 0, 0, 1, 1, -1, 1, 0),
				new Tetromino.State(0, 0, -1, 0, 1, 1, 0, 1) };
	}
}

class Z extends Tetromino {
	public Z() {
		this.cells[0] = new Cell(1, 4, Tetris.Z);
		this.cells[1] = new Cell(0, 3, Tetris.Z);
		this.cells[2] = new Cell(0, 4, Tetris.Z);
		this.cells[3] = new Cell(1, 5, Tetris.Z);
		this.states = new Tetromino.State[] { new Tetromino.State(0, 0, -1, -1, -1, 0, 0, 1),
				new Tetromino.State(0, 0, -1, 1, 0, 1, 1, 0) };
	}
}

class O extends Tetromino {
	public O() {
		this.cells[0] = new Cell(0, 4, Tetris.O);
		this.cells[1] = new Cell(0, 5, Tetris.O);
		this.cells[2] = new Cell(1, 4, Tetris.O);
		this.cells[3] = new Cell(1, 5, Tetris.O);
		this.states = new Tetromino.State[] { new Tetromino.State(0, 0, 0, 1, 1, 0, 1, 1),
				new Tetromino.State(0, 0, 0, 1, 1, 0, 1, 1) };
	}
}

class J extends Tetromino {
	public J() {
		this.cells[0] = new Cell(0, 4, Tetris.J);
		this.cells[1] = new Cell(0, 3, Tetris.J);
		this.cells[2] = new Cell(0, 5, Tetris.J);
		this.cells[3] = new Cell(1, 5, Tetris.J);
		this.states = new Tetromino.State[] { 
				new Tetromino.State(0, 0, 0, -1, 0, 1, 1, 1),
				new Tetromino.State(0, 0, -1, 0, 1, 0, 1, -1),
				new Tetromino.State(0, 0, 0, 1, 0, -1, -1, -1),
				new Tetromino.State(0, 0, 1, 0, -1, 0, -1, 1) };
	}
}

