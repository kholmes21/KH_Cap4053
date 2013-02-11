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
	Vector2f v2f_acceleration;
	Vector2f v2f_force;
	Vector2f v2f_velocity;
	Vector2f v2f_position;
	float f_carMass;
	Input input;
	Input keyboard;
	
	// Other agents
	Image image_agent1;
	Image image_agent2;
	Image image_agent3;
	float f_thetaAgt1 = 0;
	float f_thetaAgt2 = 0;
	float f_thetaAgt3 = 0;
	
	Vector2f v2f_positionAgt1;
		
	Vector2f v2f_positionAgt2;	// get these working later
	Vector2f v2f_velocityAgt2;
	Vector2f v2f_positionAgt3;	
	
	// Car entity
	private int id = 1;  // ID 1 is for the player entity
	public CCarEntity C_car;
	
	// Enemy entities
	public CEnemyAgt C_agent1;
	public CEnemyAgt C_agent2;
	public CEnemyAgt C_agent3;
	public static ArrayList<CEnemyAgt> agentArray;
	public static final boolean ON = true;
	public static final boolean OFF = false;
	
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
		g.setColor(new Color(0,0,0));	//g.setColor(new Color(189,153,167));
		g.fill(obstacle1);
		g.fill(obstacle2);
		//g.setColor(Color.white);
		//g.draw(westHigh);	g.draw(eastHigh);	g.draw(westLow); g.draw(eastLow);
		
		// Player objects
		tempCarImage = C_car.getImagePointer();
		tempCarImage.setCenterOfRotation(20, 50);  //  car.setCenterOfRotation(20, 50);
		C_car.getImagePointer().draw(C_car.getV2fPosition().x, C_car.getV2fPosition().y, 40 , 70);
		
		// Enemy agents
		
		image_agent1.setCenterOfRotation(20, 50);
		C_agent1.getImagePointer().draw(C_agent1.getV2fPosition().getX(), C_agent1.getV2fPosition().getY(), 40, 70);
		g.draw(C_agent1.getBoundingCircle());
		
		
		
		
		image_agent2.setCenterOfRotation(25, 50);
		C_agent2.getImagePointer().draw(C_agent2.getV2fPosition().x, C_agent2.getV2fPosition().y, 40, 70);
		g.draw(C_agent2.getBoundingCircle());
		
		image_agent3.setCenterOfRotation(20, 50);
		C_agent3.getImagePointer().draw(C_agent3.getV2fPosition().x, C_agent3.getV2fPosition().y, 40, 70);
		g.draw(C_agent3.getBoundingCircle());
		
						
		
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
		g.drawString("x: " + v2f_position.x + " y: " + v2f_position.y, 10, 35);
		g.drawString("Mouse x: " + f_mouseX + "  y: " + f_mouseY, 290, 10);
		
		
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		
		// Background
		background = new Image("background.jpg");
		
		// Vector2f
		v2f_position  = new Vector2f(f_carX, f_carY);
		v2f_velocity = new Vector2f(0, 0);
		v2f_acceleration = new Vector2f(0,0);
		v2f_force = new Vector2f(0,0);
		
		v2f_positionAgt1 = new Vector2f(600.0f,500.0f);
		
		v2f_positionAgt2 = new Vector2f(600.0f,300.0f);
		v2f_positionAgt3 = new Vector2f(550.0f,250.0f);
		
		
		// Agent array
		agentArray = new ArrayList<CEnemyAgt>();
		
		// Entities
		carImage = new Image("car.png");
		image_agent1 = new Image("agent1.png");
		image_agent2 = new Image("agent2.png");
		image_agent3 = new Image("agent3.png");
		
		
		
		C_car = new CCarEntity(v2f_position, id, carImage, f_carMass); // No bounding circle
		
		for(CEnemyAgt e : agentArray){
			System.out.println(e);
		}
		C_agent1 = new CEnemyAgt(v2f_positionAgt1, 1, image_agent1, ON);	agentArray.add(C_agent1); 
		C_agent2 = new CEnemyAgt(v2f_positionAgt2, 2, image_agent2, ON); 	agentArray.add(C_agent2);
		C_agent3 = new CEnemyAgt(v2f_positionAgt3, 3, image_agent3, ON); 	agentArray.add(C_agent3);
		//C_agent4 = new CEnemyAgt(v2f_positionAgt4, 44, image_agent4, ON); 	agentArray.add(C_agent4);
		
		
				 
		
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
		
		// Entity updates
		C_car.entityUpdate(delta, v2f_velocity, v2f_position, f_theta, input);
		f_theta = C_car.getTheta();
		
		key_pressed = C_car.getKeyPressed();
		
		// Turn Range Finder(wall sensor) on/off -- toggle
		if(gc.getInput().isKeyPressed(Input.KEY_W) && (C_car.m_i_Id == 1)){
			C_car.C_sensor.rangeFinderOnOff(!C_car.C_sensor.isOnOff("range"));
			System.out.println("Range finder on/off !!!");
		}
		
		// Turn Pie Slice Sensor on/off -- toggle
		if(gc.getInput().isKeyPressed(Input.KEY_P) && (C_car.m_i_Id == 1)){
			C_car.C_sensor.pieSliceSensorOnOff(!C_car.C_sensor.isOnOff("pie"));
			System.out.println("Pie Slice Sensor on/off !!!");
		}
		
		// Turn Agent Finder on/off -- toggle
		if(gc.getInput().isKeyPressed(Input.KEY_A) && (C_car.m_i_Id == 1)){
			C_car.C_sensor.agentFinderOnOff(!C_car.C_sensor.isOnOff("agent"));
			System.out.println("Agent Finder on/off !!!");
		}
		
		// Enemy agent updates
		C_agent1.entityUpdate(input);
		C_agent2.entityUpdate(input);
		C_agent3.entityUpdate(input);
		
		
				
		// Mouse location
		f_mouseX = input.getMouseX();
		f_mouseY = input.getMouseY();
	}

	
	
	
}
