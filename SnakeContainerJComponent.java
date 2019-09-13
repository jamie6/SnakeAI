import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;

/**
 *
 * @author Jamie
 */
// draws all the snake containers 
public class SnakeContainerJComponent extends JComponent
{
    TrainSnakeTable tst;
    boolean largeContainer = false;
    int snakeArea, containerSize, spacing;
    boolean showSnakeId = true;
    Color containerColor = new java.awt.Color(255, 220, 120);
    Color deadSnakeColor = new Color(200, 150, 150);
    Color backgroundColor = new java.awt.Color(0, 120, 130);
    Color selectedContainerColor = new Color(155, 220, 120);

    int selectedIndex = -1;

    SnakeContainer sc[];
    Food[] food;

    java.awt.Rectangle background;
    java.util.LinkedList<java.awt.Rectangle> bgs; //backgrounds
    int fontSize;
    int labelOffset;
    int size;

    public SnakeContainerJComponent(int size, SnakeContainer sc[], TrainSnakeTable tst)
    {
        background = new java.awt.Rectangle(0, 0, size, size);
        this.sc = sc;
        this.tst = tst;
        bgs = new java.util.LinkedList<>();
        this.size = size;
        for (int i = 0; i < sc.length; i++)
        {
            bgs.add(new java.awt.Rectangle(sc[i].getXOffset(), sc[i].getYOffset(), sc[i].getSize(), sc[i].getSize()));
        }
        fontSize = 12;
        labelOffset = (int) (bgs.get(0).getSize().height * .1);

        setPreferredSize(new java.awt.Dimension(size, size));

        this.addMouseListener(new SCJCMouseListener());
    }
    
    public void newgame(SnakeContainer sc[], TrainSnakeTable tst)
    {
        this.sc = sc;
        this.tst = tst;
    }
    
    @Override
    public Dimension getSize() { return new Dimension(size,size); }

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;

        g2.setFont(new java.awt.Font("Arial", java.awt.Font.PLAIN, fontSize));

        g2.setColor(backgroundColor);
        g2.fill(background);

        // draw background rectangles
        if (bgs != null)
        {
            for (int i = 0; i < bgs.size(); i++)
            {
                if (selectedIndex == i)
                    g2.setColor(selectedContainerColor);
                else
                {
                    if (sc[i].isGameOver())
                        g2.setColor(deadSnakeColor);
                    else
                        g2.setColor(containerColor);
                }
                g2.fill(bgs.get(i));
                g2.setColor(java.awt.Color.BLACK);
                if (showSnakeId)
                    g2.drawString("Snake " + i,
                            bgs.get(i).getLocation().x,
                            bgs.get(i).getLocation().y + g2.getFontMetrics().getHeight());
            }
        }

        // draw snake and food
        if (sc != null && sc[0] != null)
        {
            for (int i = 0; i < sc.length; i++)
            {
                for (int j = 0; j < sc[i].getFood().length; j++)
                {
                    food = sc[i].getFood();
                    g2.setColor(food[j].getColor());
                    g2.fill(food[j].getRect());
                }

                java.util.LinkedList<SnakeSegment> tempSS = sc[i].getSnake();
                for (int j = 0; j < tempSS.size(); j++)
                {
                    g2.setColor((tempSS.get(j)).getColor());
                    g2.fill((tempSS.get(j)).getRect());
                }
            }
        }
    } // end paintComponent

    public void setShowSnakeId(boolean showSnakeId)
    {
        this.showSnakeId = showSnakeId;
    }

    public int getSelectedIndex()
    {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex)
    {
        this.selectedIndex = selectedIndex;
    }

    public class SCJCMouseListener implements MouseListener
    {

        @Override
        public void mouseClicked(MouseEvent me)
        {
            for (int i = 0; i < bgs.size(); i++)
            {
                if (bgs.get(i).contains(me.getX(), me.getY()))
                {
//                    if (selectedIndex == i)
//                    {
//                        if ( largeContainer ) // make small
//                        {
//                        }
//                        else // make large
//                        {
//                        }   
//                    } else
                    //{
                    if (selectedIndex != -1)
                    {
                        Rectangle temp = bgs.get(selectedIndex);
                        selectedIndex = i;
                        tst.highlightRow(selectedIndex);
                        paintImmediately(temp);
                    }
                    selectedIndex = i;
                    paintImmediately(bgs.get(i));
                    //}
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent me)
        {

        }

        @Override
        public void mouseReleased(MouseEvent me)
        {
            for (int i = 0; i < bgs.size(); i++)
            {
                if (bgs.get(i).contains(me.getX(), me.getY()))
                {
                    if (selectedIndex != -1)
                    {
                        Rectangle temp = bgs.get(selectedIndex);
                        selectedIndex = i;
                        tst.highlightRow(selectedIndex);
                        paintImmediately(temp);
                    }
                    selectedIndex = i;
                    paintImmediately(bgs.get(i));
                }
            }
        }

        @Override
        public void mouseEntered(MouseEvent me)
        {
        }

        @Override
        public void mouseExited(MouseEvent me)
        {
        }
    }
}
