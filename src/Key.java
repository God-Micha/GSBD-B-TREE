import com.sun.jdi.Type;

import javax.swing.tree.DefaultMutableTreeNode;


public class Key {
    public int key;
    public int ligneFichier;

    Key(int k, int ligne){
        key = k;
        ligneFichier = ligne;
    }


    /* Override de la fonction equals pour l'objet keys car nous voulons
       Prouver l'egalité que grâce a la clé et non grâce a l'index
    */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Key)){
            return false;
        }
        Key k = (Key)o;
        return (this.key == k.key);
    }
}
