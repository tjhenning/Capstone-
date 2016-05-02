import javax.swing.JPanel;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;
//import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
//import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.Color;
import java.util.ArrayList;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.Stroke;
import java.awt.BasicStroke;

public class DrawingPanel extends JPanel
{
    ArrayList<Shape> groundBlocks;
    ArrayList<Entity> enemies;
    Player player=new Player(new Point2D.Double(100,400),30);
    RectObj finish=new RectObj(new Point2D.Double(3000,100),50,1000,Color.BLACK);
    Color current;    
    boolean isShift=false;
    boolean isLeft=false;
    boolean isRight=false;
    static boolean isJumping=false;
    boolean isCrouching=false;
    double lol=.5;
    double wlol=.01;
    int currentLevel=1;
    static double wobble=1;
        
    public DrawingPanel()
    {        
        groundBlocks=new ArrayList<Shape>();
        enemies=new ArrayList<Entity>();
        setBackground(Color.WHITE);       
        current=new Color(0,0,0);
        //addMouseListener(new ClickListener());
        //addMouseMotionListener(new MovementListener());
        setFocusable(true);
        addKeyListener(new KeysListener());
        loadLevel(currentLevel);
        
    }
    
    public Color getColor()
    {
        return current;
    }
    
    public Dimension getPreferredSize()
    {
        Dimension d=new Dimension(350,300);
        return d;
    }
    
    public void finishLevel()
    {           
        
        try {
            Thread.sleep(30);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        
        while (player.getYRadius()<1000)
        {
            player.changeRadius(20);
            repaint();
            try {
                Thread.sleep(30);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            
        }        
        while (player.getY()<890+player.getUpV())
        {
            player.calcMove(false);
            repaint();
        }
        currentLevel++;
        nextLevel();
    }
   
    public void nextLevel()
    {
        
        player.resetRadii();     
        player.goTo(100,300);
        loadLevel(currentLevel);
        
    }
           
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(4));
        
        double position=finish.getStaticX()-Player.scrollX;
        if (position+finish.getXL()>-20&&position-finish.getXL()<1220)
        {
            finish.goToX(position);
            finish.draw(g2);
        }
        else
        {
            finish.goToX(-999);
        }
        finish.draw(g2);      
        for (Entity enemy:enemies)
        {            
            if(enemy.isAlive())
            {
                if (enemy.getMate()!=null)
                {
                    if(enemy.getMate().getCenter().getX()!=-999)
                    {
                        enemy.calcXY();
                        enemy.draw(g2);                       
                    }
                    else
                    {
                        enemy.goToX(-999);
                    }
                }
                else
                {
                    if(enemy.getX()!=-999)
                    {
                        enemy.calcXY();
                        enemy.draw(g2);
                
                    }
                    else
                    {
                        enemy.goToX(-999);
                    }
                }
            }
        }
        
        for (Shape shape:groundBlocks)
        {            
            position=shape.getStaticX()-player.getScrollX();
            if (position+shape.getXL()>-20&&position-shape.getXL()<1220)
            {
                shape.goToX(position);
                shape.draw(g2);
            }
            else
            {
                shape.goToX(-999);
            }
        }
        
        player.draw(g2);
        // System.out.println(current);
    }
    public void nextFrame()
    {
        if (player.isHitNextFrame(finish))
        {
            finishLevel();  
            
        }
        else
        {
             for (Entity enemy:enemies)
            {                            
                if(enemy.getX()!=-999&&enemy.isAlive())
                {
                    boolean b=player.isOnTopOfNextFrame(enemy);        
                    if (b)
                    {
                        player.bounce(enemy.interactionType());
                        enemy.getHit(player);
                    }
                    else if(player.isHitNextFrame(enemy))
                    {
                        player.takeDamage(enemy.interactionType());
                        
                        
                    }
                }            
            }    
            for (Shape shape:groundBlocks)
            {            
                if(player.isHitNextFrame(shape))
                {
                    player.hitWall(shape.getCenter().getX(),shape.getCenter().getY(),shape.getXL(),shape.getYL(),shape);
                  
                }
                boolean b=player.isOnTopOfNextFrame(shape);
                int top=(int)(shape.getCenter().getY()-shape.getYL());
                player.whenTouchingGround(b,top);
            }         
                                
            player.calcMove(isJumping);      
            if (isLeft)
            {
                player.moveX(-1,isShift,isCrouching);
                lol();
            }
            if (isRight)
            {
                player.moveX(1,isShift,isCrouching);
                lol();
            }
            if (!isLeft&&!isRight)
            {
                player.moveX(0,isShift,isCrouching);
            }
            if(player.getDed())
            {
                loadLevel(currentLevel);
            }
            if(wobble>1)
            {
                wlol=-.02;                
            }
            else if (wobble<-1)
            {
                wlol=.04;
            }
            wobble+=wlol;
            repaint();
            requestFocusInWindow();
            
        }
    }
    public void lol()
    {
        if(!isCrouching)
        {
            if(player.getXRadius()>40)
            {
                lol=-.5;                
            }
            else if (player.getXRadius()<30)
            {
                lol=.5;
            }
            player.changeRadius(lol);
        }
     }
    public void loadLevel(int which)
    {
        groundBlocks=new ArrayList<Shape>();
        enemies=new ArrayList<Entity>();
        Player.scrollX=0;
        
        if (which==1)
        {
            groundBlocks.add(new RectObj(new Point2D.Double(400,625),400,25,Color.BLACK));
            groundBlocks.add(new SquareObj(new Point2D.Double(400,250),50,Color.BLACK));
            
            Powerup lv1=new Powerup(Color.GREEN,500,350,1);
            groundBlocks.add(new BoxWithItem(new Point2D.Double(500,350),50,Color.RED,lv1));
            
            groundBlocks.add(new SquareObj(new Point2D.Double(1100,625),50,Color.BLUE));
            groundBlocks.add(new SquareObj(new Point2D.Double(1500,500),50,Color.RED));
            groundBlocks.add(new RectObj(new Point2D.Double(1900,700),300,50,Color.YELLOW));
            groundBlocks.add(new RectObj(new Point2D.Double(1900,300),150,50,Color.BLACK));
            
            enemies.add(new MatedEnemy(Color.GRAY,groundBlocks.get(6),50,.01,5));
            enemies.add(new MatedEnemy(Color.RED,groundBlocks.get(3),50,.02,5));
            enemies.add(new FlyingEnemy(Color.BLUE, 2500, 400,300,.01,40));
            enemies.add(new Killplane(Color.RED,1300,600,50));
            enemies.add(lv1);            
        }
        else if (which==2)
        {
            groundBlocks.add(new RectObj(new Point2D.Double(400,625),400,25,Color.BLACK));
            groundBlocks.add(new SquareObj(new Point2D.Double(400,250),50,Color.BLACK));
            
            Powerup lv1=new Powerup(Color.GREEN,500,350,2);
            groundBlocks.add(new BoxWithItem(new Point2D.Double(500,250),50,Color.RED,lv1));
            
            groundBlocks.add(new SquareObj(new Point2D.Double(1100,725),50,Color.BLUE));
            groundBlocks.add(new SquareObj(new Point2D.Double(2500,300),50,Color.RED));
            groundBlocks.add(new RectObj(new Point2D.Double(1500,600),50,500,Color.YELLOW));
            groundBlocks.add(new RectObj(new Point2D.Double(1900,300),150,50,Color.BLACK));
            
            enemies.add(new MatedEnemy(Color.GRAY,groundBlocks.get(6),50,.01,5));
            enemies.add(new MatedEnemy(Color.RED,groundBlocks.get(3),50,.02,5));
            enemies.add(new FlyingEnemy(Color.BLUE, 600, 400 , 200,.002,40));
            enemies.add(new Killplane(Color.RED,1400,750,50));
            enemies.add(lv1);            
        }
        else if (which==3)
        {
            groundBlocks.add(new RectObj(new Point2D.Double(425,625),400,25,Color.BLACK));
            groundBlocks.add(new RectObj(new Point2D.Double(800,650),60,450,Color.YELLOW));
            groundBlocks.add(new RectObj(new Point2D.Double(1100,100),40,300,Color.YELLOW));
            finish=new RectObj(new Point2D.Double(1300,100),50,1000,Color.BLACK);              
        }
        else if (which==4)
        {
            groundBlocks.add(new RectObj(new Point2D.Double(1000,800),1000,75,Color.BLACK));
            groundBlocks.add(new RectObj(new Point2D.Double(1000,125),1000,75,Color.BLACK));
            enemies.add(new FlyingEnemy(Color.ORANGE,600,400,150,.001,40));
            enemies.add(new FlyingEnemy(Color.ORANGE,1100,0,550,.0005,40));
            enemies.add(new MatedEnemy(Color.RED,groundBlocks.get(1),75,.001,75));
            enemies.add(new MatedEnemy(Color.RED,groundBlocks.get(0),35,.002,5));
            enemies.add(new Killplane(Color.RED,1350,675,50));
            enemies.add(new Killplane(Color.RED,1350,575,50));
            enemies.add(new Killplane(Color.RED,1350,250,50));
            enemies.add(new Killplane(Color.RED,900,675,50));                       
            enemies.add(new Killplane(Color.RED,1850,625,100));  
            enemies.add(new Killplane(Color.RED,1850,475,50));   
            
            finish=new RectObj(new Point2D.Double(2000,100),50,1000,Color.BLACK);
        }
    }
    public class KeysListener implements KeyListener
    {
        
        public void keyPressed(KeyEvent e)
        {              
            
            if (e.getKeyCode()==KeyEvent.VK_SPACE)
            {
                player.jump();
                isJumping=true;
            }
            else if (e.getKeyCode()==16)
            {
                isShift=true;
            }
            else if (e.getKeyCode()==KeyEvent.VK_A)
            {
                 isLeft=true;
            }
            else if (e.getKeyCode()==KeyEvent.VK_D)
            {
                 isRight=true;
            }            
            else if (e.getKeyCode()==KeyEvent.VK_S)
            {
                 isCrouching=true;
            }       
            else if (e.getKeyCode()==KeyEvent.VK_P)
            {
                 Player.scrollX-=20;
            }
            //repaint();
            requestFocusInWindow();           
        }
        public void keyReleased(KeyEvent e)
        {
            if (e.getKeyCode()==16)
            {
                isShift=false;
            }
            else if (e.getKeyCode()==KeyEvent.VK_A)
            {
                 isLeft=false;
            }
            else if (e.getKeyCode()==KeyEvent.VK_D)
            {
                 isRight=false;
            }          
            else if (e.getKeyCode()==KeyEvent.VK_SPACE)
            {
                isJumping=false;
            }
            else if (e.getKeyCode()==KeyEvent.VK_S)
            {
                 isCrouching=false;
            }   
            
        }
        public void keyTyped(KeyEvent e)
        {
            
        }
    }
}
