import java.util.ArrayList;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Line;
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
	Input input;
	Input keyboard;
	
	// Car entity
	private int id = 1;
	public CCarEntity C_car;
	
	// Mouse tracking
	public float f_mouseX = 0;
	public float f_mouseY = 0;
	
	// Walls
	public static Shape obstacle1;
	public static Shape obstacle2;
	public static Line westLow;
	public static Line northLow;
	public static Line eastLow;
	public static Line southLow;
	public static Line westHigh;
	public static Line northHigh;
	public static Line eastHigh;
	public static Line southHigh;
	public static ArrayList <Line> lineArray;  //= new ArrayList<Line>();
	
	
	
	
	// Main
	public Main(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	
	// Game start
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
		g.setColor(Color.white);
		g.draw(westHigh);	g.draw(eastHigh);	g.draw(westLow); g.draw(eastLow);
		
		// Player objects
		tempCarImage = C_car.getImagePointer();
		tempCarImage.setCenterOfRotation(20, 50);  //  car.setCenterOfRotation(20, 50);
		C_car.getImagePointer().draw(C_car.getV2fPosition().x, C_car.getV2fPosition().y, 40 , 70); //car.draw(position.x, position.y, 40, 70);
											//V2f_position.x, V2f_position.y,
		
		// Image rotation
		if(key_pressed == 1)
			
			tempCarImage.rotate(f_theta+.2f); //rotate(.2f);
		else if(key_pressed == 2)
			tempCarImage.rotate(f_theta-.2f); // rotate(-.2f);
		
		// Render sensors
		C_car.C_sensor.sensorRender(g);
		
		// Display data
		g.setColor(new Color(0, 0, 0));
		g.drawString("Car Rotation: " + tempCarImage.getRotation(), 10, 20);
		g.drawString("x: " + V2f_position.x + " y: " + V2f_position.y, 10, 35);
		g.drawString("Mouse x: " + f_mouseX + "  y: " + f_mouseY, 10, 95);
		
		
		
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
		
		lineArray = new ArrayList<Line>();
		
		// Lines to follow walls
		northHigh = new Line(160,180,600,180);
		westHigh = new Line(160,180,160,230);	eastHigh = new Line(600,180,600,230);
		southHigh = new Line(160,230,600,230);
		lineArray.add(northHigh);	lineArray.add(westHigh);	lineArray.add(eastHigh);	lineArray.add(southHigh);
		
		northLow = new Line(300,250,350,250);
		westLow = new Line(300,250,300,500);	eastLow = new Line(350,250,350,500);
		southLow = new Line(300,500,350,500);
		lineArray.add(northLow);	lineArray.add(westLow); 	lineArray.add(eastLow);		lineArray.add(southLow);
		System.out.println("First");
		
		
		
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		
		C_car.entityUpdate(delta, V2f_velocity, V2f_position, f_theta, input);
		f_theta = C_car.getTheta();
		//V2f_velocity = C_car.getV2fVelocity();
		//System.out.println("----------" + (f_theta = C_car.getTheta()));
		key_pressed = C_car.getKeyPressed();
		
		
		if(gc.getInput().isKeyPressed(Input.KEY_W)){
			C_car.C_sensor.rangeFinderOnOff(!C_car.C_sensor.isOnOFf("range"));
			System.out.println("Range finder on/off !!!");
		}
		
		
		// Mouse location
		f_mouseX = input.getMouseX();
		f_mouseY = input.getMouseY();
	}
	
	
}
