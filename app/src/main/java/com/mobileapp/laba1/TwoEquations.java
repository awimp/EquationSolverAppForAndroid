package com.mobileapp.laba1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.apache.commons.math3.linear.DecompositionSolver;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.linear.RealVector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class TwoEquations extends Fragment {

    private EditText editTextCoeficient;
    private Button buttonMatrixMethod;
    private Button buttonGausMethod;
    private Button saveButton;
    private Button openButton;
    private TextView textViewResult;
    private static double[][] coefficients;

    private static final String FILE_NAME = "coefficients.txt";

    public TwoEquations() {
        // Порожній конструктор, обов'язковий для фрагментів.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Інфляція макету для цього фрагмента
        View view = inflater.inflate(R.layout.fragment_two_equations, container, false);

        editTextCoeficient = view.findViewById(R.id.editTextCoeficient);
        buttonMatrixMethod = view.findViewById(R.id.buttonMatrixMethod);
        buttonGausMethod = view.findViewById(R.id.buttonGausMethod);
        saveButton = view.findViewById(R.id.saveTwoEquationsButton);
        openButton = view.findViewById(R.id.openTwoEquationsButton);
        textViewResult = view.findViewById(R.id.textViewResult);

        buttonMatrixMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solveMatrixMethod();
            }
        });

        buttonGausMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                solveGausMethod();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCoefficientsToFile();
            }
        });

        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCoefficientsFromFile();
            }
        });

        return view;
    }

    private void solveMatrixMethod() {
        try {
            // Отримати рядки з EditText та розділити їх на масиви коефіцієнтів
            String[] lines = editTextCoeficient.getText().toString().split("\n");
            int rows = lines.length;
            int cols = lines[0].split(" ").length;

            double[][] coefficients = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                String[] rowValues = lines[i].split(" ");
                for (int j = 0; j < cols; j++) {
                    coefficients[i][j] = Double.parseDouble(rowValues[j]);
                }
            }

            RealMatrix matrix = MatrixUtils.createRealMatrix(coefficients);
            double[] constants = matrix.getColumn(cols - 1);
            RealMatrix coefficientsMatrix = matrix.getSubMatrix(0, rows - 1, 0, cols - 2);

            try {
                DecompositionSolver solver = new LUDecomposition(coefficientsMatrix).getSolver();
                RealVector constantsVector = MatrixUtils.createRealVector(constants);
                RealVector solution = solver.solve(constantsVector);
                textViewResult.setText("x = " + solution.getEntry(0) + "\ny = " + solution.getEntry(1));
            } catch (SingularMatrixException e) {
                textViewResult.setText("Система має або не має розв'язків");
            }
            coefficients = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                String[] rowValues = lines[i].split(" ");
                for (int j = 0; j < cols; j++) {
                    coefficients[i][j] = Double.parseDouble(rowValues[j]);
                }
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Помилка у форматі введених даних", Toast.LENGTH_SHORT).show();
        }
    }

    public static double[][] getCoefficients() {
        return coefficients;
    }
    private void solveGausMethod() {
        try {
            // Отримати рядки з EditText та розділити їх на масиви коефіцієнтів
            String[] lines = editTextCoeficient.getText().toString().split("\n");
            int rows = lines.length;
            int cols = lines[0].split(" ").length;

            double[][] coefficients = new double[rows][cols];
            for (int i = 0; i < rows; i++) {
                String[] rowValues = lines[i].split(" ");
                for (int j = 0; j < cols; j++) {
                    coefficients[i][j] = Double.parseDouble(rowValues[j]);
                }
            }

            RealMatrix matrix = MatrixUtils.createRealMatrix(coefficients);
            double[] constants = matrix.getColumn(cols - 1);
            RealMatrix coefficientsMatrix = matrix.getSubMatrix(0, rows - 1, 0, cols - 2);

            try {
                DecompositionSolver solver = new LUDecomposition(coefficientsMatrix).getSolver();
                RealVector constantsVector = MatrixUtils.createRealVector(constants);
                RealVector solution = solver.solve(constantsVector);
                textViewResult.setText("x = " + solution.getEntry(0) + "\ny = " + solution.getEntry(1));
            } catch (SingularMatrixException e) {
                textViewResult.setText("Система має або не має розв'язків");
            }

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Помилка у форматі введених даних", Toast.LENGTH_SHORT).show();
        }
    }


    private void saveCoefficientsToFile() {
        // Зберегти коефіцієнти у файл
        String coefficients = editTextCoeficient.getText().toString();
        FileOutputStream fos = null;

        try {
            fos = getContext().openFileOutput(FILE_NAME, getContext().MODE_PRIVATE);
            fos.write(coefficients.getBytes());
            Toast.makeText(getContext(), "Збережено у файл " + FILE_NAME, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void openCoefficientsFromFile() {
        // Відкрити коефіцієнти з файлу
        File file = new File(getContext().getFilesDir(), FILE_NAME);

        if (file.exists()) {
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();

                editTextCoeficient.setText(text.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Файл не існує", Toast.LENGTH_SHORT).show();
        }
    }


}
