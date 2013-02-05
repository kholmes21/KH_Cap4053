import org.newdawn.slick.*;
import org.newdawn.slick.geom.Vector2f;

public class Main extends BasicGame{
	
	// Main variables
	int key_pressed = 0;
	Image background;
	Image car;
	float speed = 0.2f;
	float x = 200, y = 500;
	float angle_x = 0;
	double angle = 0;
	Vector2f acceleration;
	Vector2f force;
	Vector2f velocity;
	Vector2f position;
	float mass;
	
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
		car.draw(position.x, position.y, 40, 70);
		g.setColor(Color.black);
		g.fillRect(400, 100, 30, 300);
		g.fillRect(400, 70, 200, 30);
		
		car.setCenterOfRotation(20, 50);
		if(key_pressed == 1)
			car.rotate(.2f);
		else if(key_pressed == 2)
			car.rotate(-.2f);
		
		g.drawString("Car Rotation: " + car.getRotation(), 100, 10);
		g.drawString("x: " + position.x + " y: " + position.y, 100, 25);
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		//background = new Image("background.jpg");
		car = new Image("car.png");
		position = new Vector2f(x, y);
	    velocity = new Vector2f(0, 0);
		
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		Input input = gc.getInput();
		
		if(input.isKeyDown(Input.KEY_LEFT))
			key_pressed = 2;
		else if(input.isKeyDown(Input.KEY_RIGHT))
			key_pressed = 1;
		else
			key_pressed = 0;

	    if (input.isKeyDown(Input.KEY_UP)) {
	        this.move(delta);
	    } 
	    else {
	        velocity = new Vector2f(0, 0);
	    }
		
	}
	
	public void move(float delta) {
	    /*
	     * Acceleration = Force / Mass
	     * Velocity += Acceleration * ElapsedTime (delta)
	     * Position += Velocity * ElapsedTime (delta)
	     */
	    //System.out.println(delta); //debugging
	    //delta = delta/10; //debugging
		
		acceleration = new Vector2f(0, 0);
	    angle = car.getRotation();
	    if(angle > 0 && angle < 90){
	    	velocity.x = (float)Math.cos(Math.toRadians(angle));
		    velocity.y = -(float)(Math.cos(Math.toRadians(angle)) / Math.tan(Math.toRadians(angle)));
	    }
	    else if(angle > 90 && angle < 180){
	    	velocity.x = -(float)Math.cos(Math.toRadians(angle));
		    velocity.y = (float)(Math.cos(Math.toRadians(angle)) / Math.tan(Math.toRadians(angle)));
	    }
	    else if(angle < 0 && angle > -90){
	    	velocity.x = -(float)Math.cos(Math.toRadians(angle));
		    velocity.y = (float)(Math.cos(Math.toRadians(angle)) / Math.tan(Math.toRadians(angle)));
	    }
	    else if(angle < -90 && angle > -180){
	    	velocity.x = (float)Math.cos(Math.toRadians(angle));
		    velocity.y = -(float)(Math.cos(Math.toRadians(angle)) / Math.tan(Math.toRadians(angle)));
	    }
	    
	    position = position.add(velocity);
	}
	public Vector2f angleToVector(double angle) {
	    return new Vector2f((float)Math.cos(angle), (float)Math.sin(angle));
	}

}
