package com.mutant.dna.domain.model;

import java.util.ArrayList;
import java.util.List;
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

        int mid = n / 2;

        for (int k = 0; k <= n; k++) {
            int fila;

            if (k == 0) {
                fila = mid;
            } else if (k % 2 == 1) {
                fila = mid + (k + 1) / 2;
            } else {
                fila = mid - (k + 1) / 2;
            }
            if (fila < 0 || fila >= n) continue;

            validResultRules(dna[fila],n);

            if (fila % 2 == 0) {


                for (int col = 0; col < n; col += 3) {
                    ResultadoSecuencia resultadoSecuencia = verificarSecuencia(dna, fila, col, n, posicionesVerificadas);
                    if (resultadoSecuencia.esMutante()) {
                        numeroSecuenciasDeMutantes = numeroSecuenciasDeMutantes + resultadoSecuencia.numeroSecuencias();
                        if (numeroSecuenciasDeMutantes > 1) return true;
                    }
                }
            } else {
                for (int col = 1; col < n; ) {
                    ResultadoSecuencia resultadoSecuencia = verificarSecuencia(dna, fila, col, n, posicionesVerificadas);
                    if (resultadoSecuencia.esMutante()) {
                        numeroSecuenciasDeMutantes = numeroSecuenciasDeMutantes + resultadoSecuencia.numeroSecuencias();
                        if (numeroSecuenciasDeMutantes > 1) return true;
                    }
                    col++;
                    if (col < n) {
                        resultadoSecuencia = verificarSecuencia(dna, fila, col, n, posicionesVerificadas);
                        if (resultadoSecuencia.esMutante()) {
                            numeroSecuenciasDeMutantes = numeroSecuenciasDeMutantes + resultadoSecuencia.numeroSecuencias();
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
     * Método que valida reglas específicas para una secuencia de caracteres.
     * Verifica si la longitud de la fila cumple con las reglas establecidas
     * para un resultado válido en una matriz N*N.
     *
     * @param fila La secuencia de caracteres (cadena) a validar.
     * @param n El tamaño N de la matriz N*N (es decir, la longitud que debe tener cada fila).
     * @throws RuntimeException Si la longitud de la fila no es igual a N o si es menor que el número requerido de consecutivos.
     */
    public static void validResultRules(String fila, int n){
        int numeroTotalDeConsecutivos = 4;

        if (fila.length() != n) {
            throw new RuntimeException("No cumple la regla N(" + n + ") * N(" + n + ").");
        }

        if (fila.length() < numeroTotalDeConsecutivos) {
            throw new RuntimeException("No cumple la regla mínima para que haya al menos " + numeroTotalDeConsecutivos + " consecutivos.");
        }


    }


    /**
     * Verifica si hay una secuencia válida de mutante a partir de una posición específica.
     *
     * @param dna Secuencia de ADN.
     * @param fila Fila de inicio.
     * @param col Columna de inicio.
     * @param n Tamaño de la matriz.
     * @param posicionesVerificadas Matriz de posiciones ya verificadas.
     * @return ResultadoSecuencia si se encuentra una secuencia válida, false en caso contrario.
     */
    private static ResultadoSecuencia verificarSecuencia(String[] dna, int fila, int col, int n, boolean[][] posicionesVerificadas) {
        char letraActual = dna[fila].charAt(col);

        if (!Mutant.esLetraPermitida(letraActual)) {
            throw new RuntimeException("La secuencia contiene caracteres no permitidos. Solo se permiten las letras A, T, C, G. Valor encontrado "+letraActual+" fila: "+fila +" Columna: "+col);
        }

        if (!posicionesVerificadas[fila][col]) {
            int secuenciasEncontradas = 0;
            secuenciasEncontradas += contarSecuenciaEnHorizontal(dna, fila, col, n, letraActual, posicionesVerificadas);
            secuenciasEncontradas += contarSecuenciaEnVertical(dna, fila, col, n, letraActual, posicionesVerificadas);
            secuenciasEncontradas += contarSecuenciaEnDiagonal(dna, fila, col, n, letraActual, posicionesVerificadas);
            secuenciasEncontradas += contarSecuenciaEnAntiDiagonal(dna, fila, col, n, letraActual, posicionesVerificadas);

            return new ResultadoSecuencia(secuenciasEncontradas > 0, secuenciasEncontradas);
        }
        return new ResultadoSecuencia(false, 0);
    }

    private static int contarSecuenciaEnHorizontal(String[] dna, int row, int col, int n, char letraActual, boolean[][] posicionesVerificadas) {
        List<int[]> posicionesCandidatas = new ArrayList<>();

        int inicio = col;
        while (inicio >= 0 && dna[row].charAt(inicio) == letraActual) {
            inicio--;
        }
        inicio++;

        int fin = col;
        while (fin < n && dna[row].charAt(fin) == letraActual) {
            fin++;
        }
        fin--;

        for (int i = inicio; i <= fin; i++) {
            posicionesCandidatas.add(new int[]{row, i});
        }

        int totalLetras = posicionesCandidatas.size();
        int sequencesFound = 0;
        int index = 0;

        if(totalLetras>3) {
            while (index + 4 <= totalLetras) {
                for (int j = index; j < index + 4; j++) {
                    int[] pos = posicionesCandidatas.get(j);
                    posicionesVerificadas[pos[0]][pos[1]] = true;
                }
                sequencesFound++;
                index += 4;
            }


            for (int i = index; i < totalLetras; i++) {
                int[] pos = posicionesCandidatas.get(i);
                posicionesVerificadas[pos[0]][pos[1]] = true;
            }
        }

        return sequencesFound;
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

        List<int[]> posicionesCandidatas = new ArrayList<>();

        int inicio = row;
        while (inicio >= 0 && dna[inicio].charAt(col) == letraActual) {
            inicio--;
        }
        inicio++;

        int fin = row;
        while (fin < n && dna[fin].charAt(col) == letraActual) {
            fin++;
        }
        fin--;


        for (int i = inicio; i <= fin; i++) {
            posicionesCandidatas.add(new int[]{i, col});
        }

        int totalLetras = posicionesCandidatas.size();
        int sequencesFound = 0;
        int index = 0;
        if(totalLetras>3) {
            while (index + 4 <= totalLetras) {
                for (int j = index; j < index + 4; j++) {
                    int[] pos = posicionesCandidatas.get(j);
                    posicionesVerificadas[pos[0]][pos[1]] = true;
                }
                sequencesFound++;
                index += 4;
            }
            for (int i = index; i < totalLetras; i++) {
                int[] pos = posicionesCandidatas.get(i);
                posicionesVerificadas[pos[0]][pos[1]] = true;
            }
        }

        return sequencesFound;
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
        List<int[]> posicionesCandidatas = new ArrayList<>();

        int i = row, j = col;
        while (i >= 0 && j < n && dna[i].charAt(j) == letraActual) {
            i--;
            j++;
        }
        i++;
        j--;

        int finI = row, finJ = col;
        while (finI < n && finJ >= 0 && dna[finI].charAt(finJ) == letraActual) {
            finI++;
            finJ--;
        }
        finI--;
        finJ++;

        while (i <= finI && j >= finJ) {
            posicionesCandidatas.add(new int[]{i, j});
            i++;
            j--;
        }

        int totalLetras = posicionesCandidatas.size();
        int sequencesFound = 0;
        int index = 0;
        if(totalLetras>3) {
            while (index + 4 <= totalLetras) {
                for (int k = index; k < index + 4; k++) {
                    int[] pos = posicionesCandidatas.get(k);
                    posicionesVerificadas[pos[0]][pos[1]] = true;
                }
                sequencesFound++;
                index += 4;
            }


            for (int k = index; k < totalLetras; k++) {
                int[] pos = posicionesCandidatas.get(k);
                posicionesVerificadas[pos[0]][pos[1]] = true;
            }
        }

        return sequencesFound;
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

        List<int[]> posicionesCandidatas = new ArrayList<>();

        int i = row, j = col;
        while (i >= 0 && j >= 0 && dna[i].charAt(j) == letraActual) {
            i--;
            j--;
        }
        i++;
        j++;

        int finI = row, finJ = col;
        while (finI < n && finJ < n && dna[finI].charAt(finJ) == letraActual) {
            finI++;
            finJ++;
        }
        finI--;
        finJ--;

        while (i <= finI && j <= finJ) {
            posicionesCandidatas.add(new int[]{i, j});
            i++;
            j++;
        }

        int totalLetras = posicionesCandidatas.size();
        int sequencesFound = 0;
        int index = 0;
        if(totalLetras>3) {
            while (index + 4 <= totalLetras) {
                for (int k = index; k < index + 4; k++) {
                    int[] pos = posicionesCandidatas.get(k);
                    posicionesVerificadas[pos[0]][pos[1]] = true;
                }
                sequencesFound++;
                index += 4;
            }


            for (int k = index; k < totalLetras; k++) {
                int[] pos = posicionesCandidatas.get(k);
                posicionesVerificadas[pos[0]][pos[1]] = true;
            }
        }

        return sequencesFound;

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


