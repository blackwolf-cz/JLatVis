package kandrm.JLatVis.export;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextMeasurer;
import java.io.IOException;
import java.io.OutputStream;
import java.text.AttributedString;
import math.geom2d.Point2D;
import math.geom2d.conic.Ellipse2D;
import math.geom2d.line.Line2D;
import math.geom2d.polygon.Polygon2D;

/**
 *
 * @author Michal Kandr
 */
public class MetapostBuilder extends VectorBuilder {
    protected static final String PICT_VAR = "p";
    protected static final String UNIT = "u";

    public MetapostBuilder(OutputStream stream, int width, int height){
        super(stream, width, height);
        doc.append("picture "+PICT_VAR+";");
    }

    @Override
    public void close() throws IOException{
        doc.insert(0, "verbatimtex \n %&latex \n \\documentclass{minimal} \n \\usepackage{color} \n \\begin{document} \n etex \n prologues:=3; \n "+UNIT+":=1pt; \n labeloffset:=0"+UNIT+"; \n beginfig(1); \n ");
        doc.append(" \n endfig; \n end;");
        super.close();
    }

    @Override
    protected void paint(Ellipse2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity) {
        float[] dashArray = null;
        if(paintType == PaintType.Draw){
            dashArray = stroke.getDashArray();
            if(dashArray != null){
                setDashing(dashArray);
            }
            setLineCap(stroke);
            setPen(stroke);
        }
        if(paintType == PaintType.Draw){
            doc.append("draw");
        } else {
            doc.append("fill");
        }
        doc.append(" fullcircle xscaled ").append(shape.getSemiMajorAxisLength() * 2).append(UNIT + " yscaled ").append(shape.getSemiMinorAxisLength() * 2)
                .append(UNIT + " rotated ").append(shape.getAngle()).append(" shifted ").append(point2Coords(shape.getCenter()))
                .append(" withcolor ").append(color2Rgb(color));
        if(paintType == PaintType.Draw && dashArray != null){
            doc.append("dashed "+PICT_VAR);
        }
        doc.append(";\n");
    }

    @Override
    protected void paint(Line2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity) {
        float[] dashArray = stroke.getDashArray();
        if(dashArray != null){
            setDashing(dashArray);
        }
        setLineCap(stroke);
        setPen(stroke);
        doc.append("draw ").append(point2Coords(shape.p1)).append("--").append(point2Coords(shape.p2)).append(" withcolor ").append(color2Rgb(color));
        if(dashArray != null){
            doc.append("dashed "+PICT_VAR);
        }
        doc.append(";\n");
    }

    @Override
    protected void paint(Polygon2D shape, PaintType paintType, BasicStroke stroke, Color color, Float opacity) {
        float[] dashArray = null;
        if(paintType == PaintType.Draw){
            dashArray = stroke.getDashArray();
            if(dashArray != null){
                setDashing(dashArray);
            }
            setLineCap(stroke);
            setPen(stroke);
        }
        if(paintType == PaintType.Draw){
            doc.append("draw");
        } else {
            doc.append("fill");
        }
        doc.append(" ");
        for(Point2D p : shape.getVertices()){
            doc.append(point2Coords(p)).append("--");
        }
        doc.append("cycle withcolor ").append(color2Rgb(color));
        if(paintType == PaintType.Draw && dashArray != null){
            doc.append("dashed "+PICT_VAR);
        }
        doc.append(";\n");
    }

    @Override
    protected void doWrite(String text, int x, int y, Float w, Float h, Font font, Color color, Float opacity) throws VectorBuilderException {
        doc.append("verbatimtex \\fontsize{").append(font.getSize()).append("}{").append((int) Math.ceil(font.getSize()*1.2)).append("}\\selectfont etex\n");
        if(w != null && w > 0){
            TextMeasurer measurer = new TextMeasurer(new AttributedString(text).getIterator(), new FontRenderContext(null, false, false));
            int actualPosition = 0;
            int yOffset = y;
            int lineHeight = font.getSize() + 2;
            while(actualPosition < text.length() && yOffset - y + lineHeight < h){
                int breakingIndex = measurer.getLineBreakIndex(actualPosition, w);
                int newLineIndex = text.indexOf("\n", actualPosition);
                boolean newLine = false;
                if(newLineIndex > 0 && newLineIndex < breakingIndex){
                    breakingIndex = newLineIndex;
                    newLine = true;
                }
                doWriteTextPart(text.subSequence(actualPosition, breakingIndex).toString(), x, yOffset, font, color, opacity);
                actualPosition = breakingIndex;
                if(newLine){
                    ++ actualPosition;
                }
                yOffset += lineHeight;
            }
        } else {
            doWriteTextPart(text, x, y, font, color, opacity);
        }
    }

    protected void doWriteTextPart(String text, int x, int y, Font font, Color color, Float opacity){
        doc.append("label.urt(btex \\textcolor[rgb]{").append(color2Rgb(color, false)).append("}{");

        if(font.isBold()){
            doc.append("\\textbf{");
        }
        if(font.isItalic()){
            doc.append("\\textit{");
        }
        if(font.getAttributes().get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON){
            doc.append("\\underline{");
        }

        doc.append(text);

        if(font.isBold()){
            doc.append("}");
        }
        if(font.isItalic()){
            doc.append("}");
        }
        if(font.getAttributes().get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON){
            doc.append("}");
        }

        doc.append("} etex, ").append(point2Coords(new Point2D(x, y))).append(");");
    }

    protected void setPen(BasicStroke stroke){
        doc.append("pickup pencircle scaled ").append(stroke.getLineWidth()).append(UNIT+";");
    }

    protected void setLineCap(BasicStroke stroke){
        doc.append("linecap:=");
        switch(stroke.getEndCap()){
            case BasicStroke.CAP_BUTT:
                doc.append("butt");
                break;
            case BasicStroke.CAP_ROUND:
                doc.append("rounded");
                break;
            case BasicStroke.CAP_SQUARE:
                doc.append("squared");
                break;
        }
        doc.append(";");
    }

    protected void setDashing(float[] dashArray){
        doc.append(PICT_VAR+" := dashpattern(");
        for (int i = 0; i < dashArray.length; i++) {
            doc.append(i % 2 == 0 ? "on" : "off").append(" ").append(dashArray[i]).append(UNIT+" ");
        }
        doc.append(");");
    }

    protected String color2Rgb(Color color, boolean brackets){
        return (brackets ? "(" : "")+(color.getRed()/255f)+", "+(color.getGreen()/255f)+", "+(color.getBlue()/255f)+(brackets ? ")" : "");
    }
    protected String color2Rgb(Color color){
        return color2Rgb(color, true);
    }

    protected String point2Coords(Point2D point){
        return  "("+point.x+UNIT+","+(height - point.y)+UNIT+")";
    }
}
