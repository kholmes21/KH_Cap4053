import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Ellipse;
import org.newdawn.slick.geom.Vector2f;


public class CShipEntity extends CEntity {

	// ID
	@SuppressWarnings("unused")
	private int m_i_id;
	@SuppressWarnings("unused")
	private float m_mass;


	// Image
	public  Image m_imgPtr_ship;
	private Image m_pgImg_picture;


	// Position
	public float m_f_shipX;
	public float m_f_shipY;
	
	// Bounding Circle
	public Ellipse m_boundingEllipse = new Ellipse(0.0f,0.0f,0.0f,0.0f);
	
	// Sensor
	CSensor C_sensor;
	

	// Constructor
	public CShipEntity(Vector2f position, int id, Image originalPicture, Circle boundingCircle, float mass){
		super(position, id, originalPicture);
		// Bounding ellipse matched to the max dimension of entity
		this.setM_boundingEllipse(false, m_imgPtr_ship);
		this.setEntityMass(mass);

		// Sensor
		C_sensor = new CSensor(CSensor.ON, CSensor.OFF, CSensor.OFF, this);
	}


	// Constructor - no bounding circle
	public CShipEntity(Vector2f position, int id, Image originalPicture, float mass){
		super(position, id, originalPicture);
		this.setEntityMass(mass);
		
		// Sensor
		C_sensor = new CSensor(CSensor.ON, CSensor.OFF, CSensor.OFF, this);
	}


	// Set entity mass
	public void setEntityMass(float mass) {
		this.m_mass = mass;
	}
	
	// get picture
	public Image getM_imgPtr_picture() {
		return m_imgPtr_ship;
	}

	// set picture
	public void setM_imgPtr_picture(Image picture) {
		
		this.m_imgPtr_ship = picture;
		
		// On new picture set, readjust bounding radius
		// Bounding circle matched to the max dimension of entity
		this.setM_boundingEllipse(false, picture);
		
	}

	// Reset original picture
	public void resetM_imgPtr_ship(){
		this.m_imgPtr_ship = m_pgImg_picture;
		this.setM_boundingEllipse(true, null);
	}
	
	// Set bounding shape
	private void setM_boundingEllipse(boolean reset, Image picture){
		if(reset == true){
			this.m_boundingEllipse.setLocation(this.getXposition(), this.getYposition());
			this.m_boundingEllipse.setRadii((float)(this.m_pgImg_picture.getWidth()/2.0f), (float)(this.m_pgImg_picture.getWidth()/2.0f));
			//this.m_boundingEllipse = new Ellipse(this.getXposition() + 30, this.getYposition() + 30,
			//		((float)(this.m_pgImg_picture.getWidth()/2.0f)), ((float)(this.m_pgImg_picture.getWidth()/2.0f)));
		}
		else{
			this.m_boundingEllipse.setLocation(this.getXposition(), this.getYposition());
			this.m_boundingEllipse.setRadii((float)(picture.getWidth()/2.0f), (float)(picture.getWidth()/2.0f));
			//this.m_boundingEllipse = new Ellipse(this.getXposition() + 30, this.getYposition() + 30,
				//	((float)(picture.getWidth()/2.0f)), ((float)(picture.getWidth()/2.0f)));
			
		}
	}
	
}
