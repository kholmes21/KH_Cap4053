import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;


public class CEnemyAgt extends CEntity {

	// Member variables
	int m_key_pressed = 0;
	int m_delta;
	float m_f_theta = 0;
	float m_mass;
	double m_d_angle = 0;
	double m_d_forwardSpeed = 0.2;
	double m_d_reverseSpeed = 0.1;
	Vector2f m_v2f_acceleration = new Vector2f();     // Note ** create empty vectors or 0 vectors here to protect Main
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
	
	//Constructor
	public CEnemyAgt(Vector2f position, int id, Image originalPicture, boolean boundingCircle){
		super(position, id, originalPicture);
		if(boundingCircle){
			this.setEntityPosition(position);
			this.m_boundingCircle = new Circle(0,0,1);
			this.setBoundingCircle(false);
			
			System.out.println("Enemy Bounding Circle" + this.m_boundingCircle);
		}
		
	}

	

	// Set bounding circle
	public void setBoundingCircle(boolean reset) {
		
		if(reset == true){
			this.m_boundingCircle.setLocation(this.m_v2f_position.x, this.m_v2f_position.y);
			this.m_boundingCircle.setRadii((float)((this.m_imgPtr_picture.getWidth()/2.0f) + 5), (
					float)((this.m_imgPtr_picture.getWidth()/2.0f) + 5));
			//this.m_boundingEllipse = new Ellipse(this.getXposition() + 30, this.getYposition() + 30,
			//		((float)(this.m_pgImg_picture.getWidth()/2.0f)), ((float)(this.m_pgImg_picture.getWidth()/2.0f)));
			System.out.println("Set bounding circle for image pointer" + this.m_v2f_position.x + " " + this.m_v2f_position.y);
		}
		else{
			this.m_boundingCircle.setLocation(this.m_v2f_position.x - 15, this.m_v2f_position.y);
			this.m_boundingCircle.setRadii((float)((this.m_imgOriginal.getWidth()/2.0f) + 5),
					(float)((this.m_imgOriginal.getWidth()/2.0f) + 5));
			//this.m_boundingEllipse = new Ellipse(this.getXposition() + 30, this.getYposition() + 30,
				//	((float)(picture.getWidth()/2.0f)), ((float)(picture.getWidth()/2.0f)));
			System.out.println("Set bounding circle for original image" + this.m_v2f_position.x + " " + this.m_v2f_position.y);
			
		}
	
	}
	
	// Update function for entity
	public void entityUpdate(int delta, Vector2f V2f_position, float f_theta, Input input){
		
		m_f_theta = f_theta;              // To guarantee that theta passed in is passed to sensor update if as-is unless changed
		//m_v2f_velocity = V2f_velocity;    // by one of the *if* statements below.
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
	    	move();  
	      
	    } 
	    /*
	    else {
	    	m_v2f_velocity.set(0, 0);   // m_V2f_velocity = new Vector2f(0, 0); // This class has a new, empty vector above
	    	m_d_forwardSpeed = 0;
	    }
		*/
	    
	   
	}
	
	
	
	public void move(){//(float delta, Vector2f V2f_velocity, Vector2f V2f_position, float theta) {
	   
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
	
	// Set position
	private void setEntityPosition(Vector2f position) {
		this.m_v2f_position = position;
	}
	
	/*
	// Get velocity
	public Vector2f getV2fVelocity(){

		return m_v2f_velocity;
	}

	// Set entity mass
	public void setEntityMass(float mass) {
		this.m_mass = mass;
	}
	*/
	
	
	// Get bounding circle if it exists
	public Circle getBoundingCircle(){
		return this.m_boundingCircle;
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

	
	
	
} // end CEnemyAgt class
