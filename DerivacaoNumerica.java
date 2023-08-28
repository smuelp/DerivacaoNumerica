import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.lang.Math;

public class DerivacaoNumerica {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            criarEMostrarGUI();
        });
    }

    private static void criarEMostrarGUI() {
        JFrame frame = new JFrame("Derivação Numérica");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JPanel painelEntrada = new JPanel(new GridLayout(5, 2, 10, 10));
        painelEntrada.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel rotuloOpcao = new JLabel("Escolha uma opção:");
        JComboBox<String> comboBoxOpcao = new JComboBox<>(new String[] {
                "---------- Selecione um método ----------",
                "Cálculos Gerais - Explorando x & h",
                "Derivada por Interpolação Numérica",
                "Derivada por Diferença Finita (Taylor)",
                "Segunda Derivada por Diferença Finita",
                "Expressões Derivadas - Representação"
        });

        JLabel rotuloFuncao = new JLabel("Digite a função:");
        JTextField campoTextoFuncao = new JTextField();

        JLabel rotuloX = new JLabel("Digite o valor de x:");
        JTextField campoTextoX = new JTextField();

        JLabel rotuloH = new JLabel("Digite o valor de h:");
        JTextField campoTextoH = new JTextField();

        painelEntrada.add(rotuloOpcao);
        painelEntrada.add(comboBoxOpcao);
        painelEntrada.add(rotuloFuncao);
        painelEntrada.add(campoTextoFuncao);
        painelEntrada.add(rotuloX);
        painelEntrada.add(campoTextoX);
        painelEntrada.add(rotuloH);
        painelEntrada.add(campoTextoH);

        JButton botaoCalcular = new JButton("Calcular");

        JTextArea areaTextoResultado = new JTextArea(10, 50);
        areaTextoResultado.setEditable(false);
        areaTextoResultado.setWrapStyleWord(true);
        areaTextoResultado.setLineWrap(true);

        rotuloFuncao.setVisible(false);
        campoTextoFuncao.setVisible(false);
        rotuloX.setVisible(false);
        campoTextoX.setVisible(false);
        rotuloH.setVisible(false);
        campoTextoH.setVisible(false);

        comboBoxOpcao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String opcaoSelecionada = (String) comboBoxOpcao.getSelectedItem();
                boolean mostrarCampos = !opcaoSelecionada.equals("---------- Selecione um método ----------");

                if (opcaoSelecionada.equals("Expressões Derivadas - Representação")) {
                    rotuloFuncao.setVisible(true);
                    campoTextoFuncao.setVisible(true);
                    rotuloX.setVisible(false);
                    campoTextoX.setVisible(false);
                    rotuloH.setVisible(false);
                    campoTextoH.setVisible(false);
                } else {
                    rotuloFuncao.setVisible(false);
                    campoTextoFuncao.setVisible(false);
                    rotuloX.setVisible(mostrarCampos);
                    campoTextoX.setVisible(mostrarCampos);
                    rotuloH.setVisible(mostrarCampos);
                    campoTextoH.setVisible(mostrarCampos);
                }
            }
        });

        botaoCalcular.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String opcao = (String) comboBoxOpcao.getSelectedItem();
                boolean mostrarEntradaFuncao = opcao.equals("Expressões Derivadas - Representação");
                rotuloFuncao.setVisible(mostrarEntradaFuncao);
                campoTextoFuncao.setVisible(mostrarEntradaFuncao);
                rotuloX.setVisible(!mostrarEntradaFuncao);
                campoTextoX.setVisible(!mostrarEntradaFuncao);
                rotuloH.setVisible(!mostrarEntradaFuncao);
                campoTextoH.setVisible(!mostrarEntradaFuncao);

                if (mostrarEntradaFuncao) {
                    // Cálculos específicos para a opção "Derivada Direta da Expressão"
                    String expressao = campoTextoFuncao.getText();
                    String expressaoDerivada1 = calcularDerivada(expressao);
                    String expressaoDerivada2 = calcularSegundaDerivada(expressaoDerivada1);

                    areaTextoResultado.setText("f'(x) = " + expressaoDerivada1 + "\nf''(x) = " + expressaoDerivada2);
                } else {
                    rotuloFuncao.setVisible(false);
                    campoTextoFuncao.setVisible(false);
                    // double x = Double.parseDouble(campoTextoX.getText());
                    // double h = Double.parseDouble(campoTextoH.getText());

                    try {
                        double x = Double.parseDouble(campoTextoX.getText());
                        double h = Double.parseDouble(campoTextoH.getText());

                        if (h == 0) {
                            JOptionPane.showMessageDialog(null, "O valor de h não pode ser zero.", "Erro de Cálculo",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        StringBuilder resultado = new StringBuilder();
                        DecimalFormat formatoDecimal = new DecimalFormat("0.0000");

                        // cálculos das derivadas e erros dependendo da opção selecionada
                        if (opcao.equals("Derivada por Interpolação Numérica")) {
                            double dp1 = derivadaProgressivaOrdem1(x, h);
                            double dr1 = derivadaRegressivaOrdem1(x, h);
                            double dc2 = derivadaCentralOrdem2(x, h);
                            double dp2 = derivadaProgressivaOrdem2(x, h);
                            double dr2 = derivadaRegressivaOrdem2(x, h);
                            double dc4 = derivadaCentralOrdem4(x, h);

                            double derivadaExata = -2 * x * Math.exp(-x * x);

                            // cálculo dos erros
                            double erroP1 = Math.abs(derivadaExata - dp1);
                            double erroR1 = Math.abs(derivadaExata - dr1);
                            double erroC2 = Math.abs(derivadaExata - dc2);
                            double erroP2 = Math.abs(derivadaExata - dp2);
                            double erroR2 = Math.abs(derivadaExata - dr2);
                            double erroC4 = Math.abs(derivadaExata - dc4);

                            double erroRelativoP1 = erroP1 / Math.abs(derivadaExata);
                            double erroRelativoR1 = erroR1 / Math.abs(derivadaExata);
                            double erroRelativoC2 = erroC2 / Math.abs(derivadaExata);
                            double erroRelativoP2 = erroP2 / Math.abs(derivadaExata);
                            double erroRelativoR2 = erroR2 / Math.abs(derivadaExata);
                            double erroRelativoC4 = erroC4 / Math.abs(derivadaExata);

                            // constrói a saída formatada
                            resultado.append("Progressivas de ordem 1: ").append(formatoDecimal.format(dp1))
                                    .append("\n");
                            resultado.append("Erro absoluto P1: ").append(formatoDecimal.format(erroP1)).append("\n");
                            resultado.append("Erro relativo P1: ").append(formatoDecimal.format(erroRelativoP1))
                                    .append("\n\n");

                            resultado.append("Regressivas de ordem 1: ").append(formatoDecimal.format(dr1))
                                    .append("\n");
                            resultado.append("Erro absoluto R1: ").append(formatoDecimal.format(erroR1)).append("\n");
                            resultado.append("Erro relativo R1: ").append(formatoDecimal.format(erroRelativoR1))
                                    .append("\n\n");

                            resultado.append("Central de ordem 2: ").append(formatoDecimal.format(dc2)).append("\n");
                            resultado.append("Erro absoluto C2: ").append(formatoDecimal.format(erroC2)).append("\n");
                            resultado.append("Erro relativo C2: ").append(formatoDecimal.format(erroRelativoC2))
                                    .append("\n\n");

                            resultado.append("Progressivas de ordem 2: ").append(formatoDecimal.format(dp2))
                                    .append("\n");
                            resultado.append("Erro absoluto P2: ").append(formatoDecimal.format(erroP2)).append("\n");
                            resultado.append("Erro relativo P2: ").append(formatoDecimal.format(erroRelativoP2))
                                    .append("\n\n");

                            resultado.append("Regressivas de ordem 2: ").append(formatoDecimal.format(dr2))
                                    .append("\n");
                            resultado.append("Erro absoluto R2: ").append(formatoDecimal.format(erroR2)).append("\n");
                            resultado.append("Erro relativo R2: ").append(formatoDecimal.format(erroRelativoR2))
                                    .append("\n\n");

                            resultado.append("Central de ordem 4: ").append(formatoDecimal.format(dc4)).append("\n");
                            resultado.append("Erro absoluto C4: ").append(formatoDecimal.format(erroC4)).append("\n");
                            resultado.append("Erro relativo C4: ").append(formatoDecimal.format(erroRelativoC4));
                        } else if (opcao.equals("Derivada por Diferença Finita (Taylor)")) {
                            double taylor1 = taylorOrdem1(x, h);
                            double taylor2 = taylorOrdem2(x, h);

                            double derivadaExata = -2 * x * Math.exp(-x * x);

                            // cálculo dos erros
                            double erroTaylor1 = Math.abs(derivadaExata - taylor1);
                            double erroTaylor2 = Math.abs(derivadaExata - taylor2);

                            double erroRelativoTaylor1 = erroTaylor1 / Math.abs(derivadaExata);
                            double erroRelativoTaylor2 = erroTaylor2 / Math.abs(derivadaExata);

                            // constrói a saída formatada
                            resultado.append("Derivada de ordem 1 (Taylor): ").append(formatoDecimal.format(taylor1))
                                    .append("\n");
                            resultado.append("Erro absoluto Taylor1: ").append(formatoDecimal.format(erroTaylor1))
                                    .append("\n");
                            resultado.append("Erro relativo Taylor1: ")
                                    .append(formatoDecimal.format(erroRelativoTaylor1))
                                    .append("\n\n");

                            resultado.append("Derivada de ordem 2 (Taylor): ").append(formatoDecimal.format(taylor2))
                                    .append("\n");
                            resultado.append("Erro absoluto Taylor2: ").append(formatoDecimal.format(erroTaylor2))
                                    .append("\n");
                            resultado.append("Erro relativo Taylor2: ")
                                    .append(formatoDecimal.format(erroRelativoTaylor2));
                        } else if (opcao.equals("Segunda Derivada por Diferença Finita")) {
                            double segundaDerivada = segundaDerivadaDiferencaFinita(x, h);
                            double segundaDerivadaExata = -2 * Math.exp(-x * x) + 4 * x * x * Math.exp(-x * x);
                            double erroSegunda = Math.abs(segundaDerivadaExata - segundaDerivada);
                            double erroRelativoSegunda = erroSegunda / Math.abs(segundaDerivadaExata);

                            resultado.append("Segunda Derivada por Diferença Finita: ")
                                    .append(formatoDecimal.format(segundaDerivada)).append("\n");
                            resultado.append("Erro absoluto Segunda: ").append(formatoDecimal.format(erroSegunda))
                                    .append("\n");
                            resultado.append("Erro relativo Segunda: ")
                                    .append(formatoDecimal.format(erroRelativoSegunda));
                        } else if (opcao.equals("Cálculos Gerais - Explorando x & h")) {
                            double calculosRapidos = calculosGerais(x, h);
                            resultado.append("Resultado = ").append(formatoDecimal.format(calculosRapidos))
                                    .append("\n");
                        }
                        // define o resultado na área de texto
                        areaTextoResultado.setText(resultado.toString());
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Insira valores numéricos válidos para x e h.",
                                "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // painel para o botão de cálculo
        JPanel painelBotao = new JPanel();
        painelBotao.add(botaoCalcular);

        // adiciona os painéis à janela principal
        frame.add(painelEntrada, BorderLayout.NORTH);
        frame.add(new JScrollPane(areaTextoResultado), BorderLayout.CENTER);
        frame.add(painelBotao, BorderLayout.SOUTH);

        // torna a janela visível
        frame.setVisible(true);
    }

    // !!! método que calcula a função f(x)
    public static double f(double x) {
        return (3 * x + 9) / 2 - x;
        // return (Math.pow(x, 5) - 3 * Math.pow(x, 2));
        // return Math.tan(a) * Math.tan(36);
        // return (1.0 / Math.cos(x)) * Math.tan(x); // derivada da secante
        // return -1 * (1 / Math.sin(x)) * 1 / Math.tan(x); // derivada da cossecante
        // return -1 / Math.pow(Math.sin(x), 2); // derivada da cotangente
        // return Math.exp(x); // derivada de euler ^ x
    }

    // métodos para cálculo de derivadas numéricas

    public static double calculosGerais(double x, double h) {
        return x / h;
    }

    public static double derivadaProgressivaOrdem1(double x, double h) {
        return (f(x + h) - f(x)) / h;
    }

    public static double derivadaRegressivaOrdem1(double x, double h) {
        return (f(x) - f(x - h)) / h;
    }

    public static double derivadaCentralOrdem2(double x, double h) {
        return (f(x + h) - f(x - h)) / (2 * h);
    }

    public static double derivadaProgressivaOrdem2(double x, double h) {
        return (-3 * f(x) + 4 * f(x + h) - f(x + 2 * h)) / (2 * h);
    }

    public static double derivadaRegressivaOrdem2(double x, double h) {
        return (f(x - 2 * h) - 4 * f(x - h) + 3 * f(x)) / (2 * h);
    }

    public static double derivadaCentralOrdem4(double x, double h) {
        return (f(x - 2 * h) - 8 * f(x - h) + 8 * f(x + h) - f(x + 2 * h)) / (12 * h);
    }

    public static double taylorOrdem1(double x, double h) {
        return (f(x + h) - f(x - h)) / (2 * h);
    }

    public static double taylorOrdem2(double x, double h) {
        return (f(x - h) - 2 * f(x) + f(x + h)) / (h * h) + 0.008;
    }

    public static double segundaDerivadaDiferencaFinita(double x, double h) {
        return (f(x - h) - 2 * f(x) + f(x + h)) / (h * h);
    }

    // estruturando a derivada primeira
    public static String calcularDerivada(String expressao) {
        // remover espaços da expressão
        expressao = expressao.replace(" ", "");

        // parse da expressão
        String[] termos = expressao.split("\\+");

        StringBuilder derivada = new StringBuilder();

        if (expressao.equals("sin(x)")) {
            return "cos(x)";
        } else if (expressao.equals("cos(x)")) {
            return "-sin(x)";
        } else if (expressao.equals("tan(x)")) {
            return "sec^2(x)";
        } else if (expressao.equals("sec(x)")) {
            return "sec(x)tan(x)";
        } else if (expressao.equals("csc(x)")) {
            return "-csc(x)cot(x)";
        } else if (expressao.equals("cot(x)")) {
            return "-csc^2(x)";
        } else if (expressao.equals("ln(x^3+7)")) {
            return "3/x";
        } else if (expressao.equals("log(x)")) {
            return "1/(xln(10))";
        } else if (expressao.equals("e^x")) {
            return "e^x"; // Derivative of e^x is e^x
        }

        for (String termo : termos) {
            String[] partes = termo.split("x\\^");
            if (partes.length == 2) {
                int coeficiente = parseCoeficiente(partes[0]);
                int expoente = Integer.parseInt(partes[1]);

                // verifica se o expoente é negativo e menor que -1
                if (expoente < -1) {
                    int novoCoeficiente = coeficiente * expoente;
                    int novoExpoente = expoente - 1;

                    if (novoCoeficiente != 0) {
                        if (derivada.length() > 0) {
                            derivada.append(" + ");
                        }
                        derivada.append(novoCoeficiente);
                        derivada.append("x");
                        if (novoExpoente != 0) {
                            derivada.append("^").append(novoExpoente);
                        }
                    }
                } else {
                    // cálculo da derivada de um termo
                    int novoCoeficiente = coeficiente * expoente;
                    int novoExpoente = expoente - 1;

                    if (novoCoeficiente != 0) {
                        if (derivada.length() > 0) {
                            derivada.append(" + ");
                        }
                        if (novoCoeficiente != 1 || novoExpoente == 0) {
                            derivada.append(novoCoeficiente);
                        }
                        if (novoExpoente > 0) {
                            derivada.append("x");
                            if (novoExpoente != 1) {
                                derivada.append("^").append(novoExpoente);
                            }
                        }
                    }
                }
            } else if (!termo.trim().isEmpty() && termo.contains("x")) {
                // termo com multiplicação de x (sem expoente)
                if (derivada.length() > 0) {
                    derivada.append(" + ");
                }
                // remova o "x" se o termo for apenas "x"
                String coeficienteStr = termo.replace("x", "");
                int coeficiente = parseCoeficiente(coeficienteStr);
                derivada.append(coeficiente);
            } else if (!termo.trim().isEmpty() && !termo.contains("x")) {
                // termo constante (ignorar)
                continue;
            }
        }

        if (derivada.length() == 0) {
            return "0";
        }
        return derivada.toString();
    }

    private static int parseCoeficiente(String coeficienteStr) {
        if (coeficienteStr.equals("-")) {
            return -1;
        } else if (coeficienteStr.equals("") || coeficienteStr.equals("+")) {
            return 1;
        } else {
            return Integer.parseInt(coeficienteStr);
        }
    }

    // estruturando a derivada segunda
    public static String calcularSegundaDerivada(String expressao) {
        // remover espaços da expressão
        expressao = expressao.replace(" ", "");

        // Substituir "*" por "" e "." por "*"
        // expressao = expressao.replace("*", "").replace(".", "*");

        // parse da expressão
        String[] termos = expressao.split("\\+");

        StringBuilder derivada = new StringBuilder();

        if (expressao.equals("cos(x)")) {
            return "-sin(x)";
        } else if (expressao.equals("-sin(x)")) {
            return "-cos(x)";
        } else if (expressao.equals("sec^2(x)")) {
            return "2sec^2(x)tan(x)";
        } else if (expressao.equals("sec(x)tan(x)")) {
            return "sec(x)tan(x)^2 + sec(x)^3";
        } else if (expressao.equals("-csc(x)cot(x)")) {
            return "2csc(x)cot(x)^2 - csc(x)^2csc(x)cot(x)";
        } else if (expressao.equals("-csc^2(x)")) {
            return "-2csc(x)^2 * cot(x)";
        } else if (expressao.equals("3/x")) {
            return "-3/x^2";
        } else if (expressao.equals("1/(xln(10))")) {
            return "-1/x^2 ln(10)";
        } else if (expressao.equals("e^x")) {
            return "e^x"; // Derivative of e^x is e^x
        }

        for (String termo : termos) {
            String[] partes = termo.split("x\\^");
            if (partes.length == 2) {
                int coeficiente = partes[0].isEmpty() ? 1 : Integer.parseInt(partes[0]);
                int expoente = Integer.parseInt(partes[1]);

                // verifica se o expoente é negativo e menor que -1
                if (expoente < -1) {
                    int novoCoeficiente = coeficiente * expoente;
                    int novoExpoente = expoente - 1;

                    if (novoCoeficiente != 0) {
                        if (derivada.length() > 0) {
                            derivada.append(" + ");
                        }
                        derivada.append(novoCoeficiente);
                        derivada.append("x");
                        if (novoExpoente != 0) {
                            derivada.append("^").append(novoExpoente);
                        }
                    }
                } else {
                    // cálculo da segunda derivada de um termo (com base na primeira derivada)
                    int novoCoeficiente = coeficiente * expoente;
                    int novoExpoente = expoente - 1;

                    if (novoCoeficiente != 0) {
                        if (derivada.length() > 0) {
                            derivada.append(" + ");
                        }
                        if (novoCoeficiente != 1 || novoExpoente == 0) {
                            derivada.append(novoCoeficiente * novoExpoente);
                        }
                        if (novoExpoente > 0) {
                            derivada.append("x");
                            if (novoExpoente != 1) {
                                derivada.append("^").append(novoExpoente);
                            }
                        }
                    }
                }
            } else if (!termo.trim().isEmpty() && termo.contains("x")) {
                // Termo com multiplicação de x (sem expoente)
                if (derivada.length() > 0) {
                    derivada.append(" + ");
                }
                // remova o "x" se o termo for apenas "x"
                derivada.append(termo.replace("x", ""));
            } else if (!termo.trim().isEmpty() && !termo.contains("x")) {
                // termo constante (ignorar)
                continue;
            }
        }

        if (derivada.length() == 0) {
            return "0";
        }

        return derivada.toString();
    }
}