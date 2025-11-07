import javax.swing.*;
public class controler{
    public static void main(String[]args){
        JFrame ventana= new JFrame("ventana");
        ventana.setSize(700,700);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setLayout(null);

        JLabel etiqueta = new JLabel("Presona el Boton para iniciar el juego",JLabel.CENTER);
        etiqueta.setBounds(50, 30, 300, 30);
        ventana.add(etiqueta);

        JButton boton=new JButton("Presioname");
        boton.setBounds(90,100,120,30);
        ventana.add(boton);
        
        boton.addActionListener(e ->{
            JOptionPane.showMessageDialog(ventana,"¡Botonpresionado!");
        });
        ventana.setVisible(true);
    }
}