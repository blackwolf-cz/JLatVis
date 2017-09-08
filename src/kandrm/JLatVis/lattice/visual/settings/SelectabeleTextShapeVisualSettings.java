package kandrm.JLatVis.lattice.visual.settings;

import com.generationjava.io.xml.XmlWriter;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import org.w3c.dom.Element;

/**
 *
 * @author Michal Kandr
 */
public class SelectabeleTextShapeVisualSettings extends TextShapeVisualSettings implements IXmlSerializable {
    private SelectedHighlightVisualSettings selectedHighlight;
    private FoundHighlightVisualSettings foundHighlight;
    private HighlightedHighlightVisualSettings highlightedHighlight;
    
    private HighlightedUpperHighlightVisualSettings upperHighlight;
    private HighlightedLowerHighlightVisualSettings lowerHighlight;

    public SelectabeleTextShapeVisualSettings(Dimension dimensions){
        super(dimensions);
        initHighlights();
    }

    public SelectabeleTextShapeVisualSettings(Dimension dimensions, Color backgroundColor){
        super(dimensions, backgroundColor);
        initHighlights();
    }

    public SelectabeleTextShapeVisualSettings(
            Dimension dimensions, int angle,
            Color backgroundColor,
            Color borderColor, BasicStroke borderStroke,
            Color textColor, Font textFont, TextVisualSettings.TextAlignment textAlignment){
        super(dimensions, angle, backgroundColor, borderColor, borderStroke, textColor, textFont, textAlignment);
        initHighlights();
    }

    public SelectabeleTextShapeVisualSettings(SelectabeleTextShapeVisualSettings s){
        super(s);
        initHighlights();
    }
    public SelectabeleTextShapeVisualSettings(TextShapeVisualSettings s){
        super(s);
        initHighlights();
    }
    
    private void initHighlights(){
        selectedHighlight = new SelectedHighlightVisualSettings();
        foundHighlight = new FoundHighlightVisualSettings();
        highlightedHighlight = new HighlightedHighlightVisualSettings();
        
        upperHighlight = new HighlightedUpperHighlightVisualSettings();
        lowerHighlight = new HighlightedLowerHighlightVisualSettings();
    }

    @Override
    public boolean equals(Object o){
        if(this == o){
            return true;
        } else if(o == null || o.getClass() != this.getClass()){
            return false;
        }

        SelectabeleTextShapeVisualSettings st = (SelectabeleTextShapeVisualSettings)o;
        return super.equals(o)
            && selectedHighlight.equals(st.selectedHighlight)
            && foundHighlight.equals(st.foundHighlight)
            && highlightedHighlight.equals(st.highlightedHighlight)
            && upperHighlight.equals(st.upperHighlight)
            && lowerHighlight.equals(st.lowerHighlight);
    }
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + super.hashCode();
        hash = 31 * hash + this.selectedHighlight.hashCode();
        hash = 31 * hash + this.foundHighlight.hashCode();
        hash = 31 * hash + this.highlightedHighlight.hashCode();
        hash = 31 * hash + this.upperHighlight.hashCode();
        hash = 31 * hash + this.lowerHighlight.hashCode();
        return hash;
    }


    public Color getSelectedHighlightColor() {
        return selectedHighlight.getColor();
    }
    public void setSelectedHighlightColor(Color color) {
        this.selectedHighlight.setColor(color);
    }

    public BasicStroke getSelectedHighlightStroke() {
        return selectedHighlight.getStroke();
    }
    public void setSelectedHighlightStroke(BasicStroke stroke) {
        this.selectedHighlight.setStroke(stroke);
    }

    public int getHighlightDistance(){
        return selectedHighlight.getDistance();
    }
    public void setHighlightDistance(int highlightDistance){
        this.selectedHighlight.setDistance(highlightDistance);
    }


    public Color getFoundHighlightColor() {
        return foundHighlight.getColor();
    }
    public void setFoundHighlightColor(Color color) {
        this.foundHighlight.setColor(color);
    }

    public BasicStroke getFoundHighlightStroke() {
        return foundHighlight.getStroke();
    }
    public void setFoundHighlightStroke(BasicStroke stroke) {
        this.foundHighlight.setStroke(stroke);
    }

    public int getFoundHighlightDistance(){
        return this.foundHighlight.getDistance();
    }
    public void setFoundHighlightDistance(int foundHighlightDistance){
        this.foundHighlight.setDistance(foundHighlightDistance);
    }
    


    public Color getHighlightedHighlightColor() {
        return highlightedHighlight.getColor();
    }
    public void setHighlightedHighlightColor(Color color) {
        this.highlightedHighlight.setColor(color);
    }

    public BasicStroke getHighlightedHighlightStroke() {
        return highlightedHighlight.getStroke();
    }
    public void setHighlightedHighlightStroke(BasicStroke stroke) {
        this.highlightedHighlight.setStroke(stroke);
    }

    public int getHighlightedHighlightDistance(){
        return this.highlightedHighlight.getDistance();
    }
    public void setHighlightedHighlightDistance(int foundHighlightDistance){
        this.highlightedHighlight.setDistance(foundHighlightDistance);
    }
    


    public Color getUpperHighlightColor() {
        return upperHighlight.getColor();
    }
    public void setUpperHighlightColor(Color color) {
        this.upperHighlight.setColor(color);
    }

    public BasicStroke getUpperHighlightStroke() {
        return upperHighlight.getStroke();
    }
    public void setUpperHighlightStroke(BasicStroke stroke) {
        this.upperHighlight.setStroke(stroke);
    }

    public int getUpperHighlightDistance(){
        return this.upperHighlight.getDistance();
    }
    public void setUpperHighlightDistance(int foundHighlightDistance){
        this.upperHighlight.setDistance(foundHighlightDistance);
    }
    
    
    
    public Color getLowerHighlightColor() {
        return lowerHighlight.getColor();
    }
    public void setLowerHighlightColor(Color color) {
        this.lowerHighlight.setColor(color);
    }

    public BasicStroke getLowerHighlightStroke() {
        return lowerHighlight.getStroke();
    }
    public void setLowerHighlightStroke(BasicStroke stroke) {
        this.lowerHighlight.setStroke(stroke);
    }

    public int getLowerHighlightDistance(){
        return this.lowerHighlight.getDistance();
    }
    public void setLowerHighlightDistance(int foundHighlightDistance){
        this.lowerHighlight.setDistance(foundHighlightDistance);
    }    
    



    public int getMaxHighlightDistance(){
        return (int) Math.max(Math.max(getHighlightDistance(), getFoundHighlightDistance()), getHighlightedHighlightDistance());
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        return super.toXml(writer);
    }

    public static SelectabeleTextShapeVisualSettings fromXml(Element element) throws XmlInvalidException {
        return new SelectabeleTextShapeVisualSettings( TextShapeVisualSettings.fromXml(element) );
    }
}