
package testPaint;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.font.TextHitInfo;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PaintApp extends JFrame implements ActionListener
{
	
	File f1,f2 ;
	PrintWriter pw1;
	
	static StringBuffer textHistory = new StringBuffer() ;
	
	ArrayList<Integer> points ;
	
	JMenuBar mbar;
	JMenu mnuDraw,mnuMore;
	JMenuItem miRect,miCircle,miTrace,miText,miClearScr,miSave,miExit;
	
	JPanel jp1,jp2 ;
	JCheckBox jcBold,jcItalics,jcUnderline;
	JLabel jl1, jlFont ;
	JTextField jtText, jtFont;
	JButton jbOK,jbCancel,jbColor;
	
	Font ftext; // font for text
	
	//This are for Circle
	int CenterX = 0;
	int CenterY = 0;
	int diameter = 0, radius=0 ;
	
	boolean flagRect = false, flagCircle = false,flagLine = false, flagText = false;
	
	//this are for rectangles
	int rectX =0;
	int rectY = 0;
	int width=0,height=0;
	
	//this is to trace lines
	int x1=0,x2=0,y1=0,y2=0,thick=0;
	int pointCount = 0;
	String tracePoints = "";
	
	Color circleColor,rectColor,lineColor,textColor;
	
	//This are for text
	String text = "",font="";
	int textFont = 0;
	int drawTextX = 0;
	int drawTextY = 0 ;
	boolean isBold = false;
	boolean isItalic = false;
	
	
	public PaintApp() throws IOException 
	{
		double random = 0;
		
		random = Math.random();
		
		f1 = new File(".\\logFiles");
		
		if(!f1.exists())
		{
			if(f1.mkdir())
			{
				f2 = new File(".\\logFiles\\PaintLogs"+random);
			}
			
		}
		else if(f1.exists())
		{
			f2 = new File(".\\logFiles\\PaintLogs"+random);
		}
		
		pw1 = new PrintWriter(new FileWriter(f2));
		points = new ArrayList<Integer>();
		
		Font fBold, fItalics,fUnderline;
		
		fBold = new Font("Comic Sans MS", Font.BOLD, 15);
		fItalics = new Font("Comic Sans MS", Font.ITALIC, 15);
		//fUnderline = new Font("Comic Sans MS", Font.HANGING_BASELINE, 15);
		
		mbar = new JMenuBar();
		
		mnuDraw = new JMenu("Draw");
		mnuMore = new JMenu("More");
		
		miRect = new JMenuItem("Rectangle");
		miRect.addActionListener(this);
		mnuDraw.add(miRect);
		
		miCircle = new JMenuItem("Circle");
		miCircle.addActionListener(this);
		mnuDraw.add(miCircle);
		
		miTrace = new JMenuItem("Trace");
		miTrace.addActionListener(this);
		mnuDraw.add(miTrace);
		
		miText = new JMenuItem("Draw Text");
		miText.addActionListener(this);
		mnuDraw.add(miText);
		
		
		miClearScr = new JMenuItem("Clear Screen");
		miClearScr.addActionListener(this);
		mnuMore.add(miClearScr);
		
		/*miSave = new JMenuItem("Save");
		miSave.addActionListener(this);
		mnuMore.add(miSave);*/
		
		
		miExit = new JMenuItem("Exit");
		miExit.addActionListener(this);
		mnuMore.add(miExit);
		
		//mnuMore.addActionListener(this);
		
		mbar.add(mnuDraw);
		mbar.add(mnuMore);
		setJMenuBar(mbar);
		
		jp1 = new JPanel();
		//jp1.setName("Text Editor");
		jp1.setBackground(Color.green);
		jp1.setBorder(BorderFactory.createTitledBorder("Text Editor"));
		
		
		jl1 = new JLabel("Enter Text : ");
		jl1.setFont(fBold);
		jp1.add(jl1);
		
		jtText = new JTextField();
		jtText.setPreferredSize(new Dimension(400,22));
		jp1.add(jtText);
		
		jlFont = new JLabel("Font");
		jlFont.setFont(fBold);
		jp1.add(jlFont);
		
		jtFont = new JTextField();
		jtFont.setPreferredSize(new Dimension(30,22));
		jp1.add(jtFont);
		
		
		//jp.add(jtText,BorderLayout.NORTH);
		
		jcBold = new JCheckBox("Bold");
		jcBold.setFont(fBold);
		jcBold.setEnabled(true);
		jcBold.setBackground(Color.green);
		jp1.add(jcBold);	
		
		jcItalics = new JCheckBox("Italics");
		jcItalics.setFont(fItalics);
		jcItalics.setEnabled(true);
		jcItalics.setBackground(Color.green);
		jp1.add(jcItalics);
		
		jbColor = new JButton("Choose Color");
		jbColor.addActionListener(this);
		jp1.add(jbColor);
		
		jbOK = new JButton("OK");
		jbOK.addActionListener(this);
		jp1.add(jbOK);
		
		jbCancel = new JButton("Cancel");
		jbCancel.addActionListener(this);
		jp1.add(jbCancel);
		
		
		this.add(jp1,BorderLayout.SOUTH);
		jp1.setVisible(false);
		
		setBounds(225, 10, 1250, 650);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.WHITE);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public void drawRect()
	{
		textHistory.append("\n");
		
		flagRect = true;
		
		flagCircle=false;
		flagLine = false;
		flagText = false;
		
		String tempWidth="", tempHeight="";
		
		tempWidth = JOptionPane.showInputDialog("Please Enter the width of the Rectangle ");
		tempHeight = JOptionPane.showInputDialog("Please Enter the height of the Rectangle ");
		
		
		if(tempWidth==null || tempHeight==null || (!tempWidth.matches("^[0-9]\\d*$")) || (!tempHeight.matches("^[0-9]\\d*$")))
		{
			JOptionPane.showMessageDialog(this, "Default width will be 100 units and height will be 75 units", "Warning", JOptionPane.WARNING_MESSAGE);
			width = 100;
			height = 75;			
			
			rectColor = JColorChooser.showDialog(this, "Choose Color for Rectangle", Color.white);
			
			if(rectColor==null)
			{
				JOptionPane.showMessageDialog(this, "Default Color will be Black", "Warning", JOptionPane.WARNING_MESSAGE);
				rectColor = Color.black;
			}
			 
			addMouseListener(
					new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent me) 
						{
							if(flagRect==true && flagCircle==false && flagLine==false && flagText == false)
							{
								rectX = me.getX();
								rectY = me.getY();
								
								//System.out.println("Mouse Pressed in rectangle --  X : "+rectX+" Y : "+rectY);
								Graphics gr = getGraphics();
								
								gr.setColor(rectColor);
								gr.draw3DRect(rectX, rectY, width, height, true);
								//System.out.println("Rectangle : "+rectX+", "+rectY+", "+width+", "+height);
								textHistory.append("Rectangle : "+rectX+", "+rectY+", "+width+", "+height).append("\n");
								//System.out.println("Rectangle is drawn ");
							}
						}
					}
			
				);
		}
		else
		{
			width = Integer.parseInt(tempWidth);
			height = Integer.parseInt(tempHeight);
			
			if(width==0 ||  width<0 )
			{
				JOptionPane.showMessageDialog(this, "Width cannot be 0 or negative, so width will be 100 units", "Warning", JOptionPane.WARNING_MESSAGE);
				width = 100;
				
			}
			if(height==0 || height<0)
			{
				JOptionPane.showMessageDialog(this, "Height cannot be 0 or negative, so height will be 75 units", "Warning", JOptionPane.WARNING_MESSAGE);
				height = 75;
				
			}
			
			if(width>0 && height>0 )
			{
				rectColor = JColorChooser.showDialog(this, "Choose Color for Rectangle", Color.white);
				
				addMouseListener(
						new MouseAdapter() 
						{
							public void mouseClicked(MouseEvent me) 
							{
								if(flagRect==true && flagCircle==false && flagLine==false && flagText == false)
								{
									rectX = me.getX();
									rectY = me.getY();
									
									//System.out.println("Mouse Pressed in rectangle --  X : "+rectX+" Y : "+rectY);
									Graphics gr = getGraphics();
									
									gr.setColor(rectColor);
									gr.draw3DRect(rectX, rectY, width, height, true);
									//System.out.println("Rectangle : "+rectX+", "+rectY+", "+width+", "+height);
									textHistory.append("Rectangle : "+rectX+", "+rectY+", "+width+", "+height).append("\n");
									//System.out.println("Rectangle is drawn ");
								}
							}
						}
				
					);	
			}
		}
	}
	
	public void drawCircle()
	{	
		textHistory.append("\n");
		
		flagCircle=true;
		
		flagRect =false;
		flagLine = false;
		flagText = false;
		
		String diameterTemp = "";
		
		diameterTemp = JOptionPane.showInputDialog("Please Enter the diameter of the Circle ");
		
		if(diameterTemp==null || (!diameterTemp.matches("^[0-9]\\d*$")))
		{
			JOptionPane.showMessageDialog(this, "Default diameter will be 100 units", "Warning", JOptionPane.WARNING_MESSAGE);
			diameter =100;	
			radius = diameter/2;
			
			circleColor = JColorChooser.showDialog(this, "Choose Color for Circle", Color.white);
			
			if(circleColor==null)
			{
				JOptionPane.showMessageDialog(this, "Default Color will be Black", "Warning", JOptionPane.WARNING_MESSAGE);
				circleColor = Color.black;
			}
			
			addMouseListener(
					new MouseAdapter() 
					{
						public void mouseClicked(MouseEvent me) 
						{
							if(flagRect==false && flagCircle==true && flagLine==false && flagText == false)
							{
								CenterX = me.getX();
								CenterY = me.getY();
								
								//System.out.println("Mouse Pressed in circle --  X : "+CenterX+" Y : "+CenterY);
								Graphics gr = getGraphics();
								
								gr.setColor(circleColor);
								
								//gr.drawOval(me.getX(), me.getY(), 100, 100);
								gr.drawOval(CenterX-radius, CenterY-radius, diameter, diameter);
								//System.out.println("Circle : "+CenterX+", "+CenterY+", "+diameter);
								
								textHistory.append("Circle : "+CenterX+", "+CenterY+", "+diameter).append("\n");
								
								//System.out.println("Circle is drawn ");
							}
						}
					}	
					);
		}
		else
		{
			diameter = Integer.parseInt(diameterTemp);
			
			if(diameter==0)
			{
				JOptionPane.showMessageDialog(this, "Since the diameter entered is 0, default size will be 100 units", "Warning", JOptionPane.WARNING_MESSAGE);
				diameter =100;	
				radius = diameter/2;
			}
			else if(diameter<0)
			{
				JOptionPane.showMessageDialog(this, "Since the diameter cannot be less than 0, default size will be 100 units", "Warning", JOptionPane.WARNING_MESSAGE);
				diameter =100;	
				radius = diameter/2;
			}
			
			if(diameter>0)
			{
				radius = diameter/2;				
				//System.out.println("Diameter of the circle is : "+diameter);
				circleColor = JColorChooser.showDialog(this, "Choose Color for Circle", Color.white);
				
				if(circleColor==null)
				{
					JOptionPane.showMessageDialog(this, "Default Color will be Black", "Warning", JOptionPane.WARNING_MESSAGE);
					circleColor = Color.black;
				}
				
				addMouseListener(
						new MouseAdapter() 
						{
							public void mouseClicked(MouseEvent me) 
							{
								if(flagRect==false && flagCircle==true && flagLine==false && flagText == false)
								{
									CenterX = me.getX();
									CenterY = me.getY();
									
									//System.out.println("Mouse Pressed in circle --  X : "+CenterX+" Y : "+CenterY);
									Graphics gr = getGraphics();
									
									gr.setColor(circleColor);
									
									gr.drawOval(CenterX-radius, CenterY-radius, diameter, diameter);
									//System.out.println("Circle : "+CenterX+", "+CenterY+", "+diameter);
									textHistory.append("Circle : "+CenterX+", "+CenterY+", "+diameter).append("\n");
									
								}
							}
						}	
						);
			}
		}
		
	}
	
	
	public void traceLine()
	{
		String thickTemp = "";
		
		x1=0;x2=0;y1=0;y1=0;
		flagLine = true;
		
		flagRect=false;
		flagCircle=false;
		flagText=false;
		
		textHistory.append("\n");
		
		try 
		{
			thickTemp = JOptionPane.showInputDialog("Please Enter the thickness of the Line ");
			
			if(thickTemp==null || (!thickTemp.matches("^[0-9]\\d*$")))
			{
				JOptionPane.showMessageDialog(this, "Default size will be 5 units", "Warning", JOptionPane.WARNING_MESSAGE);
				
				thick=5;
				try 
				{
					lineColor = JColorChooser.showDialog(this, "Choose Color for Line", Color.white);
				}
				catch (Exception e) 
				{
					e.printStackTrace();			
				}
				
				//System.out.println("Color of line : "+lineColor);
				getAxis();
				
			}
			else
			{
				thick = Integer.parseInt(thickTemp);
				
				if(thick==0)
				{
					JOptionPane.showMessageDialog(this, "Since the size chosen is 0, so default size will be 5 units", "Warning", JOptionPane.WARNING_MESSAGE);
					thick=5;
				}
				if(thick>0)
				{					
					try 
					{
						lineColor = JColorChooser.showDialog(this, "Choose Color for Line", Color.white);
					}
					catch (Exception e) 
					{
						e.printStackTrace();			
					}
					//System.out.println("Color of line : "+lineColor);
					getAxis();
				}
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	public void getAxis()
	{
		//System.out.print("Trace : "+thick+", "+lineColor);
		
		textHistory.append("Trace : "+thick+", "+lineColor);
		
		addMouseListener(
				new MouseAdapter() 
				{
					public void mouseClicked(MouseEvent me) 
					{
						if(flagRect==false && flagCircle==false && flagLine==true && flagText == false)
						{
							if(pointCount==0)
							{
								x1=me.getX();
								y1=me.getY();
								
								//Saving starting points to an ArrayList
								
								/*points.add(x1);
								points.add(y1);*/
								
								System.out.println(", "+x1+", "+y1);
								textHistory.append(", "+x1+", "+y1);
								
								
								pointCount++;
							}
							else if (pointCount==1) 
							{
								x2=me.getX();
								y2=me.getY();
								
								/*//Saving the 2nd point into the ArrayList
								points.add(x2);
								points.add(y2);*/
								
								System.out.println("Count = 1");
								//System.out.print(", "+x2+", "+y2);
								textHistory.append(", "+x2+", "+y2);
								
								drawLine(x1,y1,x2,y2);
								
								pointCount++;
							}
							else if (pointCount==2) 
							{
								x1=x2;
								y1=y2;
								
								x2=me.getX();
								y2=me.getY();

								/*//keeping track of remaining points in the ArrayList
								points.add(x2);
								points.add(y2);
								*/
								System.out.println("Count = 2");
								//System.out.print(", "+x2+", "+y2);
								
								textHistory.append(", "+x2+", "+y2);
								
								drawLine(x1,y1,x2,y2);
								System.out.println("Draw Line Executed");
								pointCount=2;
							}
						}
					}
				}
				);
	}
	

	public void drawLine(int x1, int y1, int x2, int y2)
	{
		Graphics gr = getGraphics();
		//gr.setColor(lineColor);
		
		
		Graphics2D g2 = (Graphics2D)gr;
		g2.setColor(lineColor);
		g2.setStroke(new BasicStroke(thick));
		
		g2.drawLine(x1, y1, x2, y2);
		g2.dispose();
		
	}
	
	
	public void drawText()
	{
		//System.out.println("drawTextCalled");
		flagText=true;
		
		flagLine = false;
		flagRect=false;
		flagCircle=false;
		
		
		if((!font.equals("")) && (!text.equals("")))
		{
			//System.out.println("Empty text");
			
			textFont = Integer.parseInt(font);  //&& (!text.equals(""))
			
			if(textFont==0)
			{
				JOptionPane.showMessageDialog(this, "Font size canot be 0, so default font size is 15 units", "Warning", JOptionPane.WARNING_MESSAGE);
				textFont = 15;
				jtFont.setText("15");
				
			}
			else if(textFont<0)
			{
				JOptionPane.showMessageDialog(this, "Font size canot be less than 0, so default font size is 15 units", "Warning", JOptionPane.WARNING_MESSAGE);
				textFont = 15;
				jtFont.setText("15");
				
			}
			if(textFont>0)
			{
				//textColor = JColorChooser.showDialog(this, "Choose Color for Line", Color.black);
				
				if(jcBold.isSelected() && jcItalics.isSelected())//Bold and Italics
				{
					isBold = true;
					isItalic = true;
					
					ftext = new Font("Comic Sans MS", Font.BOLD + Font.ITALIC, textFont);
					
				}
				else if((jcBold.isSelected()) && (!jcItalics.isSelected()))//Only Bold
				{
					isBold = true;
					isItalic = false;
					
					ftext = new Font("Comic Sans MS", Font.BOLD , textFont);
				}
				else if((!jcBold.isSelected()) && (jcItalics.isSelected()))//Only Italics
				{
					isBold = false;
					isItalic = true;
					
					ftext = new Font("Comic Sans MS", Font.BOLD , textFont);
				}
				else if((!jcBold.isSelected()) && (!jcItalics.isSelected()))//Plain Text
				{
					isBold = false;
					isItalic = false;
					
					ftext = new Font("Comic Sans MS", Font.PLAIN , textFont);
				}
				
				addMouseListener(
						new MouseAdapter() 
						{
							public void mouseClicked(MouseEvent me) 
							{
								if(flagRect==false && flagCircle==false && flagLine==false && flagText == true)
								{
									drawTextX = me.getX();
									drawTextY = me.getY();
									
									//System.out.println("in drawText : "+textColor.toString());
									//System.out.println("Mouse Pressed in Draw Text --  X : "+x+" Y : "+y);
									Graphics gr = getGraphics();
									
									gr.setColor(textColor);
									gr.setFont(ftext);
									
									gr.drawString(text, drawTextX, drawTextY);
									//System.out.println("Text : "+drawTextX+", "+drawTextY+", Comic Sans MS, "+isBold+", "+isItalic+", "+textFont+", "+text);
									
									textHistory.append("Text : "+drawTextX+", "+drawTextY+", Comic Sans MS, "+isBold+", "+isItalic+", "+textFont+", "+text).append("\n");
								}
							}
						}	
						);	
			}
		}
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource()==miExit)
		{
			System.exit(0);
		}
		
		if(e.getSource()==miClearScr)
		{
			repaint();
			jp1.setVisible(false);
		}
		
		if(e.getSource()==miCircle)
		{
			if(circleColor==null)
			{
				circleColor = Color.black;
			}
			
			CenterX = 0;CenterY = 0;diameter = 0;radius = 0;
			
			jp1.setVisible(false);
			drawCircle();
			
			//System.out.println("Circle : "+CenterX+", "+CenterY+", "+diameter);
		}
		
		if(e.getSource()==miRect)
		{	
			if(rectColor==null)
			{
				rectColor = Color.black;
			}
			
			width = 0;height = 0;
			
			jp1.setVisible(false);
			drawRect();
			
			//System.out.println("Rectangle : "+rectX+", "+rectX+", "+width+", "+height);
		}
		
		if(e.getSource()==miTrace)
		{
			thick = 0;
			
			//points = new ArrayList<Integer>();
			
			if(lineColor==null)
			{
				lineColor = Color.black;
			}
			
			x1 = 0;
			y1 = 0;
			x2 = 0;
			y2 = 0;
			
			pointCount = 0;
			
			jp1.setVisible(false);
			traceLine();
			
		}
		
		if(e.getSource()==miText)
		{
			text = "";font="";
			textFont = 0;
			drawTextX = 0;
			drawTextY = 0 ;
			
			isBold = false;
			isItalic = false;
			
			jp1.setVisible(true);
			
			flagText = false;
		}
		
		if(e.getSource()==jbOK)
		{
			if(textColor==null)
			{
				JOptionPane.showMessageDialog(this, "No Color Selected, so default color is Black", "Warning", JOptionPane.WARNING_MESSAGE);
				textColor = Color.black;
			}
			
			text = jtText.getText();
			
			if(text.equals("") || text==null)
			{
				JOptionPane.showMessageDialog(this, "TextBox Empty, so Default Text set", "Warning", JOptionPane.WARNING_MESSAGE);
				jtText.setText("No Text Entered");
				text = jtText.getText();	
				flagText = true;
			}
			
			font = jtFont.getText();
			
			if(font.equals(""))
			{
				JOptionPane.showMessageDialog(this, "No font size entered, so default font size will be 15 units", "Warning", JOptionPane.WARNING_MESSAGE);
				textFont = 15;
				jtFont.setText("15");
				flagText = true;
			}
			else if((!font.matches("^[0-9]\\d*$")))
			{
				JOptionPane.showMessageDialog(this, "No valid font size entered, so default font size will be 15 units", "Warning", JOptionPane.WARNING_MESSAGE);
				textFont = 15;
				jtFont.setText("15");
				flagText = true;
			}
			
			drawText();
			
			//System.out.println("Text : "+drawTextX+", "+drawTextY+", Comic Sans MS, "+isBold+", "+isItalic+", "+textFont+", "+text);
		}
		if(e.getSource()==jbColor)
		{
			textColor = JColorChooser.showDialog(this, "Choose Color for Line", Color.white);
			if(textColor==null)
			{
				//System.out.println("No Color Selected in JButtColor");
				JOptionPane.showMessageDialog(this, "No Color Selected, so default color is Black", "Warning", JOptionPane.WARNING_MESSAGE);
				textColor = Color.black;
			}
			//System.out.println(textColor.toString());
		}
		if(e.getSource()==jbCancel)
		{
			flagText = false;
			jp1.setVisible(false);
			
		}
		if(e.getSource()==miSave)
		{
			if(textHistory.length()==0)
			{
				JOptionPane.showInputDialog(this, "Nothing to Save");
			}
			else
			{
				System.out.println("String Buffer Content "+textHistory);
			}
			
		}
	}
	
	public static void main(String[] args) throws IOException 
	{
		new PaintApp();
		
	}


	
	
	
	
}
