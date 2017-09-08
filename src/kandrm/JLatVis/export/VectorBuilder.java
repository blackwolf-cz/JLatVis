package kandrm.JLatVis.export;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import math.geom2d.AffineTransform2D;
import math.geom2d.Box2D;
import math.geom2d.Point2D;
import math.geom2d.Shape2D;
import math.geom2d.conic.Ellipse2D;
import math.geom2d.line.Line2D;
import math.geom2d.polygon.Polygon2D;

/**
 *
 * @author Michal Kandr
 */
public abstract class VectorBuilder {
    public class VectorBuilderException extends Exception{
        public VectorBuilderException() {
            super();
        }
        public VectorBuilderException(String message) {
            super(message);
        }
        public VectorBuilderException(String message, Throwable cause) {
            super(message, cause);
        }
        public VectorBuilderException(Throwable cause) {
            super(cause);
        }
    }

    protected static final BasicStroke DEFAULT_DRAW_STROKE = new BasicStroke(1);
    protected static enum PaintType {Draw, Fill};

    protected Integer width = null;
    protected Integer height = null;
    protected Point2D padding = null;

    protected Color defaultColor = null;
    protected BasicStroke defaultStroke = null;
    protected Font defaultFont = null;
    protected Float defaultOpacity = null;

    protected OutputStream stream = null;
    protected StringBuffer doc = new StringBuffer();
    protected boolean closed = false;

    public VectorBuilder(OutputStream stream, int width, int height, Point2D padding){
        this.width = width;
        this.height = height;
        this.stream = stream;
    }
    public VectorBuilder(OutputStream stream, int width, int height){
        this(stream, width, height, null);
    }

    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) throws VectorBuilderException {
        this.height = height;
        if(closed){
            throw new VectorBuilderException("Document is closed.");
        }
    }

    public Integer getWidth() {
        return width;
    }
    public void setWidth(Integer width) throws VectorBuilderException {
        this.width = width;
        if(closed){
            throw new VectorBuilderException("Document is closed.");
        }
    }

    public Point2D getPadding(){
        return padding;
    }
    public void setPadding(Point2D padding){
        this.padding = padding;
    }
    public void setPadding(double left, double top){
        setPadding(new Point2D(left, top));
    }

    public Color getDefaultColor() {
        return defaultColor;
    }
    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Font getDefaultFont() {
        return defaultFont;
    }
    public void setDefaultFont(Font defaultFont) {
        this.defaultFont = defaultFont;
    }

    public BasicStroke getDefaultStroke() {
        return defaultStroke;
    }
    public void setDefaultStroke(BasicStroke defaultStroke) {
        this.defaultStroke = defaultStroke;
    }

    public Float getDefaultOpacity() {
        return defaultOpacity;
    }
    public void setDefaultOpacity(Float defaultOpacity) {
        this.defaultOpacity = defaultOpacity;
    }


    public void fill(Shape2D shape, Color color, Float opacity) throws VectorBuilderException{
        if(color == null){
            throw new VectorBuilderException("No shape color specified.");
        }
        paint(shape, PaintType.Fill, null, color, opacity);
    }

    public void fill(Shape2D shape, Color color) throws VectorBuilderException{
        fill(shape, color, defaultOpacity);
    }
    public void fill(Shape2D shape) throws VectorBuilderException{
        fill(shape, defaultColor, defaultOpacity);
    }

    public void fill(Box2D shape, Color color, Float opacity) throws VectorBuilderException{
        fill(shape.getAsRectangle(), color, opacity);
    }
    public void fill(Box2D shape, Color color) throws VectorBuilderException{
        fill(shape, color, defaultOpacity);
    }
    public void fill(Box2D shape) throws VectorBuilderException{
        fill(shape, defaultColor, defaultOpacity);
    }



    public void draw(Shape2D shape, BasicStroke stroke, Color color, Float opacity) throws VectorBuilderException{
        if(stroke == null){
            stroke = DEFAULT_DRAW_STROKE;
        }
        if(color == null){
            throw new VectorBuilderException("No shape color specified.");
        }
        paint(shape, PaintType.Draw, stroke, color, opacity);
    }

    public void draw(Shape2D shape, BasicStroke stroke, Color color) throws VectorBuilderException{
        draw(shape, stroke, color, defaultOpacity);
    }
    public void draw(Shape2D shape) throws VectorBuilderException{
        draw(shape, defaultStroke, defaultColor, defaultOpacity);
    }
    public void draw(Shape2D shape, BasicStroke stroke) throws VectorBuilderException{
        draw(shape, stroke, defaultColor, defaultOpacity);
    }

    public void draw(Box2D shape, BasicStroke stroke, Color color, Float opacity) throws VectorBuilderException{
        draw(shape.getAsRectangle(), stroke, color, opacity);
    }
    public void draw(Box2D shape, BasicStroke stroke, Color color) throws VectorBuilderException{
        draw(shape, stroke, color, defaultOpacity);
    }
    public void draw(Box2D shape) throws VectorBuilderException{
        draw(shape, defaultStroke, defaultColor, defaultOpacity);
    }
    public void draw(Box2D shape, BasicStroke stroke) throws VectorBuilderException{
        draw(shape, stroke, defaultColor, defaultOpacity);
    }


    public void write(String text, int x, int y, Float w, Float h, Font font, Color color, Float opacity) throws VectorBuilderException{
        if(font == null){
            throw new VectorBuilderException("No text font specified.");
        }
        if(color == null){
            throw new VectorBuilderException("No text color specified.");
        }
        if(closed){
            throw new VectorBuilderException("Document is closed.");
        }
        if(padding != null){
            x += padding.getX();
            y += padding.getY();
        }
        doWrite(text, x, y, w, h, font, color, opacity);
    }

    public void write(String text, int x, int y, Float w, Float h, Font font, Color color) throws VectorBuilderException{
        write(text, x, y, w, h, font, color, defaultOpacity);
    }
    public void write(String text, int x, int y, int w, int h, Font font, Color color, Float opacity) throws VectorBuilderException{
        write(text, x, y, (float)w, (float)h, font, color, opacity);
    }
    public void write(String text, int x, int y, int w, int h, Font font, Color color) throws VectorBuilderException{
        write(text, x, y, w, h, font, color, defaultOpacity);
    }
    public void write(String text, int x, int y, Float w, Float h) throws VectorBuilderException{
        write(text, x, y, w, h, defaultFont, defaultColor, defaultOpacity);
    }
    public void write(String text, int x, int y, int w, int h) throws VectorBuilderException{
        write(text, x, y, (float)w, (float)h, defaultFont, defaultColor, defaultOpacity);
    }
    public void write(String text, int x, int y) throws VectorBuilderException{
        write(text, x, y, null, null, defaultFont, defaultColor, defaultOpacity);
    }
    public void write(String text, int x, int y, Font font, Color color) throws VectorBuilderException{
        write(text, x, y, null, null, font, color, defaultOpacity);
    }

    public void close() throws IOException{
        closed = true;
        stream.write(doc.toString().getBytes(Charset.forName("utf-8")));
        stream.flush();
        stream.close();
    }


    protected void paint(Shape2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity) throws VectorBuilderException{
        if(closed){
            throw new VectorBuilderException("Document is closed.");
        }
        if(padding != null){
            shape = shape.transform(AffineTransform2D.createTranslation(padding.getX(), padding.getY()));
        }
        if(shape instanceof Ellipse2D){
            paint((Ellipse2D)shape, paintType, stroke, color, opacity);
        } else if(shape instanceof Line2D){
            paint((Line2D)shape, paintType, stroke, color, opacity);
        } else if(shape instanceof Polygon2D){
            paint((Polygon2D)shape, paintType, stroke, color, opacity);
        } else {
            throw new VectorBuilderException("Instances of class "+shape.getClass()+" are not supported by "+this.getClass()+".");
        }
    }
    abstract protected void paint(Ellipse2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity);
    abstract protected void paint(Line2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity);
    abstract protected void paint(Polygon2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity);

    abstract protected void doWrite(String text, int x, int y, Float w, Float h, Font font, Color color, Float opacity) throws VectorBuilderException;
}
