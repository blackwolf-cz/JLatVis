package kandrm.JLatVis.export;

import kandrm.JLatVis.lattice.visual.LatticeShape;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import kandrm.JLatVis.export.VectorBuilder.VectorBuilderException;


/**
 *
 * @author Michal Kandr
 */
public class ImageExport {
    private static final String PDF = "pdf";
    private static final String SVG = "svg";
    private static final String MP = "mp";

    private static void exportPdf(LatticeShape lattice, File file) throws IOException, DocumentException{
       // resize for better output quallity
       double oldZoom = lattice.getZoom().getZoom();
       lattice.getZoom().setZoom(3);
       
       Rectangle bounds = lattice.getRealBounds();
       
       Dimension dim = bounds.getSize();
       BufferedImage im = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
       Graphics2D graphics2D = im.createGraphics();
       graphics2D.translate(- bounds.x, - bounds.y);
       lattice.paint(graphics2D);

       // resize back
       lattice.getZoom().setZoom(oldZoom);

       bounds = lattice.getRealBounds();
       
       Document doc = new Document(new com.itextpdf.text.Rectangle(bounds.width, bounds.height), 0, 0, 0, 0);
       PdfWriter.getInstance(doc, new FileOutputStream(file.getCanonicalPath()));
       doc.open();

       Image image = Image.getInstance (im, null);

       Float percentSmaller = Math.max(doc.getPageSize().getWidth() / image.getPlainWidth(), doc.getPageSize().getHeight() / image.getPlainHeight());
       if(percentSmaller < 1){
           image.scalePercent(percentSmaller*100);
       }

       doc.setPageSize(new com.itextpdf.text.Rectangle(image.getPlainWidth(), image.getPlainHeight()));
       doc.add (image);
       doc.close ();
    }

    private static void exportSvg(LatticeShape lattice, File file) throws IOException, DocumentException, VectorBuilderException{
        Rectangle bounds = lattice.getRealBounds();
        SvgBuilder svg = new SvgBuilder(new FileOutputStream(file), bounds.width, bounds.height);
        lattice.paintVector(svg);
        svg.close();
    }

    private static void exportMp(LatticeShape lattice, File file) throws IOException, DocumentException, VectorBuilderException{
        Rectangle bounds = lattice.getRealBounds();
        MetapostBuilder mp = new MetapostBuilder(new FileOutputStream(file), bounds.width, bounds.height);
        lattice.paintVector(mp);
        mp.close();
    }

    private static void exportBitmap(LatticeShape lattice, String type, File file) throws IOException, DocumentException{
        Rectangle bounds = lattice.getRealBounds();
        BufferedImage im = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = im.createGraphics();
        graphics2D.translate(- bounds.x, - bounds.y);
        lattice.paint(graphics2D);
        ImageIO.write(im, type, file);
    }

    public static void export(LatticeShape lattice, String type, File file) throws IOException, DocumentException, VectorBuilderException{
        lattice.recountPreferredSize();
        if(type.equals(PDF)){
            exportPdf(lattice, file);
        } else if(type.equals(SVG)){
            exportSvg(lattice, file);
        } else if(type.equals(MP)){
            exportMp(lattice, file);
        } else {
            exportBitmap(lattice, type, file);
        }
    }
}
