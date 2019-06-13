package com.fry.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * 俄罗斯方块游戏面板主界面*/

public class Tetris extends JPanel {
	private Tetromino tetromino;//正在下落方块
	private Tetromino nextOne;     //下一个下落的方块
	public static final int ROWS = 20;  //最多20行
	public static final int COLS = 10;  //10列消除
	private Cell[][] wall = new Cell[20][10];
	private int lines;//消除的行数
	private int score; //得到的分数
	public static final int CELL_SIZE = 26;
	private static Image background; //背景图片
	public static Image I;
	public static Image J;
	public static Image L;
	public static Image S;
	public static Image Z;
	public static Image O;
	public static Image T;
	public static final int FONT_COLOR = 6715289;
	public static final int FONT_SIZE = 32;

	static {
		try {
			background = ImageIO.read(Tetris.class.getResource("tetris.png"));
			T = ImageIO.read(Tetris.class.getResource("T.png"));
			I = ImageIO.read(Tetris.class.getResource("I.png"));
			S = ImageIO.read(Tetris.class.getResource("S.png"));
			Z = ImageIO.read(Tetris.class.getResource("Z.png"));
			L = ImageIO.read(Tetris.class.getResource("L.png"));
			J = ImageIO.read(Tetris.class.getResource("J.png"));
			O = ImageIO.read(Tetris.class.getResource("O.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void action() {
		startAction();
		repaint();
		KeyAdapter l = new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_Q) {
					System.exit(0);//键入q退出游戏
				}
				if (Tetris.this.gameOver) {
					if (key == 83) {
						Tetris.this.startAction();
					}
					return;
				}
				if (Tetris.this.pause) {
					if (key == 67) {
						Tetris.this.continueAction();
					}
					return;
				}
				switch (key) {
				case KeyEvent.VK_RIGHT:
					moveRightAction();
					break;
				case KeyEvent.VK_LEFT:
					moveLeftAction();//左右键进行移动
					break;
				case KeyEvent.VK_DOWN:
					softDropAction();//按下键就下落
					break;
				case KeyEvent.VK_UP:
					rotateRightAction();//上键向右旋转
					break;
			//	case KeyEvent.VK_Z:
				//	rotateLeftAction();//按Z会向左旋转
					//break;
				case KeyEvent.VK_SPACE:
					hardDropAction();//空格键就加速下落
					break;
				case KeyEvent.VK_P:
					pauseAction();//p键进行暂停
				}
				repaint();
			}
		};
		this.requestFocus();
		this.addKeyListener(l);
	}

	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);//添加背景图
		g.translate(15, 15); //平移绘图坐标系
		paintTetromino(g);//绘制下落的方块
		paintWall(g);//画墙
		paintNextOne(g);//下一个下落的方块
		paintScore(g);//成绩
	}

	private void paintScore(Graphics g) {//记录成绩
		Font f = getFont();       //获取当前面板的默认字体
		Font font = new Font(f.getName(), 1, 32);
		
		int x = 290;
		int y = 162;
		g.setColor(new Color(6715289));
		g.setFont(font);
		String str = "SCORE:" + this.score;
		g.drawString(str, x, y); //在指定位置显示成绩
		y += 56;
		str = "LINES:" + this.lines;
		g.drawString(str, x, y);  //指定位置显示消除的行数
		y += 56;
		str = "[P]Pause";
		if (this.pause) {
			str = "[C]Continue";
		}
		if (this.gameOver) {
			str = "[S]Start!";
		}
		g.drawString(str, x, y);
	}

	private void paintNextOne(Graphics g) {
		Cell[] cells = this.nextOne.getCells();
		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			int x = (c.getCol() + 10) * 26 - 1;//出现的时候的横坐标
			int y = (c.getRow() + 1) * 26 - 1;//纵坐标
			g.drawImage(c.getImage(), x, y, null);//画出方块
		}
	}

	private void paintTetromino(Graphics g) {
		Cell[] cells = this.tetromino.getCells();
		for (int i = 0; i < cells.length; i++) {
			Cell c = cells[i];
			int x = c.getCol() * 26 - 1;
			int y = c.getRow() * 26 - 1;

			g.drawImage(c.getImage(), x, y, null);
		}
	}

	private void paintWall(Graphics g) {
		for (int row = 0; row < this.wall.length; row++) {
			Cell[] line = this.wall[row];
			for (int col = 0; col < line.length; col++) {
				Cell cell = line[col];
				int x = col * 26;
				int y = row * 26;
				if (cell != null) {
					g.drawImage(cell.getImage(), x - 1, y - 1, null);
				}
			}
		}
	}

	public void softDropAction() {
		if (tetrominoCanDrop()) {
			this.tetromino.softDrop();
		} else {
			tetrominoLandToWall();
			destroyLines();
			checkGameOver();
			this.tetromino = this.nextOne;
			this.nextOne = Tetromino.randomTetromino();
			try {
				finalize();
			} catch (Throwable e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	public void destroyLines() {
		int lines = 0;
		for (int row = 0; row < this.wall.length; row++) {
			if (fullCells(row)) {
				deleteRow(row);
				lines++;
			}
		}
		this.lines += lines;
		this.score += SCORE_TABLE[lines];
	}

	private static final int[] SCORE_TABLE = { 0, 1, 10, 30, 200 };
	private boolean pause;
	private boolean gameOver;
	private Timer timer;

	public boolean fullCells(int row) {
		Cell[] line = this.wall[row];
		for (int i = 0; i < line.length; i++) {
			if (line[i] == null) {
				return false;
			}
		}
		return true;
	}

	public void deleteRow(int row) {
		for (int i = row; i >= 1; i--) {
			System.arraycopy(this.wall[(i - 1)], 0, this.wall[i], 0, 10);
		}
		Arrays.fill(this.wall[0], null);
	}

	public boolean tetrominoCanDrop() {//判断是否可以下落
		Cell[] cells = this.tetromino.getCells();
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			if (row == 19) {//最多只能有20行
				return false;
			}
		}
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			if (this.wall[(row + 1)][col] != null) {
				return false;
			}
		}
		return true;
	}

	public void tetrominoLandToWall() {
		Cell[] cells = this.tetromino.getCells();
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			this.wall[row][col] = cell;
		}
	}

	public void moveRightAction() {
		this.tetromino.moveRight();
		if ((outOfBound()) || (coincide())) {//进行判断，是否到达边界
			this.tetromino.moveLeft();
		}
	}

	public void moveLeftAction() {
		this.tetromino.moveLeft();
		if ((outOfBound()) || (coincide())) {//panduan
			this.tetromino.moveRight();
		}
	}
/*判断是否到达边界*/
	private boolean outOfBound() {
		Cell[] cells = this.tetromino.getCells();
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int col = cell.getCol();
			if ((col < 0) || (col >= 10)) {
				return true;
			}
		}
		return false;
	}
/**判断是否可以移动*/
	private boolean coincide() {
		Cell[] cells = this.tetromino.getCells();
		for (Cell cell : cells) {
			int row = cell.getRow();
			int col = cell.getCol();
			if ((row < 0) || (row >= 20) || (col < 0) || (col >= 10) || (this.wall[row][col] != null)) {
				return true;
			}
		}
		return false;
	}
/*
 * 
 * 方块的旋转实现*/
	public void rotateRightAction() {
		this.tetromino.rotateRight();
		if ((outOfBound()) || (coincide())) {//判断
			this.tetromino.rotateRight();
		}
	}

	/*public void rotateLeftAction() {
		this.tetromino.rotateLeft();
		if ((outOfBound()) || (coincide())) {//判断是否满足旋转的条件
			this.tetromino.rotateRight();
		}
	}
*/
	public void hardDropAction() {
		while (tetrominoCanDrop()) {
			this.tetromino.softDrop();
		}
		tetrominoLandToWall();
		destroyLines();
		checkGameOver();
		this.tetromino = this.nextOne;
		this.nextOne = Tetromino.randomTetromino();
	}

	public void startAction() {
		clearWall();
		this.tetromino = Tetromino.randomTetromino();
		this.nextOne = Tetromino.randomTetromino();
		this.lines = 0;
		this.score = 0;
		this.pause = false;
		this.gameOver = false;
		this.timer = new Timer();
		this.timer.schedule(new TimerTask() {
			public void run() {
				Tetris.this.softDropAction();
				Tetris.this.repaint();
			}
		}, 700L, 700L);
	}

	private void clearWall() {
		for (int row = 0; row < 20; row++) {
			Arrays.fill(this.wall[row], null);
		}
	}

	public void pauseAction() {
		this.timer.cancel();
		this.pause = true;
		repaint();
	}

	public void continueAction() {
		this.timer = new Timer();
		this.timer.schedule(new TimerTask() {
			public void run() {
				Tetris.this.softDropAction();
				Tetris.this.repaint();
			}
		}, 700L, 700L);
		this.pause = false;
		repaint();
	}

	public void checkGameOver() {
		if (this.wall[0][4] == null) {
			return;
		}
		this.gameOver = true;
		this.timer.cancel();
		repaint();
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Tetris tetris = new Tetris();
		frame.add(tetris);
		frame.setSize(525, 590);
		frame.setUndecorated(false);
		frame.setTitle("堂良方块大作战");
		frame.setDefaultCloseOperation(3);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		tetris.action();
	}
}
