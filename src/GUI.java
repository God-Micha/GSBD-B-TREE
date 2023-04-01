import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Galli Gregory, Mopolo Moke Gabriel
 */
public class GUI extends JFrame implements ActionListener {
    TestKey comparKey = new TestKey();
    BTreePlus<Key> bKey;

    Integer index = 1;
    private JButton buttonClean, buttonRemove, buttonLoad, buttonSave, buttonAddMany, buttonAddItem, buttonRefresh, buttonLoadTxt;
    private JTextField txtNbreItem, txtNbreSpecificItem, txtU, txtFile, removeSpecific;
    private final JTree tree = new JTree();

    public GUI() {
        super();
        build();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonLoad || e.getSource() == buttonClean || e.getSource() == buttonSave || e.getSource() == buttonRefresh) {
            if (e.getSource() == buttonLoad) {
                BDeserializer<Key> load = new BDeserializer<Key>();
                bKey = load.getArbre(txtFile.getText());
                if (bKey == null)
                    System.out.println("Echec du chargement.");

            } else if (e.getSource() == buttonClean) {
                if (Integer.parseInt(txtU.getText()) < 2)
                    System.out.println("Impossible de creer un arbre dont le nombre de cles est inferieur a 2.");
                else
                    bKey = new BTreePlus<Key>(Integer.parseInt(txtU.getText()), comparKey);
            } else if (e.getSource() == buttonSave) {
                BSerializer<Key> save = new BSerializer<Key>(bKey, txtFile.getText());
            }else if (e.getSource() == buttonRefresh) {
                tree.updateUI();
            }else if (e.getSource() == buttonLoadTxt){
                // POUR CHAQUES LIGNES
                //        try(BufferedReader br = new BufferedReader(new FileReader(txtFile.getText())))
                //        {
                //            String line;
                //            while ((line = br.readLine()) != null) {
                //              Key valeur = new Key((int)line,i);
                //              bKey.addvaleur(valeur);
                //            }
                //        }
                //        catch (IOException e) {
                //            System.out.println("An error occurred.");
                //            e.printStackTrace();
                //        }
            }
        } else {
            if (bKey == null)
                bKey = new BTreePlus<Key>(Integer.parseInt(txtU.getText()), comparKey);

            if (e.getSource() == buttonAddMany) {
                for (int i = 0; i < Integer.parseInt(txtNbreItem.getText()); i++) {
                    Key valeur = new Key((int) (Math.random() * 10 * Integer.parseInt(txtNbreItem.getText())), index);
                    boolean done = bKey.addValeur(valeur);

                    /**
                     On pourrait forcer l'ajout mais on risque alors de tomber dans une boucle infinie sans "regle" faisant sens pour en sortir

                     while (!done)
                     {
                     valeur =(int) (Math.random() * 10 * Integer.parseInt(txtNbreItem.getText()));
                     done = bInt.addValeur(valeur);
                     }
                     */
                }

            } else if (e.getSource() == buttonAddItem) {
                /* On peut placer l'index car il n'a aucune importance pour la recherche, le equals utilisé dans contains de contient a été override */
                Key valeur = new Key(Integer.parseInt(txtNbreSpecificItem.getText()),index);
                if (!bKey.addValeur(valeur))
                    System.out.println("Tentative d'ajout d'une valeur existante : " + txtNbreSpecificItem.getText());
                txtNbreSpecificItem.setText(
                        String.valueOf(
                                Integer.parseInt(txtNbreSpecificItem.getText()) + 2
                        )
                );

            } else if (e.getSource() == buttonRemove) {
                /* On peut placer l'index car il n'a aucune importance pour la recherche, le equals utilisé dans contains de contient a été override */
                Key valeur = new Key(Integer.parseInt(removeSpecific.getText()),index);
                bKey.removeValeur(valeur);
            }
        }

        tree.setModel(new DefaultTreeModel(bKey.bArbreToJTree()));
        for (int i = 0; i < tree.getRowCount(); i++)
            tree.expandRow(i);

        tree.updateUI();
    }

    private void build() {
        setTitle("Indexation - B Arbre");
        setSize(760, 760);
        setLocationRelativeTo(this);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(buildContentPane());
    }

    private JPanel buildContentPane() {
        GridBagLayout gLayGlob = new GridBagLayout();

        JPanel pane1 = new JPanel();
        pane1.setLayout(gLayGlob);

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 5, 2, 0);

        JLabel labelU = new JLabel("Nombre max de cles par noeud (2m): ");
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        pane1.add(labelU, c);

        txtU = new JTextField("4", 7);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 2;
        pane1.add(txtU, c);

        JLabel labelBetween = new JLabel("Nombre de clefs a ajouter:");
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        pane1.add(labelBetween, c);

        txtNbreItem = new JTextField("10000", 7);
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        pane1.add(txtNbreItem, c);


        buttonAddMany = new JButton("Ajouter n elements aleatoires a l'arbre");
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonAddMany, c);

        JLabel labelSpecific = new JLabel("Ajouter une valeur specifique:");
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(labelSpecific, c);

        txtNbreSpecificItem = new JTextField("50", 7);
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(txtNbreSpecificItem, c);

        buttonAddItem = new JButton("Ajouter l'element");
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonAddItem, c);

        JLabel labelRemoveSpecific = new JLabel("Retirer une valeur specifique:");
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(labelRemoveSpecific, c);

        removeSpecific = new JTextField("54", 7);
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(removeSpecific, c);

        buttonRemove = new JButton("Supprimer l'element n de l'arbre");
        c.gridx = 2;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonRemove, c);

        JLabel labelFilename = new JLabel("Nom de fichier : ");
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(labelFilename, c);

        txtFile = new JTextField("arbre.abr", 7);
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(txtFile, c);

        buttonSave = new JButton("Sauver l'arbre");
        c.gridx = 2;
        c.gridy = 5;
        c.weightx = 0.5;
        c.gridwidth = 1;
        pane1.add(buttonSave, c);

        buttonLoad = new JButton("Charger l'arbre");
        c.gridx = 3;
        c.gridy = 5;
        c.weightx = 0.5;
        c.gridwidth = 1;
        pane1.add(buttonLoad, c);

        buttonClean = new JButton("Reset");
        c.gridx = 2;
        c.gridy = 7;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonClean, c);

        buttonLoadTxt = new JButton("Charger le fichier Txt");
        c.gridx= 2;
        c.gridy = 6;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonLoadTxt, c);

        buttonRefresh = new JButton("Refresh");
        c.gridx = 2;
        c.gridy = 8;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonRefresh, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 400;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.gridwidth = 4;   //2 columns wide
        c.gridx = 0;
        c.gridy = 9;

        JScrollPane scrollPane = new JScrollPane(tree);
        pane1.add(scrollPane, c);

        tree.setModel(new DefaultTreeModel(null));
        tree.updateUI();

        txtNbreItem.addActionListener(this);
        buttonAddItem.addActionListener(this);
        buttonAddMany.addActionListener(this);
        buttonLoad.addActionListener(this);
        buttonSave.addActionListener(this);
        buttonRemove.addActionListener(this);
        buttonClean.addActionListener(this);
        buttonRefresh.addActionListener(this);
        buttonLoadTxt.addActionListener(this);

        return pane1;
    }
}

