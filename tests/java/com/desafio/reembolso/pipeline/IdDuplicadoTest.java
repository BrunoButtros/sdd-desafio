package com.desafio.reembolso.pipeline;

import com.desafio.reembolso.modelo.CampoCanonico;
import com.desafio.reembolso.modelo.ItemValidado;
import com.desafio.reembolso.modelo.ItemValidado.Motivo;
import com.desafio.reembolso.modelo.MotivoCodigo;
import com.desafio.reembolso.modelo.RegraNegocio;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Cobre RN-003 / CA-019 (spec 4.2, 8.4.6-8.4.7): todas as ocorrências de um
 * {@code despesa.id} estruturalmente válido e repetido recebem
 * {@code ID_DUPLICADO} — sem "primeira ocorrência preservada" — e um
 * {@code id} estruturalmente inválido (nulo) não participa da contagem nem
 * contamina outros itens com {@code id} nulo entre si.
 */
@DisplayName("Unicidade de despesa.id — RN-003 / CA-019")
class IdDuplicadoTest {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS);

    private static JsonNode despesas(String json) {
        try {
            return MAPPER.readTree(json).get("despesas");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static List<ItemValidado> validar(String json) {
        return ValidadorItem.validarLista(despesas(json));
    }

    private static Motivo idDuplicado() {
        return new Motivo(MotivoCodigo.ID_DUPLICADO, RegraNegocio.RN_003, CampoCanonico.ID);
    }

    private static boolean contemIdDuplicado(ItemValidado item) {
        return item.getMotivos().contains(idDuplicado());
    }

    @Test
    @DisplayName("CA-019 — três itens com o mesmo id válido recebem ID_DUPLICADO, todos, sem primeira ocorrência preservada")
    void tresOcorrenciasDoMesmoId_todasRecebemIdDuplicado() {
        List<ItemValidado> itens = validar("""
                {
                  "despesas": [
                    { "id": "d-100", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-100", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true },
                    { "id": "d-100", "data": "2026-07-05", "categoria": "alimentacao", "descricao": "C", "fornecedor": "F3", "valor": 30.00, "tem_nota_fiscal": true }
                  ]
                }
                """);

        List<ItemValidado> resultado = DetectorIdDuplicado.detectar(itens);

        assertEquals(3, resultado.size());
        for (ItemValidado item : resultado) {
            assertTrue(contemIdDuplicado(item), "toda ocorrência de d-100 deve receber ID_DUPLICADO");
            Motivo motivo = item.getMotivos().get(item.getMotivos().size() - 1);
            assertEquals(MotivoCodigo.ID_DUPLICADO, motivo.codigo());
            assertEquals(RegraNegocio.RN_003, motivo.regra());
            assertEquals(CampoCanonico.ID, motivo.campo());
        }
    }

    @Test
    @DisplayName("RN-003 / 8.4.6 — id estruturalmente inválido (texto vazio) não participa da verificação e não recebe ID_DUPLICADO")
    void idEstruturalmenteInvalido_naoParticipaDaVerificacao() {
        List<ItemValidado> itens = validar("""
                {
                  "despesas": [
                    { "id": "", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true }
                  ]
                }
                """);

        assertEquals(1, itens.size());
        assertEquals(null, itens.get(0).getId(), "id vazio não é preservado por RN-002");

        List<ItemValidado> resultado = DetectorIdDuplicado.detectar(itens);

        assertEquals(1, resultado.size());
        assertFalse(contemIdDuplicado(resultado.get(0)));
    }

    @Test
    @DisplayName("8.4.6 — dois itens com id nulo não são tratados como duplicados entre si")
    void doisItensComIdNulo_naoSaoDuplicadosEntreSi() {
        List<ItemValidado> itens = validar("""
                {
                  "despesas": [
                    { "id": "", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true },
                    { "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """);

        List<ItemValidado> resultado = DetectorIdDuplicado.detectar(itens);

        assertEquals(2, resultado.size());
        assertFalse(contemIdDuplicado(resultado.get(0)));
        assertFalse(contemIdDuplicado(resultado.get(1)));
    }

    @Test
    @DisplayName("RN-003 — ids válidos distintos permanecem sem ID_DUPLICADO")
    void idsValidosDistintos_semIdDuplicado() {
        List<ItemValidado> itens = validar("""
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-002", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true }
                  ]
                }
                """);

        List<ItemValidado> resultado = DetectorIdDuplicado.detectar(itens);

        assertEquals(2, resultado.size());
        assertFalse(contemIdDuplicado(resultado.get(0)));
        assertFalse(contemIdDuplicado(resultado.get(1)));
    }

    @Test
    @DisplayName("Cenário misto — só o grupo repetido recebe ID_DUPLICADO; ordem, indiceEntrada e motivos estruturais anteriores são preservados")
    void cenarioMisto_somenteGrupoRepetidoRecebeMotivo() {
        List<ItemValidado> itens = validar("""
                {
                  "despesas": [
                    { "id": "d-100", "data": "31/07/2026", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true },
                    { "id": "d-200", "data": "2026-07-04", "categoria": "alimentacao", "descricao": "B", "fornecedor": "F2", "valor": 20.00, "tem_nota_fiscal": true },
                    { "id": "d-100", "data": "2026-07-05", "categoria": "alimentacao", "descricao": "C", "fornecedor": "F3", "valor": 30.00, "tem_nota_fiscal": true }
                  ]
                }
                """);

        List<ItemValidado> resultado = DetectorIdDuplicado.detectar(itens);

        assertEquals(3, resultado.size());

        ItemValidado primeiro = resultado.get(0);
        assertEquals(1, primeiro.getIndiceEntrada());
        assertEquals("d-100", primeiro.getId());
        assertEquals(2, primeiro.getMotivos().size(), "motivo estrutural de data + ID_DUPLICADO");
        assertEquals(MotivoCodigo.CAMPO_FORMATO_INVALIDO, primeiro.getMotivos().get(0).codigo(),
                "motivo estrutural anterior (data malformada) deve continuar presente");
        assertTrue(contemIdDuplicado(primeiro));

        ItemValidado segundo = resultado.get(1);
        assertEquals(2, segundo.getIndiceEntrada());
        assertEquals("d-200", segundo.getId());
        assertFalse(contemIdDuplicado(segundo));

        ItemValidado terceiro = resultado.get(2);
        assertEquals(3, terceiro.getIndiceEntrada());
        assertEquals("d-100", terceiro.getId());
        assertEquals(1, terceiro.getMotivos().size(), "sem motivo estrutural anterior, só ID_DUPLICADO");
        assertTrue(contemIdDuplicado(terceiro));
    }

    @Test
    @DisplayName("A lista retornada é não modificável")
    void listaRetornada_naoModificavel() {
        List<ItemValidado> itens = validar("""
                {
                  "despesas": [
                    { "id": "d-001", "data": "2026-07-03", "categoria": "alimentacao", "descricao": "A", "fornecedor": "F1", "valor": 10.00, "tem_nota_fiscal": true }
                  ]
                }
                """);

        List<ItemValidado> resultado = DetectorIdDuplicado.detectar(itens);

        assertThrows(UnsupportedOperationException.class, () -> resultado.add(resultado.get(0)));
    }
}
