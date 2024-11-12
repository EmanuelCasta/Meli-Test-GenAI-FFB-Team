package com.mutant.dna.domain.model;

import java.util.Set;

public class Mutant {

    /**
     * letras válidas para la secuencia de ADN
    **/
    private static final Set<Character> LETRAS_PERMITIDAS = Set.of('A', 'T', 'C', 'G');

    /**
     * Verifica si se cumple con las secuencias
     * Una secuencia es mutante si tiene al menos dos secuencias de cuatro caracteres consecutivos iguales en cualquier dirección.
     *
     * Este método optimiza el recorrido de la matriz ADN de tamaño N x N,
     * en lugar de analizar cada posición se divide el recorrido en filas pares e impares donde
     * las filas pares recorren cada tercera columna `columna += 3`, evitando posiciones redundantes y
     * reduciendo el número de comparaciones al analizar solo una fracción de las posiciones. por otra parte,
     * las filas impares realiza comprobaciones en posiciones intermedias con 2 pasos por 1 vacio.
     *
     * O(5n*n/12)
     *
     * @param dna Array de strings que representa la secuencia ADN en una matriz N x N.
     * @return true si se identifican al menos dos secuencias de mutantes, false en caso contrario.
     */
    public static boolean isMutant(String[] dna) {
        int n = dna.length;
        int numeroSecuenciasDeMutantes = 0;


        boolean[][] posicionesVerificadas = new boolean[n][n];

        for (int fila = 0; fila < n; fila++) {
            if (fila % 2 == 0) {
                for (int col = 0; col < n; col += 3) {
                    if (verificarSecuencia(dna, fila, col, n, posicionesVerificadas)) {
                        numeroSecuenciasDeMutantes++;
                        if (numeroSecuenciasDeMutantes > 1) return true;
                    }
                }
            } else {
                for (int col = 1; col < n; ) {
                    if (verificarSecuencia(dna, fila, col, n, posicionesVerificadas)) {
                        numeroSecuenciasDeMutantes++;
                        if (numeroSecuenciasDeMutantes > 1) return true;
                    }
                    col++;
                    if (col < n) {
                        if (verificarSecuencia(dna, fila, col, n, posicionesVerificadas)) {
                            numeroSecuenciasDeMutantes++;
                            if (numeroSecuenciasDeMutantes > 1) return true;
                        }
                    }

                    col += 2;
                }
            }
        }

        return false;
    }


    /**
     * Verifica si hay una secuencia válida de mutante a partir de una posición específica.
     *
     * @param dna Secuencia de ADN.
     * @param fila Fila de inicio.
     * @param col Columna de inicio.
     * @param n Tamaño de la matriz.
     * @param posicionesVerificadas Matriz de posiciones ya verificadas.
     * @return true si se encuentra una secuencia válida, false en caso contrario.
     */
    private static boolean verificarSecuencia(String[] dna, int fila, int col, int n, boolean[][] posicionesVerificadas) {
        char letraActual = dna[fila].charAt(col);


        if (!posicionesVerificadas[fila][col]) {
            int secuenciasEncontradas = 0;
            secuenciasEncontradas += contarSecuenciaEnHorizontal(dna, fila, col, n, letraActual, posicionesVerificadas);
            secuenciasEncontradas += contarSecuenciaEnVertical(dna, fila, col, n, letraActual, posicionesVerificadas);
            secuenciasEncontradas += contarSecuenciaEnDiagonal(dna, fila, col, n, letraActual, posicionesVerificadas);
            secuenciasEncontradas += contarSecuenciaEnAntiDiagonal(dna, fila, col, n, letraActual, posicionesVerificadas);

            return secuenciasEncontradas > 0;
        }
        return false;
    }

    private static int contarSecuenciaEnHorizontal(String[] dna, int row, int col, int n, char letraActual, boolean[][] posicionesVerificadas) {
        int conteo = 1;

        for (int i = 1; i < 4 && col + i < n; i++) {
            if (dna[row].charAt(col + i) == letraActual) {
                conteo++;
            } else {
                break;
            }
        }

        for (int i = 1; i < 4 && col - i >= 0; i++) {
            if (dna[row].charAt(col - i) == letraActual) {
                conteo++;
            } else {
                break;
            }
        }

        if (conteo >= 4) {
            for (int i = 0; i < 4; i++) {
                if (col + i < n) posicionesVerificadas[row][col + i] = true;
                if (col - i >= 0) posicionesVerificadas[row][col - i] = true;
            }
            return 1;
        }
        return 0;
    }

    /**
     * Cuenta secuencias verticales de cuatro caracteres consecutivos iguales en una posición específica de la matriz ADN.
     *
     * Este método verifica la columna de la fila en la dirección vertical hacia arriba y hacia abajo,
     * buscando cuatro caracteres consecutivos iguales a la letra dada, en caso de encontrar una secuencia válida,
     * marca las posiciones en la matriz posicionesVerificadas para evitar que se vuelvan a contar y retorna 1 para sumar a la secuencia.
     * en otro caso retorna 0.
     *
     * @param dna                Array de strings que representa la matriz ADN.
     * @param row                Fila inicial en la matriz donde empieza la búsqueda vertical.
     * @param col                Columna inicial en la matriz donde empieza la búsqueda vertical.
     * @param n                  Tamaño de la matriz (asumida como N x N).
     * @param letraActual        Caracter de referencia que se busca en la secuencia vertical.
     * @param posicionesVerificadas Matriz que marca las posiciones ya verificadas para evitar contar secuencias duplicadas.
     * @return 1 si se encuentra una secuencia de cuatro caracteres consecutivos iguales, 0 si no se encuentra.
     */
    private static int contarSecuenciaEnVertical(String[] dna, int row, int col, int n, char letraActual, boolean[][] posicionesVerificadas) {
        int conteo = 1;

        for (int i = 1; i < 4 && row + i < n; i++) {
            if (dna[row + i].charAt(col) == letraActual) {
                conteo++;
            } else {
                break;
            }
        }

        for (int i = 1; i < 4 && row - i >= 0; i++) {
            if (dna[row - i].charAt(col) == letraActual) {
                conteo++;
            } else {
                break;
            }
        }

        if (conteo >= 4) {
            for (int i = 0; i < 4; i++) {
                if (row + i < n) posicionesVerificadas[row + i][col] = true;
                if (row - i >= 0) posicionesVerificadas[row - i][col] = true;
            }
            return 1;
        }
        return 0;
    }

    /**
     * Cuenta secuencias diagonales de cuatro caracteres consecutivos iguales en una posición específica de la matriz ADN.
     *
     * Este método verifica en la dirección diagonal (↘ y ↖), hacia abajo a la derecha y hacia arriba a la izquierda,
     * buscando cuatro caracteres consecutivos iguales a `letraActual`. Si encuentra una secuencia válida,
     * marca las posiciones en la matriz `posicionesVerificadas` y retorna 1 para indicar la secuencia.
     * En caso contrario, retorna 0.
     *
     * @param dna                Array de strings que representa la matriz ADN.
     * @param row                Fila inicial en la matriz donde empieza la búsqueda diagonal.
     * @param col                Columna inicial en la matriz donde empieza la búsqueda diagonal.
     * @param n                  Tamaño de la matriz (asumida como N x N).
     * @param letraActual        Carácter de referencia que se busca en la secuencia diagonal.
     * @param posicionesVerificadas Matriz que marca las posiciones ya verificadas para evitar contar secuencias duplicadas.
     * @return 1 si se encuentra una secuencia de cuatro caracteres consecutivos iguales, 0 si no se encuentra.
     */
    private static int contarSecuenciaEnDiagonal(String[] dna, int row, int col, int n, char letraActual, boolean[][] posicionesVerificadas) {
        int conteo = 1;

        for (int i = 1; i < 4 && row + i < n && col + i < n; i++) {
            if (dna[row + i].charAt(col + i) == letraActual) {
                conteo++;
            } else {
                break;
            }
        }

        for (int i = 1; i < 4 && row - i >= 0 && col - i >= 0; i++) {
            if (dna[row - i].charAt(col - i) == letraActual) {
                conteo++;
            } else {
                break;
            }
        }

        if (conteo >= 4) {
            for (int i = 0; i < 4; i++) {
                if (row + i < n && col + i < n) posicionesVerificadas[row + i][col + i] = true;
                if (row - i >= 0 && col - i >= 0) posicionesVerificadas[row - i][col - i] = true;
            }
            return 1;
        }
        return 0;
    }

    /**
     * Cuenta secuencias anti-diagonales de cuatro caracteres consecutivos iguales en una posición específica de la matriz ADN.
     *
     * Este método verifica en la dirección anti-diagonal (↙ y ↗), hacia abajo a la izquierda y hacia arriba a la derecha,
     * buscando cuatro caracteres consecutivos iguales a `letraActual`. Si encuentra una secuencia válida,
     * marca las posiciones en la matriz `posicionesVerificadas` para evitar que se vuelvan a contar y retorna 1.
     * En caso contrario, retorna 0.
     *
     * @param dna                Array de strings que representa la matriz ADN.
     * @param row                Fila inicial en la matriz donde empieza la búsqueda anti-diagonal.
     * @param col                Columna inicial en la matriz donde empieza la búsqueda anti-diagonal.
     * @param n                  Tamaño de la matriz (asumida como N x N).
     * @param letraActual        Carácter de referencia que se busca en la secuencia anti-diagonal.
     * @param posicionesVerificadas Matriz que marca las posiciones ya verificadas para evitar contar secuencias duplicadas.
     * @return 1 si se encuentra una secuencia de cuatro caracteres consecutivos iguales, 0 si no se encuentra.
     */
    private static int contarSecuenciaEnAntiDiagonal(String[] dna, int row, int col, int n, char letraActual, boolean[][] posicionesVerificadas) {
        int conteo = 1;

        for (int i = 1; i < 4 && row + i < n && col - i >= 0; i++) {
            if (dna[row + i].charAt(col - i) == letraActual) {
                conteo++;
            } else {
                break;
            }
        }

        for (int i = 1; i < 4 && row - i >= 0 && col + i < n; i++) {
            if (dna[row - i].charAt(col + i) == letraActual) {
                conteo++;
            } else {
                break;
            }
        }

        if (conteo >= 4) {
            for (int i = 0; i < 4; i++) {
                if (row + i < n && col - i >= 0) posicionesVerificadas[row + i][col - i] = true;
                if (row - i >= 0 && col + i < n) posicionesVerificadas[row - i][col + i] = true;
            }
            return 1;
        }
        return 0;
    }


    /**
     * Verifica si un carácter es permitido en la secuencia de ADN.
     *
     * @param letraActual Carácter a verificar.
     * @return true si el carácter está en el conjunto de letras permitidas, false en caso contrario.
     */
    public static boolean esLetraPermitida(char letraActual) {
        return LETRAS_PERMITIDAS.contains(letraActual);
    }
}

