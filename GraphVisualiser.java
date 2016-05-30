package org.main;

import java.awt.*;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Samuel Heath
 */
public class GraphVisualiser {
    
    private Graph g;
    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Image canvasImage;
    
    /**
     * 
     * @param g The graph we want to visually represent.
     * @param title The name of the window that opens.
     * @param width 
     * @param height
     * @param bgColour 
     */
    public GraphVisualiser(Graph g, String title, int width, int height, Color bgColour) {
        this.g = g;
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width,height));
        frame.pack();
        Dimension size = canvas.getSize();
        canvasImage = canvas.createImage(size.width,size.height);
        graphic = (Graphics2D) canvasImage.getGraphics();
        graphic.setColor(bgColour);
        graphic.fillRect(0,0,size.width,size.height);
        graphic.setColor(Color.black);
        frame.setVisible(true);
        frame.setVisible(true);
	//        frame.show();
        frame.setVisible(true);
        draw();
        repaint();
    }
    
    /**
     * 
     */
    class CanvasPane extends JPanel {
        public void paint(Graphics g) {
            g.drawImage(canvasImage,0,0,null);
        }
    }
    
    /**
     * 
     */
    public void draw() {
        //Draw key
        int h = canvas.getHeight();
        int w = canvas.getWidth();
        drawLine(2,2,2,152,Color.BLACK);
        drawLine(2,152,75,152,Color.BLACK);
        drawLine(2,2,75,2,Color.BLACK);
        drawLine(75,2,75,152,Color.BLACK);
        drawRectangle(5,5,15,15, Color.GREEN);
        drawString("0-9 %", 25,15, Color.BLACK);
        drawRectangle(5,20,15,30, Color.ORANGE);
        drawString("10-19 %",25,30, Color.BLACK);
        drawRectangle(5,35,15,45, Color.PINK);
        drawString("20-29 %", 25,45, Color.BLACK);
        drawRectangle(5,50,15,60, Color.MAGENTA);
        drawString("30-39 %", 25,60, Color.BLACK);
        drawRectangle(5,65,15,75, Color.RED);
        drawString("40-49 %", 25,75, Color.BLACK);
        drawRectangle(5,80,15,90, Color.LIGHT_GRAY);
        drawString("50-59 %", 25,90, Color.BLACK);
        drawRectangle(5,95,15,105, Color.GRAY);
        drawString("60-69 %", 25,105, Color.BLACK);
        drawRectangle(5,110,15,120,Color.DARK_GRAY);
        drawString("70-79 %", 25, 120, Color.BLACK);
        drawRectangle(5,125,15,135,Color.BLUE);
        drawString("80-89 %",25,135, Color.BLACK);
        drawRectangle(5,140,15,150,Color.BLACK);
        drawString("90-99 %",25,150, Color.BLACK);
        drawString("Incident Nodes (%)", 2,165, Color.BLACK);
        //End
        Random rx = new Random(); //random for the x component.
        Random ry = new Random(); //random for the y component.
        int x,y;
        int[][] nodePos = new int[g.getNumberOfVertices()][3];
        for (int i = 0; i < g.getEdgeMatrix()[0].length; i ++) {
            x = rx.nextInt(canvas.getWidth() - 80) + 80;
            y = ry.nextInt(canvas.getHeight() - 60) + 50;
            int count = 0;
            for (int j = 0; j < g.getNumberOfVertices(); j++) {
                if (g.getEdgeMatrix()[i][j] == 1) {
                    count++;
                }
            }
            int percentIndex = (int) Math.floor(((double)count/g.getNumberOfVertices())*10);
            nodePos[i] = new int[] {x,y, percentIndex};
        }
        for (int i = 0; i < g.getNumberOfVertices(); i++) {
            for (int j = 0; j < g.getNumberOfVertices(); j++) {
                if(g.getEdgeMatrix()[i][j] == 1) {
                    switch (nodePos[i][2]){
                        case 0: this.setForegroundColour(Color.GREEN);
                                break;
                        case 1: this.setForegroundColour(Color.ORANGE);
                                break;
                        case 2: this.setForegroundColour(Color.PINK);
                                break;
                        case 3: this.setForegroundColour(Color.MAGENTA);
                                break;
                        case 4: this.setForegroundColour(Color.RED);
                                break;
                        case 5: this.setForegroundColour(Color.LIGHT_GRAY);
                                break;
                        case 6: this.setForegroundColour(Color.GRAY);
                                break;
                        case 7: this.setForegroundColour(Color.DARK_GRAY);
                                break;
                        case 8: this.setForegroundColour(Color.BLUE);
                                break;
                        case 9: this.setForegroundColour(Color.BLACK);
                                break;
                        default: this.setForegroundColour(Color.YELLOW);
                                break;
                    }
                    drawLine(nodePos[i][0], nodePos[i][1], nodePos[j][0], nodePos[j][1], this.getForegroundColour());
                }
                graphic.fillOval(nodePos[i][0]-7, nodePos[i][1]-7, 14, 14);
            }
        }
    }
    
    /**
     * @param x1 X position start.
     * @param y1 y position start.
     * @param x2 X position finish.
     * @param y2 Y position finish.
     * @param c The colour to draw the line in.
     */
    public void drawLine(int x1, int y1, int x2, int y2, Color c) {
        setForegroundColour(c);
        graphic.drawLine(x1,y1,x2,y2);
        canvas.repaint();
    }
    
    /** 
     * Draws a rectangle on the SimpleCanvas between two points.
     * Assumes that x1 <= x2 && y1 <= y2
     */
    public void drawRectangle(int x1, int y1, int x2, int y2, Color c) {
        setForegroundColour(c);
        for (int i = x1; i < x2; i++)
            graphic.drawLine(i, y1, i, y2 - 1);
        canvas.repaint();
    }
    
    /** 
     * Changes the colour for subsequent drawing on this SimpleCanvas
     */
    public void setForegroundColour(Color newColour) {
        graphic.setColor(newColour);
    }
    
    /**
     * Returns the color used for drawing.
     */
    
    public Color getForegroundColour() {
        return graphic.getColor();
    }
    
    /**
     * he font to be used.
     */
    
    public void setFont(Font newFont) {
        graphic.setFont(newFont);
    }
    
    /**
     * Returns the font being used.
     */
    public Font getFont() {
        return graphic.getFont();
    }
    
    /**
     * 
     * @param text
     * @param x
     * @param y
     * @param c 
     */
    public void drawString(String text, int x, int y, Color c) {
        setForegroundColour(c);
        graphic.drawString(text, x, y);
        canvas.repaint();
    }
     
    /**
     * Repaints the canvas.
     */
    public void repaint() {
        canvas.repaint();
    }
}