package com.alura.challenge;


import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyConverterGUI {
    private JFrame frame;
    private JPanel panel;
    private JLabel titleLabel;
    private JLabel fromLabel;
    private JLabel toLabel;
    private JLabel amountLabel;
    private JComboBox<String> fromComboBox;
    private JComboBox<String> toComboBox;
    private JTextField amountField;
    private JButton convertButton;

    public CurrencyConverterGUI() {
        // Crear componentes de la interfaz gráfica y configurarlos
        frame = new JFrame("Currency Converter");
        panel = new JPanel();
        titleLabel = new JLabel("Currency Converter");
        fromLabel = new JLabel("From:");
        toLabel = new JLabel("To:");
        amountLabel = new JLabel("Amount:");
        fromComboBox = new JComboBox<>();
        toComboBox = new JComboBox<>();
        amountField = new JTextField(10);
        convertButton = new JButton("Convert");

        // Agregar las monedas al ComboBox
        String[] currencies = {"USD", "EUR", "GBP", "AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "HKD",
                "HRK", "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN",
                "RON", "RUB", "SEK", "SGD", "THB", "TRY", "ZAR"};
        for (String currency : currencies) {
            fromComboBox.addItem(currency);
            toComboBox.addItem(currency);
        }

        // Configurar el panel
        panel.setLayout(new GridLayout(4, 2, 10, 10));
        panel.add(titleLabel);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(fromLabel);
        panel.add(fromComboBox);
        panel.add(toLabel);
        panel.add(toComboBox);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(new JLabel()); // Espacio en blanco
        panel.add(convertButton);

        // Configurar el botón de conversión
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertCurrency();
            }
        });

        // Configurar el frame
        frame.add(panel);
        frame.pack();
        frame.setSize(450, 200); // Ajusta el tamaño de la ventana
        frame.setLocationRelativeTo(null); // Centrar la ventana en la pantalla
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void show() {
        // Mostrar la ventana
        frame.setVisible(true);
    }

    private void convertCurrency() {
        String apiKey = "zzz"; // Reemplaza con tu API key
        String fromCurrency = (String) fromComboBox.getSelectedItem();
        String toCurrency = (String) toComboBox.getSelectedItem();
        double amount = Double.parseDouble(amountField.getText());

        try {
            // Realizar la solicitud GET a la API
            URL url = new URL("https://api.freecurrencyapi.com/v1/latest?apikey=" + apiKey);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            //respuesta JSON utilizando "org.json"
            JSONObject jsonObject = new JSONObject(response.toString());
            JSONObject ratesObject = jsonObject.getJSONObject("data");

            // Obtener las tasas de conversión
            double fromRate = ratesObject.getDouble(fromCurrency);
            double toRate = ratesObject.getDouble(toCurrency);

            // Realizar la conversión y mostrar el resultado
            double result = amount * (toRate / fromRate);
            JOptionPane.showMessageDialog(frame, amount + " " + fromCurrency + " = " + result + " " + toCurrency);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error al obtener los datos de la API.");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        CurrencyConverterGUI gui = new CurrencyConverterGUI();
        gui.show();
    }
}
