package kandrm.JLatVis.lattice.visual.settings.patterns;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;
import kandrm.JLatVis.lattice.visual.DashingPattern;
import kandrm.JLatVis.lattice.visual.settings.TextShapeVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.TextVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class TextShapePattern {
    private ShapeSettingPattern shape;
    private LineSettingPattern border;
    private TextSettingPattern text;

    public TextShapePattern(
            //tvar
            Integer width,
            Integer height,
            Integer angle,
            Color backgroundColor,

            // okraje
            Color borderColor,
            Integer borderWidth,
            DashingPattern borderDashing,
            
            // text
            Color textColor,
            String fontName,
            Integer fontSize,
            Boolean fontBold,
            Boolean fontItalic,
            Boolean fontUnderline,
            TextVisualSettings.TextAlignment textAlignment){
        this.shape = new ShapeSettingPattern(backgroundColor, width, height, angle);
        this.border = new LineSettingPattern(borderColor, borderWidth, borderDashing);
        this.text = new TextSettingPattern(textColor, fontName, fontSize, fontBold, fontItalic, fontUnderline, textAlignment);
    }
    public TextShapePattern(TextShapeVisualSettings settings){
        this(
            settings.getDimensions() != null ? settings.getDimensions().width : null,
            settings.getDimensions() != null ? settings.getDimensions().height : null,
            settings.getAngle(),
            settings.getBackgroundColor(),
            settings.getBorderColor(),
            (int) settings.getBorderStroke().getLineWidth(),
            new DashingPattern(settings.getBorderStroke().getDashArray()),
            settings.getTextColor(),
            settings.getTextFont().getName(),
            settings.getTextFont().getSize(),
            settings.getTextFont().isBold(),
            settings.getTextFont().isItalic(),
            isFontUnderlined(settings.getTextFont()),
            settings.getTextAlignment()
        );
    }



    private static boolean isFontUnderlined(Font font){
        return font.getAttributes().get(TextAttribute.UNDERLINE) == TextAttribute.UNDERLINE_ON;
    }
    



    private boolean matchShapes(TextShapeVisualSettings settings){
        boolean sameBackgroundColor = shape.getColor() == null || shape.getColor().equals(settings.getBackgroundColor());
        boolean sameWidth = shape.getWidth() == null || shape.getWidth().equals(settings.getDimensions().width);
        boolean sameHeight = shape.getHeight() == null || shape.getHeight().equals(settings.getDimensions().height);
        boolean sameAngle = shape.getAngle() == null || shape.getAngle().equals(settings.getAngle());
        
        return sameBackgroundColor
            && sameWidth
            && sameHeight
            && sameAngle;
    }
    
    private boolean matchBorders(TextShapeVisualSettings settings){
        boolean sameBorderColor = border.getColor() == null || border.getColor().equals(settings.getBorderColor());
        boolean sameBorderWidth = border.getWidth() == null || border.getWidth().equals((int) settings.getBorderStroke().getLineWidth());
        boolean sameBorderDashing = border.getDashing() == null || border.getDashing().equals(new DashingPattern(settings.getBorderStroke().getDashArray()));
        
        return sameBorderColor
            && sameBorderWidth
            && sameBorderDashing;
    }
    
    private boolean matchText(TextShapeVisualSettings settings){
        boolean sameTextColor = text.getColor() == null || text.getColor().equals(settings.getTextColor());
        boolean sameTextFontName = text.getFontName() == null || text.getFontName().equals(settings.getTextFont().getName());
        boolean sameTextFontSize = text.getFontSize() == null || text.getFontSize().equals(settings.getTextFont().getSize());
        boolean sameTextFontBold = text.isFontBold() == null || text.isFontBold().equals(settings.getTextFont().isBold());
        boolean sameTextFontItalic = text.isFontItalic() == null || text.isFontItalic().equals(settings.getTextFont().isItalic());
        boolean sameTextFontUnderline = text.isFontUnderline() == null || text.isFontUnderline().equals(isFontUnderlined(settings.getTextFont()));
        boolean sameTextAlignment = text.getAlignment() == null || text.getAlignment().equals(settings.getTextAlignment());

        return sameTextColor
            && sameTextFontName
            && sameTextFontSize
            && sameTextFontBold
            && sameTextFontItalic
            && sameTextFontUnderline
            && sameTextAlignment;
    }

    public boolean match(TextShapeVisualSettings settings){
        return matchShapes(settings)
            && matchBorders(settings)
            && matchText(settings);
    }



    

    private void updateShapeByPattern(TextShapeVisualSettings settings){
        if(shape.getColor() != null){
            settings.setBackgroundColor(shape.getColor());
        }
        if(settings.getDimensions() == null){
            settings.setDimensions(new Dimension());
        }
        if (shape.getWidth() != null) {
            settings.setDimensions(new Dimension(shape.getWidth(), settings.getDimensions().height));
        }
        if (shape.getHeight() != null) {
            settings.setDimensions(new Dimension(settings.getDimensions().width, shape.getHeight()));
        }
        if (shape.getAngle() != null) {
            settings.setAngle(shape.getAngle());
        }
    }
    
    private void updateBordersByPattern(TextShapeVisualSettings settings){
        if(border.getColor() != null){
            settings.setBorderColor(border.getColor());
        }
        settings.setBorderStroke( border.createStrokeByPattern(settings.getBorderStroke()) );
    }

    private void updateTextByPattern(TextShapeVisualSettings settings){
        if(text.getColor() != null){
            settings.setTextColor(text.getColor());
        }

        Map fontAttributes = settings.getTextFont().getAttributes();
        if(text.getFontName() != null){
            fontAttributes.put(TextAttribute.FAMILY, text.getFontName());
        }
        if(text.getFontSize() != null){
            fontAttributes.put(TextAttribute.SIZE, text.getFontSize());
        }
        if(text.isFontBold() != null){
            fontAttributes.put(TextAttribute.WEIGHT, text.isFontBold() ? TextAttribute.WEIGHT_BOLD : TextAttribute.WEIGHT_REGULAR);
        }
        if(text.isFontItalic() != null){
            fontAttributes.put(TextAttribute.POSTURE, text.isFontItalic() ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        }
        if(text.isFontUnderline() != null){
            fontAttributes.put(TextAttribute.UNDERLINE, text.isFontUnderline() ? TextAttribute.UNDERLINE_ON : -1);
        }
        settings.setTextFont(settings.getTextFont().deriveFont(fontAttributes));


        if (text.getAlignment() != null) {
            settings.setTextAlignment(text.getAlignment());
        }

    }

    public void updateByPattern(TextShapeVisualSettings settings){
        updateShapeByPattern(settings);
        updateBordersByPattern(settings);
        updateTextByPattern(settings);
    }
    




    
    private void intersectShape(TextShapeVisualSettings settings){
        if(shape.getColor() == null || ! shape.getColor().equals(settings.getBackgroundColor())){
            shape.setColor(null);
        }
        if (shape.getWidth() == null || ! shape.getWidth().equals(settings.getDimensions().width)) {
            shape.setWidth(null);
        }
        if (shape.getHeight() == null || ! shape.getHeight().equals(settings.getDimensions().height)) {
            shape.setHeight(null);
        }
        if (shape.getAngle() == null || ! shape.getAngle().equals(settings.getAngle())) {
            shape.setAngle(null);
        }
    }
    
    private void intersectBorders(TextShapeVisualSettings settings){
        if(border.getColor() == null || ! border.getColor().equals(settings.getBorderColor())){
            border.setColor(null);
        }
        if (border.getWidth() == null || border.getWidth() != (int)settings.getBorderStroke().getLineWidth()) {
            border.setWidth(null);
        }
        if(border.getDashing() == null || ! border.getDashing().equals(new DashingPattern(settings.getBorderStroke().getDashArray()))){
            border.setDashing(null);
        }
    }

    private void intersectText(TextShapeVisualSettings settings){
        if(text.getColor() == null || ! text.getColor().equals(settings.getTextColor())){
            text.setColor(null);
        }
 
        if (text.getFontName() == null || ! text.getFontName().equals(settings.getTextFont().getName())) {
            text.setFontName(null);
        }
        if (text.getFontSize() == null || ! text.getFontSize().equals(settings.getTextFont().getSize())) {
            text.setFontSize(null);
        }
        if (text.isFontBold() == null || ! text.isFontBold().equals(settings.getTextFont().isBold())) {
            text.setFontBold(null);
        }
        if (text.isFontItalic() == null || ! text.isFontItalic().equals(settings.getTextFont().isItalic())) {
            text.setFontItalic(null);
        }
        if (text.isFontUnderline() == null || ! text.isFontUnderline().equals(isFontUnderlined(settings.getTextFont()))) {
            text.setFontUnderline(null);
        }
        
        if (text.getAlignment() == null || ! text.getAlignment().equals(settings.getTextAlignment())) {
            text.setAlignment(null);
        }

    }
    
    public void intersect(TextShapeVisualSettings settings){
        intersectShape(settings);
        intersectBorders(settings);
        intersectText(settings);
    }
    
 
    
    public Integer getWidth() {
        return shape.getWidth();
    }

    public Integer getHeight() {
        return shape.getHeight();
    }

    public Integer getAngle(){
        return shape.getAngle();
    }
   
    public Color getBackgroundColor() {
        return shape.getColor();
    }
    
    

    public Color getBorderColor() {
        return border.getColor();
    }

    public Integer getBorderWidth() {
        return border.getWidth();
    }

    public DashingPattern getBorderDashing() {
        return border.getDashing();
    }
    
    


    public Color getTextColor() {
        return text.getColor();
    }

    public String getFontName() {
        return text.getFontName();
    }

    public Integer getFontSize() {
        return text.getFontSize();
    }
    
    public Boolean isFontBold() {
        return text.isFontBold();
    }

    public Boolean isFontItalic() {
        return text.isFontItalic();
    }

    public Boolean isFontUnderline() {
        return text.isFontUnderline();
    }
}
