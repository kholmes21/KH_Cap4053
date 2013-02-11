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
	int m_delta;
	float m_f_theta = 0;
	float m_mass;
	double m_d_angle = 0;
	double m_d_forwardSpeed = 0.2;
	double m_d_reverseSpeed = 0.1;
	Vector2f m_v2f_acceleration = new Vector2f();     // Note ** create empty vectors or 0 vectors here to protect Main
	Vector2f m_v2f_force = new Vector2f();
	Vector2f m_v2f_velocity = new Vector2f();
	Vector2f m_v2f_position = new Vector2f();
	
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
	public CCarEntity(Vector2f position, int id, Image originalPicture, float mass, boolean boundingCircle){
		super(position, id, originalPicture);
		this.m_boundingCircle = new Circle(0,0,1);
		this.setBoundingCircle(false, originalPicture);
		this.setEntityMass(mass);
		// Sensor
		C_sensor = new CSensor(CSensor.OFF, CSensor.OFF, CSensor.OFF, this);
	}
		
	// Constructor - no bounding circle
	public CCarEntity(Vector2f position, int id, Image originalPicture, float mass){
		super(position, id, originalPicture);
		this.setEntityMass(mass);
		
		// Sensor
		C_sensor = new CSensor(CSensor.OFF, CSensor.OFF, CSensor.OFF, this);
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
	public void entityUpdate(int delta, Vector2f V2f_velocity, Vector2f V2f_position, float f_theta, Input input){
		
		m_f_theta = f_theta;              // To guarantee that theta passed in is passed to sensor update if as-is unless changed
		m_v2f_velocity = V2f_velocity;    // by one of the *if* statements below.
		m_v2f_position = V2f_position;
		m_delta = delta;
		//System.out.println("delta" + delta);
		
		
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
		
		
		// Moving the entity
	    if (input.isKeyDown(Input.KEY_UP)) {
	    	System.out.println("MOVE UP");
	    	
	    	// Runs move method for entity forward
	    	moveForward();  
	      
	    } 
	    else if(input.isKeyDown(Input.KEY_DOWN)){
	    	System.out.println("MOVE DOWN");
	    	
	    	// Runs move method for entity backward
	    	moveBackward();
	    }
	    else {
	    	m_v2f_velocity.set(0, 0);   // m_V2f_velocity = new Vector2f(0, 0); // This class has a new, empty vector above
	    	m_d_forwardSpeed = 0;
	    }
		
	    
	    // Update sensors
		this.C_sensor.sensorUpdate(m_f_theta, m_v2f_position, m_v2f_velocity);
	}
	
	
	
	public void moveForward(){//(float delta, Vector2f V2f_velocity, Vector2f V2f_position, float theta) {
	    /*
	     * Acceleration = Force / Mass
	     * Velocity += Acceleration * ElapsedTime (delta)
	     * Position += Velocity * ElapsedTime (delta)
	     */
	    //System.out.println(delta); //debugging
	    //delta = delta/10; //debugging
		
		//m_V2f_position = V2f_position;  // To guarantee passed in value is passed out as-is unless changed below
		//m_V2f_velocity = V2f_velocity;
		//System.out.println("MOVE P and V" + m_V2f_position + "  " + m_V2f_velocity);
		  
		m_v2f_acceleration.set(0, 0);   //m_V2f_acceleration = new Vector2f(0, 0); // This class has a new, empty vector above
		m_d_angle = (float)(Math.toRadians(this.getImagePointer().getRotation()));
		
	    //System.out.println("Angle from image " + m_d_angle);
	    
	    // Calculate a new position vector by adding a velocity vector
	    // Velocity vector has an angle m_angle from the entity's rotation
	    // and a magnitude that is hard coded and changes with (delta) up to a max value
	    
	    // Add to speed but limit to 2 (until I figure out the acceleration vector)
	    m_d_forwardSpeed += 0.02;	
	    if(m_d_forwardSpeed >= 2){
	    	m_d_forwardSpeed = 2;
	    }
	    else{
	    	m_d_forwardSpeed += 0.02;
	    }
	    
	    m_v2f_velocity.x = (float)(m_d_forwardSpeed * Math.sin(m_d_angle));
	    m_v2f_velocity.y = (float)((-1)*(m_d_forwardSpeed * Math.cos(m_d_angle))); // Must multiply y velocity by -1 to account for
	    																	// game window y actually being game y - (calculated y)
	    System.out.println("speed" + m_d_forwardSpeed);
	    
	    
	    System.out.println("VELOCITY ADDED " + m_v2f_velocity);
	    m_v2f_position = m_v2f_position.add(m_v2f_velocity);
	    System.out.println("NEW POSITION " + m_v2f_position);
	    
	    /*
	    if((user_range.intersects(agent1) || user_range.intersects(agent2))){
	    	if(agentBool == true)
	    		agentFinder();
	    }
	    else{
	    	printRadar = false;
	    	agent1Distance = -1;
	    	agent2Distance = -1;
	    }
		*/
	    
	    // Update sensors
	    this.C_sensor.sensorUpdate(m_f_theta, m_v2f_position, m_v2f_velocity);
	}
	
	
	

	
	public void moveBackward(){//(float delta, Vector2f V2f_velocity, Vector2f V2f_position, float theta) {
	    /*
	     * Acceleration = Force / Mass
	     * Velocity += Acceleration * ElapsedTime (delta)
	     * Position += Velocity * ElapsedTime (delta)
	     */
	    //System.out.println(delta); //debugging
	    //delta = delta/10; //debugging
		
		//m_V2f_position = V2f_position;  // To guarantee passed in value is passed out as-is unless changed below
		//m_V2f_velocity = V2f_velocity;
		//System.out.println("MOVE P and V" + m_V2f_position + "  " + m_V2f_velocity);
		  
		m_v2f_acceleration.set(0, 0);   //m_V2f_acceleration = new Vector2f(0, 0); // This class has a new, empty vector above
		m_d_angle = (float)(Math.toRadians(this.getImagePointer().getRotation()));
		
	    System.out.println("Angle from image " + m_d_angle);
	    /*
	    if(m_angle > 0 && m_angle < 90){
	    	m_V2f_velocity.x = (float)Math.cos(m_angle);
	    	m_V2f_velocity.y = -(float)(Math.cos(m_angle) / Math.tan(m_angle)); // Math.cos, etc require a double to work properly
	    }
	    else if(m_angle > 90 && m_angle < 180){
	    	m_V2f_velocity.x = -(float)Math.cos(m_angle);
	    	m_V2f_velocity.y = (float)(Math.cos(m_angle) / Math.tan(m_angle));
	    }
	    else if(m_angle < 0 && m_angle > -90){
	    	
	    	m_V2f_velocity.x = -(float)Math.cos(m_angle);
	    	m_V2f_velocity.y = (float)(Math.cos(m_angle) / Math.tan(m_angle));
	    }
	    else if(m_angle < -90 && m_angle > -180){
	    	m_V2f_velocity.x = (float)Math.cos(m_angle);
	    	m_V2f_velocity.y = -(float)(Math.cos(m_angle) / Math.tan(m_angle));
	    }
	    */
	    // Calculate a new position vector by adding a velocity vector
	    // Velocity vector has an angle m_angle from the entity's rotation
	    // and a magnitude that is hard coded and changes with (delta) up to a max value
	    
	    // Add to speed but limit to 2 (until I figure out the acceleration vector)
	    m_d_reverseSpeed += 0.02;	
	    if(m_d_reverseSpeed >= 1){
	    	m_d_reverseSpeed = 1;
	    }
	    else{
	    	m_d_reverseSpeed += 0.02;
	    }
	    
	    m_v2f_velocity.x = (float)((-1)*(m_d_reverseSpeed * Math.sin(m_d_angle)));
	    m_v2f_velocity.y = (float)((m_d_reverseSpeed * Math.cos(m_d_angle))); // Must multiply y velocity by -1 to account for
	    																	// game window y actually being game y - (calculated y)
	    System.out.println("reverse speed" + m_d_reverseSpeed);
	    
	    
	    System.out.println("VELOCITY ADDED " + m_v2f_velocity);
	    m_v2f_position = m_v2f_position.add(m_v2f_velocity);
	    System.out.println("NEW POSITION " + m_v2f_position);
	    
	    // Update sensors
	    this.C_sensor.sensorUpdate(m_f_theta, m_v2f_position, m_v2f_velocity);
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
	
		return m_v2f_position;
	}
	
	// Get velocity
	public Vector2f getV2fVelocity(){

		return m_v2f_velocity;
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

		str_string1 = "ID: " + this.m_i_Id + "X pos:" + this.m_v2f_position.x + "Y pos" + this.m_v2f_position.y + "\n" + str_circle;

		return str_circle + "\n" + "str_string1";
	}

	
}
