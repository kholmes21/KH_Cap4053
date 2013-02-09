import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Vector2f;

/* This is the entity class for the construction of a single entity
 * 
 */
public class CEntity {
	
	// ID
	public int m_i_Id = 0;
	//public int type position bounding radius
	
	// Position
	Vector2f m_V2f_position;
	
	// Bounding Circle
	public Circle m_boundingCircle = null;
	
	// Entity Image
	public Image m_imgPtr_picture;
	public Image m_imgOriginal;
	
	// Other variables
	private String str_string = null;
	private String str_circle = null;
	
	
	// Constructor - Every entity has an ID, position vector, an original picture that stays with it
	//					and a pointer to an image that it can use temporarily.
	public CEntity(Vector2f position, int id, Image originalPicture){
		
		this.setLocationVector(position);
		this.setEntityID(id);
		this.setImagePointer(originalPicture); // Initially the image pointer is set to original
		this.setOriginalImg(originalPicture);  // This is for returning to the original image
		
	}
	
	// Set location vector
	public void setLocationVector(Vector2f position){
		this.m_V2f_position = position;
	}
	
	/*
	// Set y position
	public void setVeloc(float y_position){
		this.m_f_entityY = y_position;
	}
	*/
	// Get x position
	public float getXposition(){
		return this.m_V2f_position.x;
	}
	
	// Get y position
	public float getYposition(){
		return this.m_V2f_position.y;
	}
	
	// Set entity ID
	public void setEntityID(int id){
		this.m_i_Id = id;
	}
	
	// Get entity ID
	public int getEntityID(){
		return this.m_i_Id;
	}
	
	// Set picture - original
	private void setOriginalImg(Image picture){
		this.m_imgOriginal = picture;
	}
	
	// Get picture - some original
	public Image getOriginalPicture(){
		return this.m_imgOriginal;
	}

	// Set picture - some temporary picture
	public void setImagePointer(Image picture){
		this.m_imgPtr_picture = picture;
	}
	
	// Get picture - what picture is currently being pointed to
	public Image getImagePointer(){
		return this.m_imgPtr_picture;
	}
	
	// Get bounding circle if it exists
	public Circle getBoundingCircle(){
		return this.m_boundingCircle;
	}
	
	
	// String override
	public String toString(CEntity entity){
		
		
		str_circle = "Parent Class - - No Bounding Circle";
		str_string = "ID: " + this.m_i_Id + "X pos:" + this.m_V2f_position.x + "Y pos" + this.m_V2f_position.y + "\n" + str_circle;
		
		return str_string;
	}
	
	
	
	
	
	
}
