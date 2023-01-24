import com.sun.jdi.Type;

import javax.swing.tree.DefaultMutableTreeNode;


public class Key {
    public Type key;
    public int ligneFichier;

    Key(Type k, int ligne){
        key = k;
        ligneFichier = ligne;
    }
}
