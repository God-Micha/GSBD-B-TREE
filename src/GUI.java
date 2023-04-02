import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

/**
 * @author Galli Gregory, Mopolo Moke Gabriel
 */
public class GUI extends JFrame implements ActionListener {
    TestKey comparKey = new TestKey();
    BTreePlus<Key> bKey;
    String saveFile = "arbre.abr";
    String txtFile = "FSGBD/Personnes.txt";
    Integer nligne = 1;

    private JButton buttonClean, buttonRemove, buttonLoad, buttonSave, buttonAddMany, buttonAddItem, buttonRefresh, buttonLoadTxt, buttonGenerateTxt, buttonChercherArbre, buttonChercherTexte;
    private JTextField txtNbreItem, txtNbreSpecificItem, txtU, removeSpecific, chercheSpecific;
    private final JTree tree = new JTree();

    public GUI() {
        super();
        build();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == buttonLoad || e.getSource() == buttonClean || e.getSource() == buttonSave || e.getSource() == buttonRefresh || e.getSource() == buttonGenerateTxt) {

            if (e.getSource() == buttonLoad) {
                System.out.println("Load");
                BDeserializer<Key> load = new BDeserializer<Key>();
                bKey = load.getArbre(saveFile);
                if (bKey == null) {
                    System.out.println("Echec du chargement.");
                }
            }

            else if (e.getSource() == buttonClean) {
                System.out.println("Clean all");
                if (Integer.parseInt(txtU.getText()) < 2)
                    System.out.println("Impossible de creer un arbre dont le nombre de cles est inferieur a 2.");
                else
                    bKey = new BTreePlus<Key>(Integer.parseInt(txtU.getText()), comparKey);
            }

            else if (e.getSource() == buttonSave) {
                System.out.println("Save");
                BSerializer<Key> save = new BSerializer<Key>(bKey, saveFile);
            }

            else if (e.getSource() == buttonRefresh) {
                System.out.println("Refresh");
                tree.updateUI();
            }

            else if (e.getSource() == buttonGenerateTxt) {
                System.out.println("Generation txt");
                try {
                    PrintWriter writer = null;
                    writer = new PrintWriter(txtFile);
                    for (int i = 0; i < Integer.parseInt(txtNbreItem.getText()); i++) {
                        writer.println((int) (Math.random() * 100000));
                    }
                    writer.close();
                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }

        else {
            if (bKey == null)
                bKey = new BTreePlus<Key>(Integer.parseInt(txtU.getText()), comparKey);

            if (e.getSource() == buttonAddMany) {
                for (int i = 0; i < Integer.parseInt(txtNbreItem.getText()); i++) {
                    Key valeur = new Key((int) (Math.random() * 10 * Integer.parseInt(txtNbreItem.getText())), nligne);
                    boolean done = bKey.addValeur(valeur);
                    nligne++;
                }
            }

            else if (e.getSource() == buttonAddItem) {
                /* On peut placer l'index car il n'a aucune importance pour la recherche, le equals utilisé dans contains de contient a été override */
                Key valeur = new Key(Integer.parseInt(txtNbreSpecificItem.getText()),nligne);
                if (!bKey.addValeur(valeur))
                    System.out.println("Tentative d'ajout d'une valeur existante : " + txtNbreSpecificItem.getText());
                txtNbreSpecificItem.setText(
                        String.valueOf(
                                Integer.parseInt(txtNbreSpecificItem.getText()) + 2
                        )
                );
                nligne++;

            }

            else if (e.getSource() == buttonRemove) {
                /* On peut placer l'index car il n'a aucune importance pour la recherche, le equals utilisé dans contains de contient a été override */
                Key valeur = new Key(Integer.parseInt(removeSpecific.getText()),nligne);
                bKey.removeValeur(valeur);
                nligne--;
            }

            else if (e.getSource() == buttonLoadTxt) {
                System.out.println("loading txt");
                try (BufferedReader br = new BufferedReader(new FileReader(txtFile))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                        Key valFich = new Key(Integer.parseInt(line), nligne);
                        if (!bKey.addValeur(valFich))
                            System.out.println("Tentative d'ajout d'une valeur existante : " + txtNbreSpecificItem.getText());
                        nligne++;
                    }
                } catch (IOException ex) {
                    System.out.println("An IO error occurred.");
                    ex.printStackTrace();
                }
            }
            else if (e.getSource() == buttonChercherArbre){
                System.out.println("Recherche de " + chercheSpecific.getText());
                Key cle = new Key(Integer.parseInt(chercheSpecific.getText()),0);
                Key trouve = bKey.rechercheIndexKey(cle);
                if(trouve == null){
                    System.out.println("pas trouvé");
                }
                else {
                    System.out.println("trouvé");
                    System.out.println("le client avec le numero " + trouve.key +  " se trouve en ligne " + trouve.ligneFichier);
                }
            }
            else if (e.getSource() == buttonChercherTexte){
                boolean found = false;
                System.out.println("Recherche de " + chercheSpecific.getText());
                try (BufferedReader br = new BufferedReader(new FileReader(txtFile))) {
                    String line;
                    int i= 0;
                    while ((line = br.readLine()) != null) {
                        if(line == chercheSpecific.getText()){
                            System.out.println("trouvé en ligne : " + i);
                            found = true;
                            return;
                        }
                        i++;
                    }
                    if (found == false){
                        System.out.println("pas trouvé");
                    }
                } catch (IOException ex) {
                    System.out.println("An IO error occurred.");
                    ex.printStackTrace();
                }
            }

        }
        if(bKey != null)
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

        JLabel labelBetween = new JLabel("Nombre de clefs a ajouter:");
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1;
        pane1.add(labelBetween, c);

        JLabel labelSpecific = new JLabel("Ajouter une valeur specifique:");
        c.gridx = 0;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(labelSpecific, c);

        JLabel labelRemoveSpecific = new JLabel("Retirer une valeur specifique:");
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(labelRemoveSpecific, c);

        JLabel labelFilename = new JLabel("Nom de fichier : ");
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(labelFilename, c);



        txtU = new JTextField("4", 7);
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 2;
        pane1.add(txtU, c);

        txtNbreItem = new JTextField("10000", 7);
        c.gridx = 1;
        c.gridy = 2;
        c.weightx = 1;
        pane1.add(txtNbreItem, c);

        txtNbreSpecificItem = new JTextField("50", 7);
        c.gridx = 1;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(txtNbreSpecificItem, c);

        removeSpecific = new JTextField("54", 7);
        c.gridx = 1;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(removeSpecific, c);

        chercheSpecific = new JTextField("N° secu", 7);
        c.gridx = 1;
        c.gridy = 5;
        c.weightx = 1;
        c.gridwidth = 1;
        pane1.add(chercheSpecific, c);



        buttonAddMany = new JButton("Ajouter n elements aleatoires a l'arbre");
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonAddMany, c);

        buttonAddItem = new JButton("Ajouter l'element");
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonAddItem, c);

        buttonRemove = new JButton("Supprimer l'element n de l'arbre");
        c.gridx = 2;
        c.gridy = 3;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonRemove, c);

        buttonSave = new JButton("Sauver l'arbre");
        c.gridx = 2;
        c.gridy = 4;
        c.weightx = 0.5;
        c.gridwidth = 1;
        pane1.add(buttonSave, c);

        buttonLoad = new JButton("Charger l'arbre");
        c.gridx = 3;
        c.gridy = 4;
        c.weightx = 0.5;
        c.gridwidth = 1;
        pane1.add(buttonLoad, c);

        buttonClean = new JButton("Reset");
        c.gridx = 2;
        c.gridy = 5;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonClean, c);

        buttonRefresh = new JButton("Refresh");
        c.gridx = 2;
        c.gridy = 6;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonRefresh, c);

        buttonLoadTxt = new JButton("Charger le fichier Txt");
        c.gridx= 2;
        c.gridy = 7;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonLoadTxt, c);

        buttonGenerateTxt = new JButton("Generer txt");
        c.gridx = 2;
        c.gridy = 8;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonGenerateTxt, c);

        buttonChercherArbre = new JButton("Chercher dans arbre");
        c.gridx = 2;
        c.gridy = 9;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonChercherArbre, c);

        buttonChercherTexte = new JButton("Chercher dans fichier");
        c.gridx = 2;
        c.gridy = 10;
        c.weightx = 1;
        c.gridwidth = 2;
        pane1.add(buttonChercherTexte, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 400;       //reset to default
        c.weighty = 1.0;   //request any extra vertical space
        c.gridwidth = 4;   //2 columns wide
        c.gridx = 0;
        c.gridy = 11;

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
        buttonGenerateTxt.addActionListener(this);
        buttonChercherArbre.addActionListener(this);
        buttonChercherTexte.addActionListener(this);

        return pane1;
    }
}

