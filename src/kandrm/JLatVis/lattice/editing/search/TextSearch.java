package kandrm.JLatVis.lattice.editing.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import kandrm.JLatVis.lattice.logical.Tag;
import kandrm.JLatVis.lattice.logical.Lattice;
import kandrm.JLatVis.lattice.logical.Node;
import kandrm.JLatVis.lattice.editing.selection.Selector;
import kandrm.JLatVis.lattice.logical.OrderPair;

/**
 * Textové vyhledávání. Prohledává textové atributy uzlů a popisků.
 * Tzn. názvy uzlů/popisků a texty popisků.
 * 
 * @author Michal Kandr
 */
public class TextSearch extends Search {
    public TextSearch(Lattice lattice, Selector selector, SearchResults results){
        super(lattice, selector, results);
    }

    /**
     * Vyhledá uzly (logickou reprezentaci), jejichž název odpovídá zadanému regulárnímu výrazu.
     *
     * @param namePattern vzor, jemuž má název uzlu odpovídat
     * 
     * @return nalezené uzly
     */
    private List<Node> findNodesByPatern(Pattern namePattern, Pattern commentPattern){
        List<Node> foundNodes = new ArrayList<Node>();

        for(Node node : lattice.getNodes().values()){
            if((namePattern == null || namePattern.matcher(node.getName()).find()) && (commentPattern == null || commentPattern.matcher(node.getComment()).find())){
                foundNodes.add(node);
            }
        }

        return foundNodes;
    }

    /**
     * Vyhledá popisky (logickou reprezentaci) jejichž názvy a text odpovídají zadaným regulárním výrazům.
     *
     * @param namePattern vzor pro název popisku (je-li null, nebude se název prohledávat)
     * @param textPattern vzor pro text popisku (je-li null, nebude se text prohledávat)
     *
     * @return nalezené popisky
     */
    private List<Tag> findTagsByPatern(Pattern namePattern, Pattern textPattern){
        List<Tag> foundTags = new ArrayList<Tag>();
        for(Node node : lattice.getNodes().values()){
            for(Tag tag : node.getTags()){
                if( (namePattern == null || namePattern.matcher(tag.getName()).find()) && (textPattern == null || textPattern.matcher(tag.getText()).find())){
                    foundTags.add(tag);
                }
            }
        }
        return foundTags;
    }

    private List<OrderPair> findEdgesByPatern(Pattern commentPattern){
        List<OrderPair> found = new ArrayList<OrderPair>();
        for(OrderPair pair : lattice.getOrderPairs()){
            if(commentPattern.matcher(pair.getComment()).find()){
                found.add(pair);
            }
        }
        return found;
    }
    
    private Pattern stringToPattern(String text, boolean isRexExp){
        if(text == null || text.equals("")){
            return null;
        } else {
            return Pattern.compile( isRexExp ? text : Pattern.quote(text) );
        }
    }


    /**
     * Vyhledá uzly svazu na základě zadaných vzorků textu. Prohledává všechny texty
     * související s uzlem - jeho název, název jeho popisků a text všech jeho popisků.
     *
     * @param name vzorek pro jméno uzlu
     * @param isNameRegExp zda je <b>name</b> regularni vyraz
     * @param tagName vzorek pro jméno popisků
     * @param isTagNameRegExp zda je <b>tagName</b> regularni vyraz
     * @param tagText vzorek pro text popisků
     * @param isTagTextRegExp zda je <b>tagText</b> regularni vyraz
     */
    public void findNodes(String name, boolean isNameRegExp,
            String comment, boolean isCommentRegExp,
            String tagName, boolean isTagNameRegExp,
            String tagText, boolean isTagTextRegExp){

        List<Node> found = new ArrayList<Node>();
        
        if((name != null && ! name.equals("")) || (comment != null && ! comment.equals(""))){
            found.addAll( findNodesByPatern(stringToPattern(name, isNameRegExp), stringToPattern(comment, isCommentRegExp)) );
        }

        if((tagName != null && ! tagName.equals("")) || (tagText != null && ! tagText.equals(""))){
            List<Tag> foundTags = findTagsByPatern(
                stringToPattern(tagName, isTagNameRegExp),
                stringToPattern(tagText, isTagTextRegExp)
            );
            for(Tag tag : foundTags){
                if( ! found.contains(tag.getNode())){
                    found.add(tag.getNode());
                }
            }
        }

        results.saveFoundNodes(found);
    }


    /**
     * Vyhledá popisky uzlů na základě zadaných vzorků textu. Prohledává se
     * název popisku a text popisku.
     *
     * @param name vzorek pro jméno popisku
     * @param isNameRegExp zda je <b>isNameRegExp</b> regularni vyraz
     * @param text vzorek pro text popisku
     * @param isTextRegExp zda je <b>isTextRegExp</b> regularni vyraz
     */
    public void findTags(String name, boolean isNameRegExp, String text, boolean isTextRegExp){
        List<Tag> found = new ArrayList<Tag>();

        if((name != null && ! name.equals("")) || (text != null && ! text.equals(""))){
            found = findTagsByPatern(
                stringToPattern(name, isNameRegExp),
                stringToPattern(text, isTextRegExp)
            );
        }

        results.saveFoundTags(found);
    }
    
    public void findEdges(String comment, boolean isCommentRegExp){
        List<OrderPair> found = new ArrayList<OrderPair>();
        
        if(comment != null && ! comment.equals("")){
            found = findEdgesByPatern(stringToPattern(comment, isCommentRegExp));
        }

        results.saveFoundEdges(found);
    }
}
