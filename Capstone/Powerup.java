
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Ellipse2D;

class Powerup extends Entity
{
    Color color;
    double staticX;
    double staticY;
    int type;
    double grow=0;
    public Powerup(Color color, double staticX, double staticY,int type)
    {
        super(color,50);
        this.staticX=staticX;
        this.color=color;    
        this.type=type;
        setAlive(false);
        setY(staticY); 
    }
    public void calcXY()    
    {
        setY(staticY+grow);
        setX(staticX-Player.scrollX);
        
    }
    public void draw(Graphics2D g2)
    {
        //System.out.println(grow);
        if(isAlive())
        {
            if (grow>-100)
            {
                grow-=.5;
            }
            Line2D.Double l1=new Line2D.Double(getX(),(getY()-(radius-3)),getX(),(getY()+(radius-3)));
            Ellipse2D.Double ell2=new Ellipse2D.Double(getX()-25,getY()-25,50,25);
            Ellipse2D.Double ell=new Ellipse2D.Double(getX()-50,getY()-50,100,50);
        
            g2.setColor(color);  
            g2.draw(l1);            
            g2.setColor(Color.RED); 
            g2.fill(ell);
            g2.setColor(Color.ORANGE); 
            g2.fill(ell2);
        }
    }
    
    boolean isInside(Point2D.Double point)
    {
        return (Math.abs(getX()-point.getX())<getRadius())&&(Math.abs(getY()-point.getY())<getRadius());
    }                 
    
    public void getHit(Player player)
    {        
        player.getPowerUp(type);
        setAlive(false);
    }
    
    public int interactionType()
    {
        return 2;
    }
    
    public void appear(Shape block)
    {
        setAlive(true);
        staticY=block.getHeight();
        staticX=block.getStaticX();
    }
}