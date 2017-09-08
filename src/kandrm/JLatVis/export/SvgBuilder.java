package kandrm.JLatVis.export;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextMeasurer;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedString;
import math.geom2d.conic.Ellipse2D;
import math.geom2d.line.Line2D;
import math.geom2d.polygon.Polygon2D;



public class SvgBuilder extends VectorBuilder{

    public SvgBuilder(OutputStream stream, int width, int height){
        super(stream, width, height);
    }

    @Override
    public void close() throws IOException{
        doc.insert(0, "<?xml version=\"1.0\" standalone=\"no\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\"> <svg width=\""+width+"px\" height=\""+height+"px\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">");
        doc.append("</svg>");
        super.close();
    }

    @Override
    protected void doWrite(String text, int x, int y, Float w, Float h, Font font, Color color, Float opacity) throws VectorBuilderException{
        StringBuilder line = new StringBuilder();
        line.append("<text x=\"").append(x).append("\" y=\"").append(y).append("\" style=\"font-family:").append(font.getFamily())
                .append("; font-style:").append(font.isItalic() ? "italic" : "normal")
                .append("; font-weight:").append(font.isBold() ? "bold" : "normal")
                .append("; font-size:").append(font.getSize()).append("px")
                .append("; text-decoration:").append(font.getAttributes().get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON ? "underline" : "none");
        if(h != null && h > 0){
            line.append("; height: ").append(h);
        }
        if(color != null){
            line.append("; fill: ").append(color2Rgb(color));
        }
        line.append(";\"");
        if(opacity != null){
            line.append("fill-opacity=\"").append(opacity).append("\"");
        }
        line.append(">");
        if(w != null && w > 0){
            TextMeasurer measurer = new TextMeasurer(new AttributedString(text).getIterator(), new FontRenderContext(null, false, false));
            int actualPosition = 0;
            int yOffset = font.getSize() + 4;
            for(int i=1; actualPosition < text.length() && i*yOffset < h; ++i){
                int breakingIndex = measurer.getLineBreakIndex(actualPosition, w);
                int newLineIndex = text.indexOf("\n", actualPosition);
                boolean newLine = false;
                if(newLineIndex > 0 && newLineIndex < breakingIndex){
                    breakingIndex = newLineIndex;
                    newLine = true;
                }
                line.append("<tspan x=\"").append(x).append("\" dy=\"").append(actualPosition==0 ? 0 : yOffset).append("\">").append(text.subSequence(actualPosition, breakingIndex)).append("</tspan>");
                actualPosition = breakingIndex;
                if(newLine){
                    ++ actualPosition;
                }
            }
        } else {
            line.append(text);
        }
        line.append("</text>");
        doc.append(line);
    }

    @Override
    protected void paint(Ellipse2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity){
        StringBuilder line = new StringBuilder();
        line.append("<ellipse cx=\"").append(shape.getCenter().x).append("\" cy=\"").append(shape.getCenter().y)
                .append("\" rx=\"").append(shape.getSemiMajorAxisLength()).append("\" ry=\"").append(shape.getSemiMinorAxisLength()).append("\" ");
        if(paintType == PaintType.Fill){
            line.append(" fill=\"").append(color2Rgb(color)).append("\"");
        } else {
            line.append(" fill=\"none\" ").append(createStrokeStyle(stroke, color));
        }
        if(opacity != null){
            line.append(paintType == PaintType.Fill ? "fill" : "stroke").append("-opacity=\"").append(opacity).append("\"");
        }
        if(shape.getAngle() > 0){
            line.append(" transform=\"rotate(").append(Math.toDegrees(shape.getAngle())).append(" ").append(shape.getCenter().x).append(" ").append(shape.getCenter().y).append(")\"");
        }
        line.append(" />");
        doc.append(line);
    }
    @Override
    protected void paint(Line2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity){
        StringBuilder line = new StringBuilder();
        line.append("<line x1=\"").append(shape.p1.x).append("\" y1=\"").append(shape.p1.y)
                .append("\" x2=\"").append(shape.p2.x).append("\" y2=\"").append(shape.p2.y).append("\" ")
                .append(createStrokeStyle(stroke, color));
        if(opacity != null){
            line.append(paintType == PaintType.Fill ? "fill" : "stroke").append("-opacity=\"").append(opacity).append("\"");
        }
        line.append(" />");
        doc.append(line);
    }
    @Override
    protected void paint(Polygon2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity){
        StringBuilder line = new StringBuilder();
        line.append("<polygon points=\"");
        for(Point2D p : shape.getVertices()){
            line.append(p.getX()).append(",").append(p.getY()).append(" ");
        }
        line.append("\"");
        if(paintType == PaintType.Fill){
            line.append(" fill=\"").append(color2Rgb(color)).append("\"");
        } else {
            line.append(" fill=\"none\" ").append(createStrokeStyle(stroke, color));
        }
        if(opacity != null){
            line.append(paintType == PaintType.Fill ? "fill" : "stroke").append("-opacity=\"").append(opacity).append("\"");
        }
        line.append(" />");
        doc.append(line);
    }

    protected StringBuilder createStrokeStyle(BasicStroke stroke, Color color){
        StringBuilder line = new StringBuilder();
        
        line.append("style=\"stroke-width: ").append(stroke.getLineWidth()).append("; stroke: ").append(color2Rgb(color));
        float[] dashArray = stroke.getDashArray();
        if(dashArray != null){
            line.append("; stroke-dasharray: ");
            for (int i = 0; i < dashArray.length; i++) {
                line.append(dashArray[i]);
                if(i != dashArray.length - 1){
                    line.append(", ");
                }
            }
        }
        line.append("; stroke-linecap: ");
        switch(stroke.getEndCap()){
            case BasicStroke.CAP_BUTT:
                line.append("butt");
                break;
            case BasicStroke.CAP_ROUND:
                line.append("round");
                break;
            case BasicStroke.CAP_SQUARE:
                line.append("square");
                break;
        }
        line.append("; stroke-linejoin: ");
        switch(stroke.getLineJoin()){
            case BasicStroke.JOIN_MITER:
                line.append("miter");
                break;
            case BasicStroke.JOIN_ROUND:
                line.append("round");
                break;
            case BasicStroke.JOIN_BEVEL:
                line.append("bevel");
                break;
        }
        line.append("; stroke-miterlimit: ").append(stroke.getMiterLimit()).append(";\"");

        return line;
    }

    protected String color2Rgb(Color color){
        return "rgb("+color.getRed()+", "+color.getGreen()+", "+color.getBlue()+")";
    }
}
