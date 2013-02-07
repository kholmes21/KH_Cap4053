import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;

/* A class to implement sensors:
 * Range finders return distance
 * Adjacent Agent sensors return relative angles, distances
 * Pie slice sensors (radars) return activation levels
 * 
 */
public class CSensor {

	public static final boolean ON = true;
	public static final boolean OFF = false;
	
	// Range finder lines
	public Line L_feeler1 = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	public Line L_feeler2 = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	public Line L_feeler3 = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	
	// Empty vector to use
	public Vector2f vector = new Vector2f();
	
	// Turn sensors on and off
	public boolean m_bl_rangeFinder = OFF;
	public boolean m_bl_agentSensor = OFF;
	public boolean m_bl_radar = OFF;
	
	// Range finder
	public int distance = 0;
	public double f_theta = 0.0;
	public float f_thetaDiff = 45.0f;
	//private float[] fa_center = new float[3];
	//private float[] fa_vectorCalc = {-1000.0f, -1000.0f, -1000.0f, -1000.0f};
	
	// Pointer to the entity
	public CEntity C_entity;
	
	// Access to drawing space
	Graphics g;
	
	// Others
	public int directionX;
	public int directionY;
	public int lineMultiplier = 60;
	private float f_tempX;
	private float f_tempY;
	private float f_feelerLength = 120;
	private float f_feelDistCen = 0.0f;
	//private float f_feelDistLef = 0.0f;
	//private float f_feelDistRgt = 0.0f;
	
	
	
	// Entity data
	//private float boundingX = C_shipEntity.m_boundingEllipse.getCenterX();
	//private float boundingY = C_shipEntity.m_boundingEllipse.getCenterY();
	
	// Constructor
	public CSensor(boolean bl_range, boolean bl_agent, boolean bl_radar, CEntity entity) {
		
		this.setM_bl_rangeFinder(bl_range);
		this.setM_bl_agentSensor(bl_agent);
		this.setM_bl_radar(bl_radar);
		this.C_entity = entity;
		
	}
	
	// Run any active sensors
	public void sensorUpdate(float theta, Vector2f m_V2f_position, Vector2f m_V2f_velocity){
		
		if(m_bl_rangeFinder == ON){
			// method to do range stuff
			rangeFinder(theta,  m_V2f_position, m_V2f_velocity);
		}
		if(m_bl_agentSensor == ON){
			// do agent sensing
		}
		if(m_bl_radar == ON){
			// do radar
		}
	}
	
	// Draw sensor lines
	public void sensorRender(Graphics g){
		
		// Method to do range finder
		if(m_bl_rangeFinder == ON){
			
			// Draw feeler colors
			g.setColor(new Color(27, 224, 27)); // Green feeler
			g.draw(L_feeler1);	
			
			g.setColor(new Color(27, 27, 224)); // Blue feeler
			g.draw(L_feeler2);
			
			g.setColor(new Color(224, 27, 27)); // Red feeler
			g.draw(L_feeler3);
			
			// Display feeler output
			g.setColor(new Color(0, 0, 0));
			g.drawString("Center Feeler distance: " + f_feelDistCen, 10, 50);
			g.drawString("Left   Feeler distance: ", 10, 65);
			g.drawString("Right  Feeler distance: ", 10, 80);
			

		}
		if(m_bl_agentSensor == ON){
			// do agent sensing
		}
		if(m_bl_radar == ON){
			// do radar
		}
		
		
	}
	
	 
	// Range finder
	public void rangeFinder(float theta, Vector2f m_V2f_position, Vector2f m_V2f_velocity ){
		
				
		f_tempX =  (m_V2f_position.x + 20); //C_entity.getXposition(); 
		f_tempY =  (m_V2f_position.y + 50); //C_entity.getYposition(); 
		f_theta = (float)(Math.toRadians(C_entity.getImagePointer().getRotation()));
		f_thetaDiff = (float)Math.toRadians(Math.toDegrees(f_thetaDiff));
		//System.out.println("center of rotation y : " + C_entity.getImagePointer().getCenterOfRotationY());
		
		// Current x position = x + v * cos(theta) v is set to sensor length of 80
		L_feeler1.setLocation(f_tempX, f_tempY);	L_feeler2.setLocation(f_tempX, f_tempY);	L_feeler3.setLocation(f_tempX, f_tempY);
		
		System.out.println("RANGE FINDER " + "  F_theta " + f_theta + "  Diff " + f_thetaDiff + "Cos " + (float)(f_tempX * f_feelerLength * Math.cos(f_theta)));
		
		// For angle 0
		if(f_theta == 0.0f){
			L_feeler1.set(f_tempX, f_tempY, f_tempX, f_tempY - f_feelerLength);
			
			L_feeler2.set(f_tempX, f_tempY, f_tempX + ( (float)((f_feelerLength * Math.sin(f_thetaDiff))) ),
											f_tempY - ( (float)(f_feelerLength * Math.cos(f_thetaDiff))) ); // feelerLength * tan(theta) = y coord
			
			L_feeler3.set(f_tempX, f_tempY, f_tempX - ( (float)((f_feelerLength * Math.sin(f_thetaDiff))) ),
											f_tempY - ( (float)(f_feelerLength * Math.cos(f_thetaDiff))) );
			
			System.out.println("Feelers");
		}
		else /*if(f_theta > 0 && f_theta < 90)*/{
			L_feeler1.set(f_tempX, f_tempY, f_tempX + ( (float)((f_feelerLength * Math.sin(f_theta))) ),
											f_tempY - ( (float)(f_feelerLength * Math.cos(f_theta))) );
			
			L_feeler2.set(f_tempX, f_tempY, (f_tempX + ( (float)((f_feelerLength * Math.sin(f_theta + f_thetaDiff))))),
											(f_tempY - ( (float)(f_feelerLength * Math.cos(f_theta + f_thetaDiff)))));
			
			L_feeler3.set(f_tempX, f_tempY, f_tempX + ( (float)((f_feelerLength * Math.sin(f_theta - f_thetaDiff)))),
											f_tempY - ( (float)(f_feelerLength * Math.cos(f_theta - f_thetaDiff))));

			System.out.println("Vehicle x and y :" + f_tempX + "  " + f_tempY);
			System.out.println("x coordinate " + (float)((f_feelerLength * Math.sin(f_theta + f_thetaDiff))));
			System.out.println("x coordinate + next coordinate" + (f_tempX + ( (float)((f_feelerLength * Math.sin(f_theta + f_thetaDiff))))));
			System.out.println("y coordinate " + (float)(f_feelerLength * Math.cos(f_theta + f_thetaDiff)));
			System.out.println("y coordinate + next coordinate" + (f_tempY + ( (float)((f_feelerLength * Math.cos(f_theta))))));
			//L_feeler2.set(f_tempX, f_tempY, f_tempX + 30, f_tempY - feelerLength);
			//L_feeler3.set(f_tempX, f_tempY, f_tempX - 30, f_tempY - feelerLength);
		}
		/*else{
			L_feeler1.set(0.0f, 0.0f, 0.0f, 0.0f);	L_feeler2.set(0.0f, 0.0f, 0.0f, 0.0f);	L_feeler3.set(0.0f, 0.0f, 0.0f, 0.0f);
		}*/
		
		if(L_feeler1.intersects(Main.obstacle1) | L_feeler1.intersects(Main.obstacle2)){
			
			f_feelDistCen = 100;
		}
	
	}
	
	
	
	
	
	// Turn radar on / off
	public void setM_bl_radar(boolean flipSwitch) {
		this.m_bl_radar = flipSwitch;
	}

	// Turn agent sensor on / off
	public void setM_bl_agentSensor(boolean flipSwitch) {
		this.m_bl_agentSensor = flipSwitch;
	}

	// Turn range finder on / off
	public void setM_bl_rangeFinder(boolean flipSwitch) {
		this.m_bl_rangeFinder = flipSwitch;
	}

}
