package br.com.concrete.canarinho.watcher;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.concrete.canarinho.validator.Validador;
import br.com.concrete.canarinho.validator.ValidadorBoleto;
import br.com.concrete.canarinho.watcher.evento.EventoDeValidacao;
import br.com.concrete.canarinho.watcher.evento.EventoDeValidacaoDeBoleto;

/**
 * {@link android.text.TextWatcher} responsável por formatar e validar um {@link
 * android.widget.EditText} para boletos. Para usar este componente basta criar uma instância e
 * chamar {@link android.widget.EditText#addTextChangedListener(android.text.TextWatcher)}.
 */
public final class BoletoBancarioTextWatcher extends BaseCanarinhoTextWatcher {

    private static final char[] BOLETO_NORMAL = "#####.##### #####.###### #####.###### # ##############".toCharArray();
    private static final char[] BOLETO_TRIBUTO = "############ ############ ############ ############".toCharArray();
    private static final char[] BOLETO_NORMAL_MULTI_LINHA = "#####.#####\n#####.######\n#####.###### #\n##############".toCharArray();
    private static final char[] BOLETO_TRIBUTO_MULTI_LINHA = "############\n############\n############\n############".toCharArray();
    private static final InputFilter[] FILTRO_TRIBUTO = new InputFilter[]{
            new InputFilter.LengthFilter(BOLETO_TRIBUTO.length)};
    private static final InputFilter[] FILTRO_NORMAL = new InputFilter[]{
            new InputFilter.LengthFilter(BOLETO_NORMAL.length)};
    private static final InputFilter[] FILTRO_TRIBUTO_MULTI_LINHA = new InputFilter[]{
            new InputFilter.LengthFilter(BOLETO_TRIBUTO_MULTI_LINHA.length)};
    private static final InputFilter[] FILTRO_NORMAL_MULTI_LINHA = new InputFilter[]{
            new InputFilter.LengthFilter(BOLETO_NORMAL_MULTI_LINHA.length)};
    private Integer corMarcacao;
    private  boolean ehMultiLinha = false;
    private boolean ehTributo;

    private final Validador validador = ValidadorBoleto.getInstance();
    private final Validador.ResultadoParcial resultadoParcial = new Validador.ResultadoParcial();

    /**
     * TODO Javadoc incompleto
     * Ao adicionar o Watcher não esquecer de colocar
     * android:digits="0123456789.\n " no EditText
     * @param callbackErros a descrever
     */
    public BoletoBancarioTextWatcher(EventoDeValidacao callbackErros, Integer corMarcacao,
                                     boolean ehMultiLinha) {
        this.corMarcacao = corMarcacao;
        this.ehMultiLinha = ehMultiLinha;
        setEventoDeValidacao(callbackErros);
    }

    public BoletoBancarioTextWatcher(EventoDeValidacao callbackErros) {
        setEventoDeValidacao(callbackErros);
    }

    @Override
    public final void afterTextChanged(Editable s) {

        // retorna se a String é menor que o mínimo de caracteres
        // para haver uma formatação ou se a mudança foi disparada
        // pelo método atualizaTexto
        if (isMudancaInterna()) {
            return;
        }

        // Trata o caso em que tudo é apagado em lote
        if (s.length() < 3) {
            resultadoParcial.mensagem(null).parcialmenteValido(false).totalmenteValido(false);
            if (getEventoDeValidacao() != null) {
                getEventoDeValidacao().parcialmenteValido("");
            }
        }

        if (s.length() == 0) {
            verificaFiltro(s, false);
            return;
        }

        this.ehTributo = ehTributo(s);
        final char[] mascara;
        if (ehMultiLinha) {
            mascara = ehTributo ? BOLETO_TRIBUTO_MULTI_LINHA : BOLETO_NORMAL_MULTI_LINHA;
        } else {
            mascara = ehTributo ? BOLETO_TRIBUTO : BOLETO_NORMAL;
        }
        verificaFiltro(s, ehTributo);

        // Trata deleção e adição de forma diferente (só formata em adições)
        final StringBuilder builder = trataAdicaoRemocaoDeCaracter(s, mascara);
        atualizaTextoBoleto(validador, resultadoParcial, s, builder);
    }

    public Validador.ResultadoParcial getResultadoParcial() {
        return resultadoParcial;
    }

    @Override
    protected void efetuaValidacao(Validador validador, Validador.ResultadoParcial resultadoParcial,
                                   Editable s) {

        validador.ehValido(s, resultadoParcial);

        final EventoDeValidacao callbackErros = getEventoDeValidacao();

        if (callbackErros == null) {
            return;
        }

        final String valorAtual = s.toString();
        if (!resultadoParcial.isParcialmenteValido()) {

            final String mensagem = resultadoParcial.getMensagem();
            callbackErros.invalido(valorAtual, mensagem);

            if (callbackErros instanceof EventoDeValidacaoDeBoleto) {

                final List<Integer> blocos = new ArrayList<>();

                if (mensagem.contains("Primeiro")) {
                    blocos.add(1);
                    if (ehTributo) {
                        destacarBloco(s, ValidadorBoleto.PRIMEIRO_BLOCO_INICIO_MASCARA_TRIBUTO, ValidadorBoleto.PRIMEIRO_BLOCO_FIM_MASCARA_TRIBUTO);
                    } else {
                        destacarBloco(s, ValidadorBoleto.PRIMEIRO_BLOCO_INICIO_MASCARA, ValidadorBoleto.PRIMEIRO_BLOCO_FIM_MASCARA);
                    }
                }
                if (mensagem.contains("Segundo")) {
                    blocos.add(2);
                    if (ehTributo) {
                        destacarBloco(s, ValidadorBoleto.SEGUNDO_BLOCO_INICIO_MASCARA_TRIBUTO, ValidadorBoleto.SEGUNDO_BLOCO_FIM_MASCARA_TRIBUTO);
                    } else {
                        destacarBloco(s, ValidadorBoleto.SEGUNDO_BLOCO_INICIO_MASCARA, ValidadorBoleto.SEGUNDO_BLOCO_FIM_MASCARA);
                    }
                }
                if (mensagem.contains("Terceiro")) {
                    blocos.add(3);
                    if (ehTributo) {
                        destacarBloco(s, ValidadorBoleto.TERCEIRO_BLOCO_INICIO_MASCARA_TRIBUTO, ValidadorBoleto.TERCEIRO_BLOCO_FIM_MASCARA_TRIBUTO);
                    } else {
                        destacarBloco(s, ValidadorBoleto.TERCEIRO_BLOCO_INICIO_MASCARA, ValidadorBoleto.TERCEIRO_BLOCO_FIM_MASCARA);
                    }
                }
                if (mensagem.contains("Quarto")) {
                    blocos.add(4);
                    if (ehTributo) destacarBloco(s, ValidadorBoleto.QUARTO_BLOCO_INICIO_MASCARA, ValidadorBoleto.QUARTO_BLOCO_FIM_MASCARA);
                }

                ((EventoDeValidacaoDeBoleto) callbackErros).invalido(valorAtual, blocos);
            }

        } else if (!resultadoParcial.isValido()) {
            removeSpans(s);
            callbackErros.parcialmenteValido(valorAtual);
        } else {
            callbackErros.totalmenteValido(valorAtual);
        }
    }

    private void destacarBloco(final Editable s, final int inicio, final int fim) {
        if (corMarcacao != null)
        s.setSpan(new ForegroundColorSpan(corMarcacao), inicio, fim, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void removeSpans(final Editable s) {
        ForegroundColorSpan[] colorSpans = s.getSpans(0, s.length(), ForegroundColorSpan.class);
        for (ForegroundColorSpan colorSpan : colorSpans) {
            s.removeSpan(colorSpan);
        }
    }

    private void verificaFiltro(final Editable s, final boolean tributo) {
        // Filtro de tamanho
        if (ehMultiLinha && tributo && !Arrays.equals(s.getFilters(), FILTRO_TRIBUTO_MULTI_LINHA)){
            s.setFilters(FILTRO_TRIBUTO_MULTI_LINHA);
        } else if (ehMultiLinha && !tributo && !Arrays.equals(s.getFilters(), FILTRO_NORMAL_MULTI_LINHA)) {
            s.setFilters(FILTRO_NORMAL_MULTI_LINHA);
        } else if (!ehMultiLinha && tributo && !Arrays.equals(s.getFilters(), FILTRO_TRIBUTO)) {
            s.setFilters(FILTRO_TRIBUTO);
        } else if (!ehMultiLinha && tributo && !Arrays.equals(s.getFilters(), FILTRO_NORMAL)) {
            s.setFilters(FILTRO_NORMAL);
        }
    }

    // Boletos iniciados com 8 são tributos ou de concessionárias
    private boolean ehTributo(Editable e) {
        return e.charAt(0) == '8';
    }
}
