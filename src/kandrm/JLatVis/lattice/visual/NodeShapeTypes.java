package kandrm.JLatVis.lattice.visual;

import java.awt.Dialog;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import kandrm.JLatVis.lattice.visual.settings.NodeVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.ShapeRegularPolygonVisualSettings;
import kandrm.JLatVis.lattice.visual.settings.ShapeSpecialVisualSettings;
import kandrm.JLatVis.guiConnect.settings.visual.NodeModel;
import kandrm.JLatVis.guiConnect.settings.visual.NodeRegularPolygonModel;
import kandrm.JLatVis.gui.settings.visual.NodeRegularPolygonSettings;
import kandrm.JLatVis.gui.settings.visual.NodeSpecSettings;
import kandrm.JLatVis.guiConnect.settings.visual.NodeSpecialModel;
import kandrm.JLatVis.lattice.visual.settings.patterns.ShapeRegularPolygonSettingPattern;
import kandrm.JLatVis.lattice.visual.settings.patterns.ShapeSpecialSettingPattern;
import math.geom2d.Point2D;

/**
 *
 * @author Michal Kandr
 */
public class NodeShapeTypes {
    private static NodeShapeTypes instance = null;

    public enum Type {
        ELLIPSE,
        REGULAR_POLYGON
    }

    private Type[] types = {
        Type.ELLIPSE,
        Type.REGULAR_POLYGON
    };

    private String[] names = {
        "Ellipse",
        "Regular polygon"
    };

    private Class[] visualClasses = {
        OvalNodeShape.class,
        RegularPolygonNodeShape.class
    };
    private Class[] visualSettingsClasses = {
        null,
        ShapeRegularPolygonVisualSettings.class
    };

    private Class[] settingPatternClasses = {
        null,
        ShapeRegularPolygonSettingPattern.class
    };


    private Class[] settingsGuiClasses = {
        null,
        NodeRegularPolygonSettings.class
    };
    private Class[] visualSettingsModels = {
        null,
        NodeRegularPolygonModel.class
    };


    private NodeShapeTypes(){};

    public static NodeShapeTypes getInstance(){
        if(instance == null){
            instance = new NodeShapeTypes();
        }
        return instance;
    }

    public String getTypeName(Type t){
        return names[Arrays.binarySearch(types,t)];
    }

    public String[] getNames(){
        return names;
    }

    public Type[] getTypes(){
        return types;
    }


    public INodeShape getVisualInstance(Type t, NodeVisualSettings visualSetings, Point2D center){
        Class c = visualClasses[Arrays.binarySearch(types,t)];
        Constructor constr;
        INodeShape ret = null;
        try {
            constr = c.getConstructor(new Class[]{NodeVisualSettings.class, Point2D.class});
            ret = (INodeShape) constr.newInstance(new Object[]{visualSetings, center});
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public Class getVisualSettingClass(Type t){
        return visualSettingsClasses[Arrays.binarySearch(types,t)];
    }

    public ShapeSpecialVisualSettings getVisualSettingInstance(Type t){
        Class c = getVisualSettingClass(t);
        if(c == null){
            return null;
        }
        ShapeSpecialVisualSettings ret = null;
        try {
            ret = (ShapeSpecialVisualSettings) c.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public ShapeSpecialSettingPattern getSettingPatternInstance(Type t){
        Class c = settingPatternClasses[Arrays.binarySearch(types,t)];
        if(c == null){
            return null;
        }
        ShapeSpecialSettingPattern ret = null;
        try {
            ret = (ShapeSpecialSettingPattern) c.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    public ShapeSpecialSettingPattern getSettingPatternInstance(Type t, ShapeSpecialVisualSettings setting){
        Class c = settingPatternClasses[Arrays.binarySearch(types,t)];
        if(c == null){
            return null;
        }
        Constructor constr;
        ShapeSpecialSettingPattern ret = null;
        try {
            constr = c.getConstructor(new Class[]{ShapeSpecialVisualSettings.class});
            ret = (ShapeSpecialSettingPattern) constr.newInstance(new Object[]{setting});
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public NodeSpecSettings getSettingGuiInstance(Type t, Dialog parent, NodeModel settingsModel, boolean isSearch){
        Class c = settingsGuiClasses[Arrays.binarySearch(types,t)];
        if(c == null){
            return null;
        }
        Constructor constr;
        NodeSpecSettings ret = null;
        try {
            constr = c.getConstructor(new Class[]{Dialog.class, NodeModel.class, boolean.class});
            ret = (NodeSpecSettings) constr.newInstance(new Object[]{parent, settingsModel, isSearch});
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public NodeSpecialModel getSettingModelInstance(Type t){
        Class c = visualSettingsModels[Arrays.binarySearch(types,t)];
        if(c == null){
            return null;
        }
        NodeSpecialModel ret = null;
        try {
            ret = (NodeSpecialModel) c.newInstance();
        } catch (InstantiationException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }
    public NodeSpecialModel getSettingModelInstance(Type t, List<NodeShape> nodes){
        Class c = visualSettingsModels[Arrays.binarySearch(types,t)];
        if(c == null){
            return null;
        }
        Constructor constr;
        NodeSpecialModel ret = null;
        try {
            constr = c.getConstructor(new Class[]{List.class});
            ret = (NodeSpecialModel) constr.newInstance(new Object[]{nodes});
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(NodeShapeTypes.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ret;
    }
}
