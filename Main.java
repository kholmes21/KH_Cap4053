import org.newdawn.slick.*;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Main extends BasicGame{
	
	Rectangle wall1 = new Rectangle(400, 100, 30, 300);
	int key_pressed = 0;
	Image background;
	Image car;
	Image ray;
	float speed = 0.2f;
	float x = 200, y = 500;
	float angle_x = 0;
	float wallLineFrontX1 = 0, wallLineFrontY1 = 0;
	float wallLineFrontX2 = 0, wallLineFrontY2 = 0;
	float mass;
	double angle = 0;
	Vector2f acceleration;
	Vector2f force;
	Vector2f velocity;
	Vector2f position, position_temp;
	Circle user_range;
	
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
		Input input = gc.getInput();
		g.setColor(Color.white);
		g.fillRect(0, 0, 800, 600);
		
		car.draw(position.x, position.y, 40, 70);
		//The following 4 lines are for the range sensor
		//user_range.setCenterX(position.x + 20);
		//user_range.setCenterY(position.y + 35);
		//g.setColor(Color.blue);
		//g.draw(user_range);
		
		g.setColor(Color.black);
		g.fillRect(400, 100, 30, 300);
		g.fillRect(400, 70, 200, 30);
		
		car.setCenterOfRotation(20, 35);
		
		if(key_pressed == 1){
			car.rotate(.2f);
			if(car.getRotation() > 180)
				car.setRotation(-179);
		}
		else if(key_pressed == 2){
			car.rotate(-.2f);
			if(car.getRotation() < -179.8)
				car.setRotation(181);
		}
		
		g.drawLine(wallLineFrontX1 + 20, wallLineFrontY1, 
				wallLineFrontX2 + 20, wallLineFrontY2);
		
		if(input.isMouseButtonDown(Input.MOUSE_LEFT_BUTTON)){
			g.drawString("Mouse X: " + input.getMouseX() + " Mouse Y: " + input.getMouseY(), 400, 10);
		}
		
		g.drawString("Car Rotation: " + car.getRotation(), 100, 10);
		g.drawString("x: " + position.x + " y: " + position.y, 100, 25);
		
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		//background = new Image("background.jpg");
		car = new Image("car.png");
		position = new Vector2f(x, y);
	    velocity = new Vector2f(0, 0);
	    ray = new Image("ray.png");
	    user_range = new Circle(position.x, position.y, 90);
		
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
	    else if(input.isKeyPressed(Input.KEY_ESCAPE))
	    	System.exit(0);
	    else {
	        velocity = new Vector2f(0, 0);
	    }
		
	}
	
	public void move(float delta) {
		
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
	    else if(angle == 0){
	    	velocity.x = 0;
	    	velocity.y = -.2f;
	    }
	    else if(Math.abs(angle) < 181 && Math.abs(angle) > 179){
	    	velocity.x = 0;
	    	velocity.y = .2f;
	    }
	    else if(angle == 90){
	    	velocity.x = .2f;
	    	velocity.y = 0;
	    }
	    else if(angle == -90){
	    	velocity.x = -.2f;
	    	velocity.y = 0;
	    }
	    	
	    if(velocity.x < 0 || velocity.y < 0){
	    	velocity.x *= 2;
	    	velocity.y *= 2;
	    }
	    
	    normalizeVector(delta);
	    
	    wallLineFrontX1 = position.getX();
	    wallLineFrontY1 = position.getY() + car.getCenterOfRotationY();
	    wallLineFrontX2 = position.getX() + velocity.x * 400;
	    wallLineFrontY2 = position.getY() + velocity.y * 400 + car.getCenterOfRotationY();
	    
	    
	    position.add(velocity);
	    
	    /*The following lines are for the range sensor
	    user_range.setLocation(position.x + velocity.x, position.y + velocity.y);
	    
	    if(user_range.intersects(wall1))
	    	user_range.setLocation(position.x, position.y);
	    else{
	    	position.add(velocity);
	    }*/
	}
	
	public void normalizeVector(float delta) {
		//normalize vector
	    float normalNum = (float)Math.hypot((double)velocity.x, (double)velocity.y);
	    velocity.x /= normalNum;
	    velocity.y /= normalNum;
	    
	    //account for time displaced
	    velocity.x *= .2 * delta;
	    velocity.y *= .2 * delta;
	}

}
