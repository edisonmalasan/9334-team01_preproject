package Client.view;

import javax.swing.*;
import java.awt.*;

public class CategoryView extends JFrame {
    public JButton category1, category2, category3, category4, category5, category6;
    public CategoryView(){
        setTitle("Select Category");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setLayout(new GridLayout(2,3));

        category1 = new JButton("Algebra");
        category2 = new JButton("Geometry");
        category3 = new JButton("Problem-Solving");
        category4 = new JButton("Logic");
        category5 = new JButton("Arithmetic");
        category6 = new JButton("Trigonometry");

        add(category1);
        add(category2);
        add(category3);
        add(category4);
        add(category5);
        add(category6);
    }

    public JButton getCategory1() {
        return category1;
    }

    public JButton getCategory2() {
        return category2;
    }

    public JButton getCategory3() {
        return category3;
    }
    public JButton getCategory4() {
        return category4;
    }
    public JButton getCategory5() {
        return category5;
    }
    public JButton getCategory6() {
        return category6;
    }
}
