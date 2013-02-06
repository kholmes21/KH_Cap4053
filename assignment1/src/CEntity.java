import org.newdawn.slick.Image;

/* This is the entity class for the construction of a single entity
 * 
 */
public class CEntity {
	
	// ID
	public int m_i_Id = 0;
	//public int type position bounding radius
	
	// Position
	public float m_f_entityX;
	public float m_f_entityY;
	
	
	// Entity Image
	public Image m_imgPtr_picture;
	public Image m_imgOriginal;
	
	// Other variables
	private String str_string = null;
	private String str_circle = null;
	
	
	// Constructor - Every entity has an ID, x and y position, an original picture that stays with it
	//					and a pointer to an image that it can use temporarily.
	public CEntity(float x_position, float y_position, int id, Image originalPicture){
		
		this.setXposition(x_position);
		this.setYposition(y_position);
		this.setEntityID(id);
		this.setImagePointer(originalPicture); // Initially the image pointer is set to original
		this.setOriginalImg(originalPicture);  // This is for returning to the original image
		
	}
	
	// Set x position
	public void setXposition(float x_position){
		this.m_f_entityX = x_position;
	}
	
	// Set y position
	public void setYposition(float y_position){
		this.m_f_entityY = y_position;
	}
	
	// Get x position
	public float getXposition(){
		return this.m_f_entityX;
	}
	
	// Get y position
	public float getYposition(){
		return this.m_f_entityY;
	}
	
	// Set entity ID
	public void setEntityID(int id){
		this.m_i_Id = id;
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
	
	
	// String override
	public String toString(CEntity entity){
		
		
		str_circle = "Parent Class - - No Bounding Circle";
		str_string = "ID: " + this.m_i_Id + "X pos:" + this.m_f_entityX + "Y pos" + this.m_f_entityY + "\n" + str_circle;
		
		return str_string;
	}
	
	
	
	
	
	
}
