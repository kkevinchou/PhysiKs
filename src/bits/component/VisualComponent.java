package bits.component;

import org.lwjgl.opengl.GL11;

import bits.entity.*;

public class VisualComponent {
	private Bit entity;
	
	public VisualComponent(Bit entity) {
		this.entity = entity;
	}
	
	public void update() {
		draw();
	}
	
	public void draw() {
		GL11.glColor3f(0f, 0f, 1f);

		GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(entity.getX(), entity.getY());
				GL11.glVertex2f(entity.getX() + entity.getWidth(), entity.getY());
				GL11.glVertex2f(entity.getX() + entity.getWidth(), entity.getY() + entity.getHeight());
				GL11.glVertex2f(entity.getX(), entity.getY() + entity.getHeight());
			GL11.glEnd();
		GL11.glPopMatrix();
		
		GL11.glPushMatrix();
		GL11.glTranslatef(100, 100, 0);
		GL11.glScalef(50, 50, 1);

		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glVertex2f(0, 0);
		for(int i = 0; i <= 1000; i++){ //NUM_PIZZA_SLICES decides how round the circle looks.
		    double angle = Math.PI * 2 * i / 1000;
		    GL11.glVertex2f((float)Math.cos(angle), (float)Math.sin(angle));
		}
		GL11.glEnd();

		GL11.glPopMatrix();
	}
}
