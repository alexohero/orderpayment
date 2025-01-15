package com.mx.bbase.orderpayment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mx.bbase.orderpayment.configs.KafkaTestConfig;
import com.mx.bbase.orderpayment.model.dto.OrderDTO;
import com.mx.bbase.orderpayment.model.request.OrderRequest;
import com.mx.bbase.orderpayment.persistence.stores.OrderStore;
import org.apache.kafka.clients.producer.MockProducer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = {OrderpaymentApplication.class, KafkaTestConfig.class})
@AutoConfigureMockMvc
class OrderpaymentApplicationTests {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private OrderStore orderStore;
	@Autowired
	private KafkaTemplate<String, OrderDTO> kafkaTemplate;
	@Autowired
	private MockProducer<String, OrderDTO> mockProducer;
	private OrderRequest orderRequest;

	@BeforeEach
	void setUp() {
		orderRequest = new OrderRequest();
		orderRequest.setConcepto("Test Unit");
		orderRequest.setCantidadProductos(10);
		orderRequest.setNombreOrdenante("Ordenante");
		orderRequest.setNombreBeneficiario("Beneficiario");
		orderRequest.setMonto(BigDecimal.valueOf(100.50));
		orderRequest.setEstado("");

		orderStore.deleteAll();

	}

	@Test
	void testCreate() throws Exception {
		mockMvc.perform(post("/order")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.estado").value("CAPTURADO"));
	}

	@Test
	void testCreateLiquidado() throws Exception {
		orderRequest.setEstado("liquidado");
		mockMvc.perform(post("/order")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").isNotEmpty())
				.andExpect(jsonPath("$.estado").value("LIQUIDADO"));
	}

	@Test
	void testFailCreateNombre() throws Exception {
		orderRequest.setNombreOrdenante("#!%");
		mockMvc.perform(post("/order")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testFailCreateConcepto() throws Exception {
		orderRequest.setConcepto("");
		mockMvc.perform(post("/order")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testFailCreateMonto() throws Exception {
		orderRequest.setMonto(BigDecimal.ZERO);
		mockMvc.perform(post("/order")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testFindOneById() throws Exception {
		var result = mockMvc.perform(post("/order")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isCreated())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();

		var id = objectMapper.readValue(responseBody, OrderDTO.class).getId();

		mockMvc.perform(get("/order/" + id))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.estado").value("CAPTURADO"));
	}

	@Test
	void testFailFindOneById() throws Exception {
		mockMvc.perform(get("/order/1"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testUpdate() throws Exception {
		var result = mockMvc.perform(post("/order")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(orderRequest)))
				.andExpect(status().isCreated())
				.andReturn();

		String responseBody = result.getResponse().getContentAsString();

		var id = objectMapper.readValue(responseBody, OrderDTO.class).getId();

		mockMvc.perform(patch("/order/" + id)
						.param("estado", "LIQUIDADO"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(id))
				.andExpect(jsonPath("$.estado").value("LIQUIDADO"));
	}

	@Test
	void testFailUpdateEstado() throws Exception {
		mockMvc.perform(post("/order")
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(orderRequest)));

		mockMvc.perform(patch("/order/1")
						.param("estado", "DEVUELTO"))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testFailUpdateNotFound() throws Exception {
		mockMvc.perform(patch("/order/50")
						.param("estado", "LIQUIDADO"))
				.andExpect(status().isBadRequest());
	}

}
