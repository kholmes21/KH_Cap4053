import org.newdawn.slick.*;
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
		g.setColor(Color.white);
		g.fillRect(0, 0, 800, 600);


		g.setColor(Color.black);
		g.fillRect(400, 100, 30, 300);
		g.fillRect(400, 70, 200, 30);
		
		tempCarImage = C_car.getImagePointer();
		tempCarImage.setCenterOfRotation(20, 50);  //  car.setCenterOfRotation(20, 50);
		C_car.getImagePointer().draw(V2f_position.x, V2f_position.y, 40 , 70); //car.draw(position.x, position.y, 40, 70);
		
		
		if(key_pressed == 1)
			
			tempCarImage.rotate(f_theta); //rotate(.2f);
		else if(key_pressed == 2)
			tempCarImage.rotate(f_theta); // rotate(-.2f);
	
		g.draw(C_car.C_sensor.line);
		g.drawString("Car Rotation: " + tempCarImage.getRotation(), 100, 10);
		g.drawString("x: " + V2f_position.x + " y: " + V2f_position.y, 100, 25);
		
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		//background = new Image("background.jpg");
		carImage = new Image("car.png");
		V2f_position  = new Vector2f(f_carX, f_carY);
		V2f_velocity = new Vector2f(0, 0);
		C_car = new CCarEntity(f_carX, f_carY, id, carImage, f_carMass); // No bounding circle
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
