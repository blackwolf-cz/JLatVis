package kandrm.JLatVis.lattice.visual;

import com.generationjava.io.xml.XmlWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kandrm.JLatVis.export.IXmlSerializable;
import kandrm.JLatVis.export.XmlInvalidException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Obalová třída pro vzor čáry (dashing pattern v Basic Stroke).
 *
 * @author Michal Kandr
 */
public class DashingPattern implements IXmlSerializable {
    /**
     * Dashing pattern v původní surové podobě.
     */
    private float[] dashing;

    public DashingPattern(){}
    
    /**
     * Nová instance obalující zadaný vzor.
     *
     * @param dashing
     */
    public DashingPattern(float[] dashing){
        if(dashing != null){
            this.dashing = new float[dashing.length];
            System.arraycopy(dashing, 0, this.dashing, 0, dashing.length);
        }
    }
    public DashingPattern(List<Float> dashing){
        if(dashing != null){
            this.dashing = new float[dashing.size()];
            for(int i=0; i<dashing.size(); ++i){
                this.dashing[i] = dashing.get(i);
            }
        }
    }

    @Override
    public boolean equals(Object dashing){
        if(dashing == this || this.dashing == dashing){
            return true;
        } else if(dashing instanceof float[]){
            float[] dash = (float[]) dashing;
            return Arrays.equals(this.dashing, dash);
        } else if(dashing instanceof DashingPattern){
            DashingPattern dash = (DashingPattern) dashing;
            return Arrays.equals(this.dashing, dash.dashing);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.dashing != null ? this.dashing.hashCode() : 0);
        return hash;
    }

    /**
     * @return vzor v surové podobě
     */
    public float[] getDashing(){
        return dashing;
    }

    /**
     * Vrátí vzor přepočítaný podle šířky linky - u tlustší linky musí být rozestupy
     * mezi úseky linky větší, jinak se přerušovaná čára slije do plné.
     *
     * @param lineWidth šířka linky
     * @return vzor přepočítaný podle šířky linky
     */
    public float[] getRecountedDashing(float lineWidth){
        if(dashing == null){
            return null;
        }
        
        float[] dash = new float[dashing.length];
        for (int i = 0; i < dashing.length; i++) {
            dash[i] = dashing[i] * lineWidth;
        }
        return dash;
    }

    /**
     * Zjištění, zda je pole s dashingem nulové (z pohledu třídy Stroke).
     * Dashing je nulový pokud není (hodnota null), nebo jsou všechny položky pole rovné nule.
     *
     * @return zda je pole s dashingem nulové
     */
    public boolean allZero(){
        if(dashing == null){
            return true;
        }
        boolean dashAllZero = true;
        for (int i = 0; i < dashing.length; i++) {
           if(dashing[i] != 0.0){
               dashAllZero = false;
               break;
           }
        }
        return dashAllZero;
    }

    /**
     * Převede vzor na "normalizovaný" tvar - vzor pro linku šířky 1px.
     *
     * @param lineWidth šířka linky, pro kterou je současný vzor určen
     *
     * @return vzor pro 1px linku
     */
    public DashingPattern createNormalizedDashing(float lineWidth){
        float[] newDashing = new float[dashing.length];
        for (int i = 0; i < dashing.length; i++) {
            newDashing[i] = dashing[i] / lineWidth;
        }
        return new DashingPattern(newDashing);
    }

    @Override
    public XmlWriter toXml(XmlWriter writer) throws IOException {
        writer.writeEntity("Dashing");
        if(dashing != null){
            for (int i = 0; i < dashing.length; i++) {
                writer.writeEntityWithText("Segment", dashing[i]);
            }
        }
        return writer.endEntity();
    }

    public static DashingPattern fromXml(Element element) throws XmlInvalidException {
        NodeList dashingNode = element.getElementsByTagName("Dashing");
        if(dashingNode.getLength() > 0){
            NodeList childs = dashingNode.item(0).getChildNodes();
            List<Float> pattern = new ArrayList<Float>();
            for(int i=0; i<childs.getLength(); ++i){
                org.w3c.dom.Node elNode = childs.item(i);
                if(elNode.getNodeType() != Element.ELEMENT_NODE){
                    continue;
                }
                if( ! elNode.getNodeName().equals("Segment")){
                    throw new XmlInvalidException();
                }
                pattern.add(Float.parseFloat(elNode.getTextContent()));
            }
            return new DashingPattern(pattern);
        } else {
            return new DashingPattern();
        }
    }
}