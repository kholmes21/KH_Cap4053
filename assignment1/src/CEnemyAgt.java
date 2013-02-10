import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;


public class CEnemyAgt extends CEntity{

	
	
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
			this.m_boundingCircle.setLocation(this.m_v2f_position.x - 15, this.m_v2f_position.y);
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
	public void entityUpdate(Input input){
		
		
		// Locate mouse if it's over one of the agents
		if( Math.abs((int)((this.m_v2f_position.x + 20) - input.getMouseX())) < 20 &&  
				Math.abs((int)((this.m_v2f_position.y + 35) - input.getMouseY())) < 20 ){
			//System.out.println("Mouse Over" + this.getEntityID());
			
			if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
				//System.out.println("left down" + this.getEntityID());
				this.m_v2f_position.x = input.getMouseX() - 20;
				this.m_v2f_position.y = input.getMouseY() - 35;
				this.m_boundingCircle.setLocation(this.m_v2f_position.x - 15, this.m_v2f_position.y);
			}
		}
		
		
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


