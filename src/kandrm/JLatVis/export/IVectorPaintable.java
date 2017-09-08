package kandrm.JLatVis.export;

import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;

/**
 * @author Michal Kandr
 */
public interface IVectorPaintable {
    public void paintVector(VectorBuilder vb) throws VectorBuilderException;
}
