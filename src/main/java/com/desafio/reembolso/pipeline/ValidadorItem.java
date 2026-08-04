package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.Envelope;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Valida cada elemento de {@code despesas} contra o contrato estrutural do
 * item (spec 4.2, RN-002), atribuindo {@code indiceEntrada} (base 1, antes
 * de qualquer validação) e classificando cada defeito estrutural na ordem
 * canônica de contrato: id, data, categoria, descricao, fornecedor, valor,
 * tem_nota_fiscal. Um item inválido não interrompe o processamento dos
 * demais (DT-005). Cada campo tipado só é preenchido quando passa
 * integralmente na validação estrutural — sem coerção via acessores
 * permissivos do Jackson. Não realiza normalização, deduplicação ou
 * qualquer regra de negócio além de RN-002 — isso pertence às tasks
 * seguintes.
 */
public final class ValidadorItem {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter
            .ofPattern("uuuu-MM-dd")
            .withResolverStyle(ResolverStyle.STRICT);
    private static final Pattern PADRAO_DATA = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");

    private ValidadorItem() {
    }

    public static List<ItemValidado> validarLista(Envelope envelope) {
        return validarLista(envelope.getDespesas());
    }

    public static List<ItemValidado> validarLista(JsonNode despesas) {
        List<ItemValidado> resultado = new ArrayList<>();
        int indiceEntrada = 1;
        for (JsonNode elemento : despesas) {
            resultado.add(validarItem(indiceEntrada, elemento));
            indiceEntrada++;
        }
        return List.copyOf(resultado);
    }

    private static ItemValidado validarItem(int indiceEntrada, JsonNode elemento) {
        if (elemento == null || elemento.getNodeType() != JsonNodeType.OBJECT) {
            Motivo motivo = new Motivo(MotivoCodigo.ITEM_TIPO_INVALIDO, RegraNegocio.RN_002, null);
            return new ItemValidado(indiceEntrada, null, null, null, null, null, null, null, null,
                    List.of(motivo));
        }

        List<Motivo> motivos = new ArrayList<>();

        String id = validarTexto(elemento, "id", CampoCanonico.ID, false, motivos);
        LocalDate data = validarData(elemento, motivos);
        String categoria = validarTexto(elemento, "categoria", CampoCanonico.CATEGORIA, false, motivos);
        String descricao = validarTexto(elemento, "descricao", CampoCanonico.DESCRICAO, true, motivos);
        String fornecedor = validarTexto(elemento, "fornecedor", CampoCanonico.FORNECEDOR, true, motivos);
        BigDecimal valor = validarValor(elemento, motivos);
        Boolean temNotaFiscal = validarBooleano(elemento, motivos);

        JsonNode valorInformado = extrairValorInformado(elemento);

        return new ItemValidado(indiceEntrada, id, data, categoria, descricao, fornecedor, valor,
                temNotaFiscal, valorInformado, motivos);
    }

    private static String validarTexto(JsonNode elemento, String chave, CampoCanonico campo,
                                        boolean permiteVazio, List<Motivo> motivos) {
        JsonNode valor = elemento.get(chave);
        if (valor == null || valor.isNull()) {
            motivos.add(campoAusente(campo));
            return null;
        }
        if (valor.getNodeType() != JsonNodeType.STRING) {
            motivos.add(campoTipoInvalido(campo));
            return null;
        }
        String texto = valor.asText();
        if (!permiteVazio && texto.isEmpty()) {
            motivos.add(campoFormatoInvalido(campo));
            return null;
        }
        return texto;
    }

    private static LocalDate validarData(JsonNode elemento, List<Motivo> motivos) {
        JsonNode valor = elemento.get("data");
        if (valor == null || valor.isNull()) {
            motivos.add(campoAusente(CampoCanonico.DATA));
            return null;
        }
        if (valor.getNodeType() != JsonNodeType.STRING) {
            motivos.add(campoTipoInvalido(CampoCanonico.DATA));
            return null;
        }
        String texto = valor.asText();
        if (!PADRAO_DATA.matcher(texto).matches()) {
            motivos.add(campoFormatoInvalido(CampoCanonico.DATA));
            return null;
        }
        try {
            return LocalDate.parse(texto, FORMATO_DATA);
        } catch (DateTimeParseException e) {
            motivos.add(campoFormatoInvalido(CampoCanonico.DATA));
            return null;
        }
    }

    private static BigDecimal validarValor(JsonNode elemento, List<Motivo> motivos) {
        JsonNode valor = elemento.get("valor");
        if (valor == null || valor.isNull()) {
            motivos.add(campoAusente(CampoCanonico.VALOR));
            return null;
        }
        if (valor.getNodeType() != JsonNodeType.NUMBER) {
            motivos.add(campoTipoInvalido(CampoCanonico.VALOR));
            return null;
        }
        // Zero e negativos são estruturalmente válidos (4.2) — sem checagem de formato aqui.
        return valor.decimalValue();
    }

    private static Boolean validarBooleano(JsonNode elemento, List<Motivo> motivos) {
        JsonNode valor = elemento.get("tem_nota_fiscal");
        if (valor == null || valor.isNull()) {
            motivos.add(campoAusente(CampoCanonico.TEM_NOTA_FISCAL));
            return null;
        }
        if (valor.getNodeType() != JsonNodeType.BOOLEAN) {
            motivos.add(campoTipoInvalido(CampoCanonico.TEM_NOTA_FISCAL));
            return null;
        }
        return valor.booleanValue();
    }

    private static JsonNode extrairValorInformado(JsonNode elemento) {
        JsonNode valor = elemento.get("valor");
        if (valor == null || valor.isNull()) {
            return null;
        }
        return valor;
    }

    private static Motivo campoAusente(CampoCanonico campo) {
        return new Motivo(MotivoCodigo.CAMPO_AUSENTE, RegraNegocio.RN_002, campo);
    }

    private static Motivo campoTipoInvalido(CampoCanonico campo) {
        return new Motivo(MotivoCodigo.CAMPO_TIPO_INVALIDO, RegraNegocio.RN_002, campo);
    }

    private static Motivo campoFormatoInvalido(CampoCanonico campo) {
        return new Motivo(MotivoCodigo.CAMPO_FORMATO_INVALIDO, RegraNegocio.RN_002, campo);
    }
}
