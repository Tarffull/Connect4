import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Marker extends JPanel {
	private Color color;
	private int pos;
	JLabel poslabel;

	public Marker(MarkerType color, int pos) {
		this.pos = pos;
		switch (color) {
		case EMPTY:
			this.color = Color.WHITE;
			break;
		case RED:
			this.color = Color.RED;
			break;
		case YELLOW:
			this.color = Color.YELLOW;
			break;
		}

		// poslabel = new JLabel(String.valueOf(pos/7) + "/" +
		// String.valueOf(pos%7));
		// add(poslabel);
	}

	public void setNewColor() {
		MarkerType marker = Game.getBoardMarker(pos);
		switch (marker) {
		case EMPTY:
			this.color = Color.WHITE;
			break;
		case RED:
			this.color = Color.RED;
			break;
		case YELLOW:
			this.color = Color.YELLOW;
			break;
		}
	}

	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		super.paint(g2d);
		setNewColor();
		setBackground(Board.getColor());
		g2d.setColor(color);

		g2d.fillOval((getWidth() - 90) / 2, (getHeight() - 90) / 2, 90, 90);

	}

	public int getPos() {
		return pos;
	}

}
