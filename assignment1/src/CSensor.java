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
	public Line line = new Line(0.0f, 0.0f, 0.0f, 0.0f);
	public Line line2 = new Line(0.0f, 0.0f);
	
	// Empty vector to use
	public Vector2f vector = new Vector2f();
	
	// Turn sensors on and off
	public boolean m_bl_rangeFinder;
	public boolean m_bl_agentSensor;
	public boolean m_bl_radar;
	
	// Range finder
	public int distance = 0;
	//private float[] fa_center = new float[3];
	//private float[] fa_vectorCalc = {-1000.0f, -1000.0f, -1000.0f, -1000.0f};
	
	// Pointer to the entity
	public CEntity C_entity;
	
	// Others
	public int directionX;
	public int directionY;
	public int lineMultiplier = 60;
	private float f_tempX;
	private float f_tempY;
	private float feelerLength = 20;
	
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
	
	
	 
	// Range finder
	public void rangeFinder(float theta, Vector2f m_V2f_position, Vector2f m_V2f_velocity ){
		
		System.out.println("RANGE FINDER " + "Theta: " + theta);
		
		f_tempX =  m_V2f_position.x;
		f_tempY =  m_V2f_position.y;
		
		// Current x position = x + v * cos(theta) v is set to sensor length of 80
		line.setLocation(f_tempX, f_tempY);
		line.set(f_tempX, f_tempY, ((float)(f_tempX * feelerLength * Math.cos(theta))), ((float)(f_tempY * feelerLength * Math.cos(theta))));
	
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
