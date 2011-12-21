package repast.simphony.visualization.gui.styleBuilder;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.geotools.renderer.style.Java2DMark;

public class PreviewIcon2D extends JLabel implements PreviewIcon {

	private GeneralPath polygonShape;

	private Shape shape;
	private Shape previewShape;
	private Image iconImage;

	private String mark = null;

	private Color fillColor = Color.WHITE;
	private double fillOpacity = 1;
	private Color outlineColor = Color.BLACK;
	private double outlineOpacity = 1;
	private double outlineThickness = 1;
	private float markSize = 6;
	private Image image;
	private Font font = new Font("Arial",Font.PLAIN,12);
	private Color fontColor = Color.BLACK;

	public PreviewIcon2D() {
		image = new BufferedImage(200, 100, BufferedImage.TYPE_4BYTE_ABGR);
		setText("");
		this.setIcon(new ImageIcon(image));
		polygonShape = new GeneralPath();
		polygonShape.moveTo(10, 10);
		polygonShape.lineTo(90, 30);
		polygonShape.lineTo(70, 90);
		polygonShape.lineTo(10, 90);
		polygonShape.closePath();
		shape = polygonShape;

		updatePreview();
	}

	public void updatePreview() {
		Graphics2D g2d = (Graphics2D) image.getGraphics();
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, 200, 100);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(fillColor);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				(float) fillOpacity));

		if (iconImage == null){
			g2d.fill(shape);
			g2d.setColor(outlineColor);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
					(float) outlineOpacity));
			g2d.setStroke(new BasicStroke((float) outlineThickness));
			g2d.draw(shape);
		}
		else{
			// the 2*size is arbitrary for showing the preview
			double xscale = 2 * markSize / iconImage.getHeight(null);
			double yscale = 2 * markSize / iconImage.getHeight(null); 
			double x = 80;
			double y = 20;
			AffineTransform transform = new AffineTransform(xscale,0,0,yscale,x,y);
//			g2d.drawImage(iconImage, 80, 20, null);
			g2d.drawImage(iconImage, transform, null);
		}
		float minX = ((Double)shape.getBounds2D().getBounds2D().getMinX()).floatValue();
		float maxX = ((Double)shape.getBounds2D().getBounds2D().getMaxX()).floatValue();
		float minY = ((Double)shape.getBounds2D().getBounds2D().getMinY()).floatValue();
		float maxY = ((Double)shape.getBounds2D().getBounds2D().getMaxY()).floatValue();

		g2d.setColor(fontColor);
		g2d.setFont(font);

		g2d.drawString("Sample Label", 50, maxY + font.getSize2D());

		this.setIcon(new ImageIcon(image));
//		}
//		else {
//		this.setIcon(new ImageIcon(iconImage));
//		}
	}

	public void setEditorFontColor(Color color){
		fontColor = color;
		updatePreview();
	}

	public void setEditorFont(Font font){
		this.font = font;
		updatePreview();
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
		updatePreview();
	}

	public void setFillOpacity(double fillOpacity) {
		this.fillOpacity = fillOpacity;
		updatePreview();
	}

	public void setMark(String mark) {
		this.mark = mark;
		updateShape();
		updatePreview();
	}

	public void setShape(Shape shape){
		this.previewShape = shape;
		updateShape();
		updatePreview();
	}

	public void setShapeToLine() {
		GeneralPath path = new GeneralPath();
		path.moveTo(10, 10);
		path.lineTo(90, 90);
		shape = path;
		updatePreview();
	}

	private void updateShape() {
		if (this.mark != null){
			shape = AffineTransform.getScaleInstance(markSize, markSize)
			.createTransformedShape(Java2DMark.getWellKnownMark(mark));
		}
		else{
			shape = AffineTransform.getScaleInstance(markSize, markSize)
			.createTransformedShape(this.previewShape);
		}
		AffineTransform transform = AffineTransform
		.getTranslateInstance(100, 50);
		shape = transform.createTransformedShape(shape);
	}

	public void setOutlineColor(Color outlineColor) {
		this.outlineColor = outlineColor;
		updatePreview();
	}

	public void setOutlineOpacity(double outlineOpacity) {
		this.outlineOpacity = outlineOpacity;
		updatePreview();
	}

	public void setMarkSize(float markSize) {
		this.markSize = markSize;
		updateShape();
		updatePreview();
	}

	public void setOutlineThickness(double outlineThickness) {
		this.outlineThickness = outlineThickness;
		updatePreview();
	}

	public void setIconFile(File file) {
		if (file == null){
			iconImage = null;
			updatePreview();
			return;
		}

		else try {
			iconImage = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		updatePreview();
	}
}