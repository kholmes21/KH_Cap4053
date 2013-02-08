import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

/* The player agent
 * 
 */
public class CCarEntity extends CEntity{

	// Member variables
	int m_key_pressed = 0;
	float m_speed = 0.2f;
	float m_angle_x = 0;
	float m_f_theta = 0;
	double m_angle = 0;
	float m_mass;
	Vector2f m_V2f_acceleration = new Vector2f();     // Note ** create empty vectors or 0 vectors here to protect Main
	Vector2f m_V2f_force = new Vector2f();
	Vector2f m_V2f_velocity = new Vector2f();
	Vector2f m_V2f_position = new Vector2f();
	
	// Bounding circle
	public Circle m_boundingCircle;
		
	// Sensor class container
	public CSensor C_sensor;
	
	// Other Variables
	public Input input;
		
	// Strings
	@SuppressWarnings("unused")
	private String str_string1;
	private String str_circle;
	
	
	
	
	// Constructor
	public CCarEntity(float x_position, float y_position, int id, Image originalPicture, Circle boundingCircle, float mass){
		super(x_position, y_position, id, originalPicture);
		this.setBoundingCircle(false, originalPicture);
		this.setEntityMass(mass);
		// Sensor
		C_sensor = new CSensor(CSensor.ON, CSensor.OFF, CSensor.OFF, this);
	}
		
	// Constructor - no bounding circle
	public CCarEntity(float x_position, float y_position, int id, Image originalPicture, float mass){
		super(x_position, y_position, id, originalPicture);
		this.setEntityMass(mass);
		
		// Sensor
		C_sensor = new CSensor(CSensor.ON, CSensor.OFF, CSensor.OFF, this);
	}

	
	
	
	// Set bounding circle
	public void setBoundingCircle(boolean reset, Image picture) {
		
		if(reset == true){
			this.m_boundingCircle.setLocation(this.getXposition(), this.getYposition());
			this.m_boundingCircle.setRadii((float)(this.m_imgPtr_picture.getWidth()/2.0f), (float)(this.m_imgPtr_picture.getWidth()/2.0f));
			//this.m_boundingEllipse = new Ellipse(this.getXposition() + 30, this.getYposition() + 30,
			//		((float)(this.m_pgImg_picture.getWidth()/2.0f)), ((float)(this.m_pgImg_picture.getWidth()/2.0f)));
		}
		else{
			this.m_boundingCircle.setLocation(this.getXposition(), this.getYposition());
			this.m_boundingCircle.setRadii((float)(picture.getWidth()/2.0f), (float)(picture.getWidth()/2.0f));
			//this.m_boundingEllipse = new Ellipse(this.getXposition() + 30, this.getYposition() + 30,
				//	((float)(picture.getWidth()/2.0f)), ((float)(picture.getWidth()/2.0f)));
			
		}
	
	}
	
	// Update function for entity
	public void entityUpdate(float delta, Vector2f V2f_velocity, Vector2f V2f_position, float f_theta, Input input){
		
		m_f_theta = f_theta;              // To guarantee that theta passed in is passed to sensor update if as-is unless changed
		m_V2f_velocity = V2f_velocity;    // by one of the *if* statements below.
		m_V2f_position = V2f_position;
		
		
		if(input.isKeyDown(Input.KEY_LEFT)){
			m_key_pressed = 2;
			m_f_theta = (f_theta - 1.0f) * ((float)delta/100);
		}
		else if(input.isKeyDown(Input.KEY_RIGHT)){
			m_key_pressed = 1;
			m_f_theta = (f_theta + 1.0f) * ((float)delta/100);
		}
		else
			m_key_pressed = 0;

	    if (input.isKeyDown(Input.KEY_UP)) {
	    	System.out.println("MOVE");
	        move(delta, m_V2f_velocity, m_V2f_position, m_f_theta);  // runs move method for car // move(delta);  // this.move(delta); 
	      
	    } 
	    else {
	    	m_V2f_velocity.set(0, 0);   // m_V2f_velocity = new Vector2f(0, 0); // This class has a new, empty vector above
	    }
		
	    
	    // Update sensors
		this.C_sensor.sensorUpdate(m_f_theta, m_V2f_position, m_V2f_velocity);
	}
	
	
	
	public void move(float delta, Vector2f V2f_velocity, Vector2f V2f_position, float theta) {
	    /*
	     * Acceleration = Force / Mass
	     * Velocity += Acceleration * ElapsedTime (delta)
	     * Position += Velocity * ElapsedTime (delta)
	     */
	    //System.out.println(delta); //debugging
	    //delta = delta/10; //debugging
		
		m_V2f_position = V2f_position;  // To guarantee passed in value is passed out as-is unless changed below
		m_V2f_velocity = V2f_velocity;
		//System.out.println("MOVE P and V" + m_V2f_position + "  " + m_V2f_velocity);
		  
		m_V2f_acceleration.set(0, 0);   //m_V2f_acceleration = new Vector2f(0, 0); // This class has a new, empty vector above
		m_angle = this.getImagePointer().getRotation();  //car.getRotation();
	    //System.out.println("Angle from image " + m_angle);
	    
	    if(m_angle > 0 && m_angle < 90){
	    	V2f_velocity.x = (float)Math.cos(Math.toRadians(m_angle));
	    	V2f_velocity.y = -(float)(Math.cos(Math.toRadians(m_angle)) / Math.tan(Math.toRadians(m_angle)));
	    }
	    else if(m_angle > 90 && m_angle < 180){
	    	V2f_velocity.x = -(float)Math.cos(Math.toRadians(m_angle));
	    	V2f_velocity.y = (float)(Math.cos(Math.toRadians(m_angle)) / Math.tan(Math.toRadians(m_angle)));
	    }
	    else if(m_angle < 0 && m_angle > -90){
	    	
	    	V2f_velocity.x = -(float)Math.cos(Math.toRadians(m_angle));
	    	V2f_velocity.y = (float)(Math.cos(Math.toRadians(m_angle)) / Math.tan(Math.toRadians(m_angle)));
	    }
	    else if(m_angle < -90 && m_angle > -180){
	    	V2f_velocity.x = (float)Math.cos(Math.toRadians(m_angle));
	    	V2f_velocity.y = -(float)(Math.cos(Math.toRadians(m_angle)) / Math.tan(Math.toRadians(m_angle)));
	    }
	    
	    //System.out.println("VELOCITY ADDED " + V2f_velocity);
	    m_V2f_position = m_V2f_position.add(V2f_velocity);
	    //System.out.println("NEW POSITION " + V2f_position);
	    
	    // Update sensors
	    this.C_sensor.sensorUpdate(theta, m_V2f_position, m_V2f_velocity);
	}
	
	
	public Vector2f angleToVector(double angle) {
	    return new Vector2f((float)Math.cos(angle), (float)Math.sin(angle));
	}

	
	// Get theta
	public float getTheta(){
		return m_f_theta;
	}
	
	// Get key pressed
	public int getKeyPressed(){
		return m_key_pressed;
	}
	
	// Get position
	public Vector2f getV2fPosition(){
	
		return m_V2f_position;
	}
	
	// Get velocity
	public Vector2f getV2fVelocity(){

		return m_V2f_velocity;
	}
	
	
	// Set entity mass
	public void setEntityMass(float mass) {
		this.m_mass = mass;
	}

	// String override
	public String toString(CEntity entity){

		if(this.m_boundingCircle != null){
			str_circle = "Bounding Circle" + "radius: " + this.m_boundingCircle.radius + "   center: " 
					+ "(" + this.m_boundingCircle.getCenterX() + "," + this.m_boundingCircle.getCenterY() + ")";
		}

		str_string1 = "ID: " + this.m_i_Id + "X pos:" + this.m_f_entityX + "Y pos" + this.m_f_entityY + "\n" + str_circle;

		return str_circle + "\n" + "str_string1";
	}

	
}
