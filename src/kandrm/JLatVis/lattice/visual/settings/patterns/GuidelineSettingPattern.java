package kandrm.JLatVis.lattice.visual.settings.patterns;

import java.awt.Color;
import kandrm.JLatVis.lattice.visual.DashingPattern;
import kandrm.JLatVis.lattice.visual.settings.GuidelineVisualSettings;

/**
 *
 * @author Michal Kandr
 */
public class GuidelineSettingPattern extends LineSettingPattern {
    private Boolean showGrid;
    private Boolean showEdge;
    private Boolean showParallel;
    private Integer spacingHoriz;
    private Integer spacingVert;
    private Integer angle;
    private Integer stickingDist;
    private Boolean grid;
    private Boolean onlyIntersec;

    public GuidelineSettingPattern(){
        this(null, null, null, null, null, null, null, null, null, null, null, null);
    }
    public GuidelineSettingPattern(
            Color color,
            Integer width,
            DashingPattern dashing,
            
            Boolean showGrid,
            Boolean showEdge,
            Boolean showParallel,

            Integer spacingHoriz,
            Integer spacingVert,
            Integer angle,
            Integer stickingDist,
            Boolean grid,
            Boolean onlyIntersec){
        super(color, width, dashing);
        this.showGrid = showGrid;
        this.showEdge = showEdge;
        this.showParallel = showParallel;
        this.spacingHoriz = spacingHoriz;
        this.spacingVert = spacingVert;
        this.angle = angle;
        this.stickingDist = stickingDist;
        this.grid = grid;
        this.onlyIntersec = onlyIntersec;
    }
    public GuidelineSettingPattern(GuidelineVisualSettings settings){
        this(
            settings.getColor(),
            (int) settings.getStroke().getLineWidth(),
            new DashingPattern(settings.getStroke().getDashArray()),
            settings.isShowGrid(),
            settings.isShowEdge(),
            settings.isShowParallel(),
            settings.getSpacingHoriz(),
            settings.getSpacingVert(),
            settings.getAngle(),
            settings.getStickingDist(),
            settings.isGrid(),
            settings.isOnlyIntersec()
       );
    }
    
    public boolean match(GuidelineVisualSettings settings){
        return super.match(settings)
            && (showGrid == null || showGrid.equals(settings.isShowGrid()))
            && (showEdge == null || showEdge.equals(settings.isShowEdge()))
            && (showParallel == null || showParallel.equals(settings.isShowParallel()))
            && (spacingHoriz == null || spacingHoriz.equals(settings.getSpacingHoriz()))
            && (spacingVert == null || spacingVert.equals(settings.getSpacingVert()))
            && (angle == null || angle.equals(settings.getAngle()))
            && (stickingDist == null || stickingDist.equals(settings.getStickingDist()))
            && (grid == null || grid.equals(settings.isGrid()))
            && (onlyIntersec == null || onlyIntersec.equals(settings.isOnlyIntersec()));
    }

    public void updateByPattern(GuidelineVisualSettings settings){
        super.updateByPattern(settings);
        if(showGrid != null){
            settings.setShowGrid(showGrid);
        }
        if(showEdge != null){
            settings.setShowEdge(showEdge);
        }
        if(showParallel != null){
            settings.setShowParallel(showParallel);
        }
        if(spacingHoriz != null){
            settings.setSpacingHoriz(spacingHoriz);
        }
        if(spacingVert != null){
            settings.setSpacingVert(spacingVert);
        }
        if(angle != null){
            settings.setAngle(angle);
        }
        if(stickingDist != null){
            settings.setStickingDist(stickingDist);
        }
        if(grid != null){
            settings.setGrid(grid);
        }
        if(onlyIntersec != null){
            settings.setOnlyIntersec(onlyIntersec);
        }
    }

    public void intersect(GuidelineVisualSettings settings){
        super.intersect(settings);
        
        if(showGrid == null || ! showGrid.equals(settings.isShowGrid())){
            showGrid = null;
        }
        if(showEdge == null || ! showEdge.equals(settings.isShowEdge())){
            showEdge = null;
        }
        if(showParallel == null || ! showParallel.equals(settings.isShowParallel())){
            showParallel = null;
        }
        if(spacingHoriz == null || ! spacingHoriz.equals(settings.getSpacingHoriz())){
            spacingHoriz = null;
        }
        if(spacingVert == null || ! spacingVert.equals(settings.getSpacingVert())){
            spacingVert = null;
        }
        if(angle == null || ! angle.equals(settings.getAngle())){
            angle = null;
        }
        if(stickingDist == null || ! stickingDist.equals(settings.getStickingDist())){
            stickingDist = null;
        }
        if(grid == null || ! grid.equals(settings.isGrid())){
            grid = null;
        }
        if(onlyIntersec == null || ! onlyIntersec.equals(settings.isOnlyIntersec())){
            onlyIntersec = null;
        }
    }

    public Integer getAngle() {
        return angle;
    }
    public void setAngle(Integer angle) {
        this.angle = angle;
    }

    public Boolean getGrid() {
        return grid;
    }
    public void setGrid(Boolean grid) {
        this.grid = grid;
    }

    public Boolean getShowGrid() {
        return showGrid;
    }
    public void setShowGrid(Boolean showGrid) {
        this.showGrid = showGrid;
    }

    public Boolean getShowEdge() {
        return showEdge;
    }
    public void setShowEdge(Boolean showEdge) {
        this.showEdge = showEdge;
    }

    public Boolean getShowParallel() {
        return showParallel;
    }
    public void setShowParallel(Boolean showParallel) {
        this.showParallel = showParallel;
    }

    public Integer getSpacingHoriz() {
        return spacingHoriz;
    }
    public void setSpacingHoriz(Integer spacingHoriz) {
        this.spacingHoriz = spacingHoriz;
    }

    public Integer getSpacingVert() {
        return spacingVert;
    }
    public void setSpacingVert(Integer spacingVert) {
        this.spacingVert = spacingVert;
    }

    public Integer getStickingDist() {
        return stickingDist;
    }
    public void setStickingDist(Integer stickingDist) {
        this.stickingDist = stickingDist;
    }

    public Boolean getOnlyIntersec() {
        return onlyIntersec;
    }
    public void setOnlyIntersec(Boolean onlyIntersec) {
        this.onlyIntersec = onlyIntersec;
    }
}
