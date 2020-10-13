import javax.swing.JFrame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import java.awt.Graphics2D;

import javax.swing.JButton;
import java.awt.SystemColor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.event.InputMethodListener;
import java.awt.event.InputMethodEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JSlider;
import javax.swing.BoxLayout;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.WindowStateListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowFocusListener;

public class GameOfLife extends JFrame {
	private JTextField textFieldLargeur;
	private JTextField textFieldHauteur;
	private JPanel panelGrille;
	private JButton buttonGenerate; 
	private JButton buttonStop;
	private JButton buttonPlay;
	private JLabel labelGeneration;
	
	private Graphics2D graph;
	
	private int largeurGrille = 30;
	private int hauteurGrille = 20;
	
	private int tailleCell;
	
	private Cell grille[][];
	private boolean grilleCopie[][];
	private int gen = 0;
	private int vitesse = 100;
	
	private ThreadGame calculateNextGen;
	public GameOfLife() {
		super();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				drawGrille();
			}
		});
		

		setTitle("GameOfLife");
		setSize(500,500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));
		

		
		panelGrille = new JPanel();
		panelGrille.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				isClicked(x,y);
				
			}
		});
		panelGrille.setBackground(Color.WHITE);
		getContentPane().add(panelGrille, BorderLayout.CENTER);
		
		JPanel panelOption = new JPanel();
		panelOption.setBackground(SystemColor.menu);
		getContentPane().add(panelOption, BorderLayout.EAST);
		panelOption.setLayout(new BorderLayout(0, 0));
		
		JPanel panelOption2 = new JPanel();
		panelOption.add(panelOption2, BorderLayout.NORTH);
		panelOption2.setLayout(new BoxLayout(panelOption2, BoxLayout.Y_AXIS));
		
		JPanel panelLargeur = new JPanel();
		panelOption2.add(panelLargeur);
		panelLargeur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel labelLargeur = new JLabel("Largeur de la grille :");
		panelLargeur.add(labelLargeur);
		
		textFieldLargeur = new JTextField();
		textFieldLargeur.setText("30");
		textFieldLargeur.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				largeurGrille = Integer.parseInt(textFieldLargeur.getText());
			}
		});
		labelLargeur.setLabelFor(textFieldLargeur);
		textFieldLargeur.setColumns(10);
		panelLargeur.add(textFieldLargeur);
		
		JPanel panelHauteur = new JPanel();
		panelOption2.add(panelHauteur);
		panelHauteur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel labelHauteur = new JLabel("Hauteur de la grille :");
		panelHauteur.add(labelHauteur);
		
		textFieldHauteur = new JTextField();
		textFieldHauteur.setText("20");
		textFieldHauteur.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				hauteurGrille = Integer.parseInt(textFieldHauteur.getText());
			}
		});
		labelHauteur.setLabelFor(textFieldHauteur);
		textFieldHauteur.setColumns(10);
		panelHauteur.add(textFieldHauteur);
		
		JPanel panelButton = new JPanel();
		panelOption2.add(panelButton);
		panelButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		buttonGenerate = new JButton("Generer nouvelle grille");
		buttonGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initGrille();
				drawGrille();
				gen = 0;
				labelGeneration.setText("Generation : 0");
			}
		});
		panelButton.add(buttonGenerate);
		
		JButton buttonDraw = new JButton("Draw");
		buttonDraw.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawGrille();
			}
		});
		panelButton.add(buttonDraw);
		
		JPanel panel_1 = new JPanel();
		panelOption2.add(panel_1);
		
		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2);
		
		JLabel labelVitesse = new JLabel("Vitesse (ms) :");
		panel_2.add(labelVitesse);
		

		
		JPanel panel_3 = new JPanel();
		panelOption2.add(panel_3);
		
		JSlider slider = new JSlider();
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				vitesse = slider.getValue();
			}
		});
		slider.setValue(100);
		slider.setMajorTickSpacing(20);
		slider.setMinimum(20);
		slider.setMaximum(200);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		panel_3.add(slider);
		
		JPanel panel = new JPanel();
		panelOption.add(panel, BorderLayout.SOUTH);
		
		buttonPlay = new JButton("Play");
		buttonPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculateNextGen = new ThreadGame();
				calculateNextGen.start();
				buttonPlay.setEnabled(false);
				buttonStop.setEnabled(true);
				
			}
		});
		panel.add(buttonPlay);
		
		buttonStop = new JButton("Stop");
		buttonStop.setEnabled(false);
		buttonStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					calculateNextGen.arreter();
					buttonPlay.setEnabled(true);
					buttonStop.setEnabled(false);

			}
		});
		panel.add(buttonStop);
		
		labelGeneration = new JLabel("Generation : 0");
		panel.add(labelGeneration);
		
		
		
		setVisible(true);
		initGraph();
		initGrille();
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				initGraph();
				initScale();
				applyScale();
			}

		});
		
		


	}
	private void isClicked(int x,int y) {
		boolean isFinished = false;
		for (int i = 0 ; i < hauteurGrille ; i++)
		{
			for (int j = 0; j < largeurGrille ; j++)
			{
				if(grille[i][j].isClicked(x, y) && !isFinished) 
				{
					grille[i][j].setIsAlive(!grille[i][j].getIsAlive());
					grille[i][j].drawCell();
					
				}
			}
		}
	}
	public boolean isInGrid(int x, int y) {
	    //Check if a position is valid in the grid
	    if(x < 0 || y < 0) return false;
	    if(x >= hauteurGrille || y >= largeurGrille) return false;
	    return true;
	}
	private void initGraph() {
		graph = (Graphics2D) panelGrille.getGraphics(); //crée le graphic, (tout les graphics sont des graphics2d)
		graph.setStroke(new BasicStroke(1)); //change la largeur des lignes
		graph.setColor(Color.black);
	}
	private void initScale() {
		int largeurGrid = (int) (panelGrille.getWidth()-panelGrille.getWidth()*0.05);
		int hauteurGrid = (int) (panelGrille.getHeight()-panelGrille.getHeight()*0.05);
		if (largeurGrid < hauteurGrid )
		{
			tailleCell = (int) (largeurGrid / largeurGrille);
		}else
		{
			tailleCell = (int) (hauteurGrid / hauteurGrille);
		}
	}
	private void applyScale() {
		int y = 10;
		for (int i = 0 ; i < hauteurGrille ; i++)
		{
			int x = 10;
			for (int j = 0; j < largeurGrille ; j++)
			{
				grille[i][j].changeScale(x, y, tailleCell);
				x += tailleCell;
				
			}
			y += tailleCell;
		}
	}
	private void initGrille() {
		grille = new Cell[hauteurGrille][largeurGrille];
		initScale();
		
		int y = 10;
		for (int i = 0 ; i < hauteurGrille ; i++)
		{
			int x = 10;
			for (int j = 0; j < largeurGrille ; j++)
			{
				grille[i][j] = new Cell(x,y,tailleCell,graph,false);
				x += tailleCell;
				
			}
			y += tailleCell;
		}
		

	}
	private void drawGrille() {
		graph.clearRect(0,0,panelGrille.getWidth(),panelGrille.getHeight());
		for (int i = 0 ; i < hauteurGrille ; i++)
		{
			for (int j = 0; j < largeurGrille ; j++)
			{
				grille[i][j].drawCell();
				
				
			}
		}
	}


	public class ThreadGame extends Thread{
		boolean isRunning;
		public ThreadGame() {
			isRunning = true;
		}
		
		public void run() {
			theGame();
		}
		public void arreter() {
			isRunning = false;
		}
		
		
		public boolean isRunning() {
			return isRunning;
		}

		private void getBool() {
			for (int i = 0 ; i < hauteurGrille ; i++)
			{
				for (int j = 0; j < largeurGrille ; j++)
				{
					grilleCopie[i][j] = grille[i][j].getIsAlive();
				}
			}
		}
		private void setBool() {
			for (int i = 0 ; i < hauteurGrille ; i++)
			{
				for (int j = 0; j < largeurGrille ; j++)
				{
					

						if(grille[i][j].setIsAlive(grilleCopie[i][j]))
						{
							grille[i][j].drawCell();
						}
						
					
					
				}
			}
		}
		private void theGame()
		{
			
			
			while(isRunning) {
				grilleCopie = new boolean[hauteurGrille][largeurGrille];
				getBool();
				try {
					Thread.sleep(vitesse);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (int i = 0 ; i < hauteurGrille ; i++)
				{
					for (int j = 0; j < largeurGrille ; j++)
					{
						rule(i,j);
					}
				}
				setBool();	
				gen++;
				labelGeneration.setText("Generation : "+gen);
			}		
		}
		private void rule(int x,int y)
		{
			int compt = 0;
			int newX = x-1;
			int newY = y-1;
				for (int i = 0;i < 3;i++)
				{
					for(int j = 0; j < 3 ; j++)
					{
						if (isInGrid(newX+i,newY+j) && !(newX+i == x && newY+j ==y) )
						{
							if(grille[newX+i][newY+j].getIsAlive()) 
							{
								compt++;
							}
							
						}
						
					}
				}
				
				if(compt == 3 && !(grille[x][y].getIsAlive())) {
					grilleCopie[x][y] = true;
				}
				if(compt == 2) {
					grilleCopie[x][y] = grille[x][y].getIsAlive();
				}
				if((compt < 2 || compt > 3) && grille[x][y].getIsAlive()) {
					grilleCopie[x][y] = false;
				}
		}

		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GameOfLife();

	}

}
