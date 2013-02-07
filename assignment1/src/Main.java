import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

public class Main extends BasicGame{
	
	// Main variables
	int key_pressed = 0;
	float temp = 0;
	Image background;
	Image tempCarImage;
	Image carImage;
	float speed = 0.2f;
	float f_carX = 200, f_carY = 500;
	float f_theta = 0;
	float f_angleX = 0;
	double d_angle = 0;
	Vector2f V2f_acceleration;
	Vector2f V2f_force;
	Vector2f V2f_velocity;
	Vector2f V2f_position;
	float f_carMass;
	
	// Car entity
	private int id = 1;
	public CCarEntity C_car;
	
	// Walls
	public static Shape obstacle1 = null;
	public static Shape obstacle2 = null;
	
	
	public Main(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new Main("Assignment 1"));
		app.setDisplayMode(800, 600, false);
		app.start();

	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		// Background
		background.draw();
		//g.setColor(Color.white);
		//g.fillRect(0, 0, 800, 600);
		
		// Walls for collision
		g.setColor(new Color(189,153,167));
		g.fill(obstacle1);
		g.fill(obstacle2);
		
		// Player objects
		tempCarImage = C_car.getImagePointer();
		tempCarImage.setCenterOfRotation(20, 50);  //  car.setCenterOfRotation(20, 50);
		C_car.getImagePointer().draw(C_car.getV2fPosition().x, C_car.getV2fPosition().y, 40 , 70); //car.draw(position.x, position.y, 40, 70);
											//V2f_position.x, V2f_position.y,
		
		// Image rotation
		if(key_pressed == 1)
			
			tempCarImage.rotate(f_theta); //rotate(.2f);
		else if(key_pressed == 2)
			tempCarImage.rotate(f_theta); // rotate(-.2f);
		
		// Render sensors
		C_car.C_sensor.sensorRender(g);
		
		// Display data
		g.setColor(new Color(0, 0, 0));
		g.drawString("Car Rotation: " + tempCarImage.getRotation(), 10, 20);
		g.drawString("x: " + V2f_position.x + " y: " + V2f_position.y, 10, 35);
		
		
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		// Background
		background = new Image("background.jpg");
		
		// Entities
		carImage = new Image("car.png");
		C_car = new CCarEntity(f_carX, f_carY, id, carImage, f_carMass); // No bounding circle
		
		// Vector2f
		V2f_position  = new Vector2f(f_carX, f_carY);
		V2f_velocity = new Vector2f(0, 0);
		
		// Walls
		float []points = {300,250,350,250,350,500,300,500}; // y coordinates are (max y - (coordinate))
		float []points2 = {160, 180, 600, 180, 600, 230, 160, 230};
		obstacle1 = new Polygon(points);
		obstacle2 = new Polygon(points2);
		
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		
		C_car.entityUpdate(delta, V2f_velocity, V2f_position, f_theta, input);
		
		//V2f_velocity = C_car.getV2fVelocity();
		System.out.println("----------" + (f_theta = C_car.getTheta()));
		key_pressed = C_car.getKeyPressed();
		
		
		/*
		if(input.isKeyDown(Input.KEY_LEFT)){
			key_pressed = 2;
			f_theta = (f_theta - 1.0f) * ((float)delta/100);
		}
		else if(input.isKeyDown(Input.KEY_RIGHT)){
			key_pressed = 1;
			f_theta = (f_theta + 1.0f) * ((float)delta/100);
		}
		else
			key_pressed = 0;

	    if (input.isKeyDown(Input.KEY_UP)) {
	        C_car.move(delta, V2f_velocity, V2f_position, f_theta);  // runs move method for car // move(delta);  // this.move(delta); 
	      
	    } 
	    else {
	    	V2f_velocity = new Vector2f(0, 0);
	    }
	    */
		
	}
	
	
}
