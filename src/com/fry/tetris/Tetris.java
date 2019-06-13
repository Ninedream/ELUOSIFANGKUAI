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
 * ����˹������Ϸ���������*/

public class Tetris extends JPanel {
	private Tetromino tetromino;//�������䷽��
	private Tetromino nextOne;     //��һ������ķ���
	public static final int ROWS = 20;  //���20��
	public static final int COLS = 10;  //10������
	private Cell[][] wall = new Cell[20][10];
	private int lines;//����������
	private int score; //�õ��ķ���
	public static final int CELL_SIZE = 26;
	private static Image background; //����ͼƬ
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
					System.exit(0);//����q�˳���Ϸ
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
					moveLeftAction();//���Ҽ������ƶ�
					break;
				case KeyEvent.VK_DOWN:
					softDropAction();//���¼�������
					break;
				case KeyEvent.VK_UP:
					rotateRightAction();//�ϼ�������ת
					break;
			//	case KeyEvent.VK_Z:
				//	rotateLeftAction();//��Z��������ת
					//break;
				case KeyEvent.VK_SPACE:
					hardDropAction();//�ո���ͼ�������
					break;
				case KeyEvent.VK_P:
					pauseAction();//p��������ͣ
				}
				repaint();
			}
		};
		this.requestFocus();
		this.addKeyListener(l);
	}

	public void paint(Graphics g) {
		g.drawImage(background, 0, 0, null);//��ӱ���ͼ
		g.translate(15, 15); //ƽ�ƻ�ͼ����ϵ
		paintTetromino(g);//��������ķ���
		paintWall(g);//��ǽ
		paintNextOne(g);//��һ������ķ���
		paintScore(g);//�ɼ�
	}

	private void paintScore(Graphics g) {//��¼�ɼ�
		Font f = getFont();       //��ȡ��ǰ����Ĭ������
		Font font = new Font(f.getName(), 1, 32);
		
		int x = 290;
		int y = 162;
		g.setColor(new Color(6715289));
		g.setFont(font);
		String str = "SCORE:" + this.score;
		g.drawString(str, x, y); //��ָ��λ����ʾ�ɼ�
		y += 56;
		str = "LINES:" + this.lines;
		g.drawString(str, x, y);  //ָ��λ����ʾ����������
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
			int x = (c.getCol() + 10) * 26 - 1;//���ֵ�ʱ��ĺ�����
			int y = (c.getRow() + 1) * 26 - 1;//������
			g.drawImage(c.getImage(), x, y, null);//��������
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
				// TODO �Զ����ɵ� catch ��
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

	public boolean tetrominoCanDrop() {//�ж��Ƿ��������
		Cell[] cells = this.tetromino.getCells();
		for (int i = 0; i < cells.length; i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			if (row == 19) {//���ֻ����20��
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
		if ((outOfBound()) || (coincide())) {//�����жϣ��Ƿ񵽴�߽�
			this.tetromino.moveLeft();
		}
	}

	public void moveLeftAction() {
		this.tetromino.moveLeft();
		if ((outOfBound()) || (coincide())) {//panduan
			this.tetromino.moveRight();
		}
	}
/*�ж��Ƿ񵽴�߽�*/
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
/**�ж��Ƿ�����ƶ�*/
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
 * �������תʵ��*/
	public void rotateRightAction() {
		this.tetromino.rotateRight();
		if ((outOfBound()) || (coincide())) {//�ж�
			this.tetromino.rotateRight();
		}
	}

	/*public void rotateLeftAction() {
		this.tetromino.rotateLeft();
		if ((outOfBound()) || (coincide())) {//�ж��Ƿ�������ת������
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
		frame.setTitle("�����������ս");
		frame.setDefaultCloseOperation(3);

		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		tetris.action();
	}
}
