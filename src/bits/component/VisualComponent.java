package bits.component;

import org.lwjgl.opengl.GL11;

import bits.entity.*;

public class VisualComponent {
	private Entity entity;
	
	public VisualComponent(Entity entity) {
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
	}
}
