package com.portoflio.api;

import com.portoflio.api.dao.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class ApiApplicationTests {
	@Autowired
	MockMvc mvc;

	@Autowired
	IllustrationRepository illustrationRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	OrderRepository orderRepository;

	String authenticate(String user, String password) throws Exception {
		String userBody = "{\"email\": \""+ user +"\", \"password\": \"" + password + "\"}";
		//String userBody = "{\"email\": "+ user +", \"password\": " + password + "}";
		MvcResult result = mvc.perform(post("/token")
				.content(userBody)
				.contentType(MediaType.APPLICATION_JSON))
						//.with(httpBasic(user, password)))
				.andExpect(status().isOk())
				.andReturn();
		return result.getResponse().getContentAsString();
	}
	void createIllustration() throws Exception {
		MockMultipartFile image = new MockMultipartFile("image", "file.png", "application/octet-stream", new byte[0]);
		MockPart name = new MockPart("name", "Illustration".getBytes(UTF_8));
		MockPart description = new MockPart("description", "IllustrationDescription".getBytes(UTF_8));
		MockPart visibility = new MockPart("visibility", "false".getBytes(UTF_8));
		mvc.perform(multipart("/illustrations/")
				.file(image)
				.part(name)
				.part(description)
				.part(visibility)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE));
	}
	void createProduct() throws Exception {
		MockMultipartFile image = new MockMultipartFile("images", "file.png", "application/octet-stream", new byte[0]);
		MockMultipartFile image2 = new MockMultipartFile("images", "file.png", "application/octet-stream", new byte[0]);

		MockPart name = new MockPart("name", "Item".getBytes(UTF_8));
		MockPart description = new MockPart("description", "ItemDescription".getBytes(UTF_8));
		MockPart visibility = new MockPart("visibility", "false".getBytes(UTF_8));
		MockPart thumbnail_index = new MockPart("thumbnail_index", "0".getBytes(UTF_8));

		mvc.perform(multipart("/products/")
				.file(image)
				.file(image2)
				.part(name)
				.part(description)
				.part(visibility)
				.part(thumbnail_index)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE));
	}

	@Test
	void contextLoads() {
	}

	@Test
	void securityTest() throws Exception {
		// no auth

		mvc.perform(post("/illustrations/")
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
				.andExpect(status().is4xxClientError());


		String tokenUser = authenticate("user", "password");
		// user is not ADMIN
		mvc.perform(post("/illustrations/")
						.contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
						.header("Authorization", "Bearer " + tokenUser))
						.andExpect(status().is4xxClientError());
	}

	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void illustrationCreationTest() throws Exception {
		long illustrationsCount = illustrationRepository.count();
		MockMultipartFile image = new MockMultipartFile("file", "file.png", "application/octet-stream", new byte[0]);
		MockPart name = new MockPart("name", "Illustration".getBytes(UTF_8));
		MockPart description = new MockPart("description", "IllustrationDescription".getBytes(UTF_8));
		MockPart visibility = new MockPart("visibility", "false".getBytes(UTF_8));

		//String testIllustration = "{\"name\": \"Illustration\", \"description\": \"IllustrationDescription\", \"visibility\": false}";
		// method post on url /prisoners/
		mvc.perform(multipart("/illustrations/")
				.file(image)
				.part(name)
				.part(description)
				.part(visibility)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
				.andExpect(status().isCreated()) // test result
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
				/*
				.andExpect(jsonPath("$.name").value("Illustration"))
				.andExpect(jsonPath("$.description").value("IllustrationDescription"))
				.andExpect(jsonPath("$.visibility").value(false));
				 */
		// test number of prisoners is correct (+1)
		assert illustrationRepository.count() == illustrationsCount + 1;
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void illustrationListTest() throws Exception {
		createIllustration();
		mvc.perform(get("/illustrations/").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name").value("Illustration"))
				.andExpect(jsonPath("$[0].description").value("IllustrationDescription"))
				.andExpect(jsonPath("$[0].visibility").value(false))
				.andExpect(jsonPath("$[0].image_id").isNotEmpty());
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void illustrationFindByIdTest() throws Exception {
		createIllustration();
		mvc.perform(get("/illustrations/1/")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value("Illustration"))
				.andExpect(jsonPath("$.description").value("IllustrationDescription"))
				.andExpect(jsonPath("$.visibility").value(false));
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void illustrationUpdateTest() throws Exception {
		createIllustration();
		String testIllustration = "{\"name\": \"NuevoNombre\"}";
		mvc.perform(patch("/illustrations/1/")
						.content(testIllustration)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // test result
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value("NuevoNombre"));
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void deleteTest() throws Exception {
		createIllustration();
		long illustrationsCount = illustrationRepository.count();
		mvc.perform(delete("/illustrations/1/")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		assert illustrationRepository.count() == illustrationsCount - 1;
	}

	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void createProductTest() throws Exception {
		MockMultipartFile image = new MockMultipartFile("images", "file.png", "application/octet-stream", new byte[0]);
		MockMultipartFile image2 = new MockMultipartFile("images", "file.png", "application/octet-stream", new byte[0]);

		MockPart name = new MockPart("name", "Item".getBytes(UTF_8));
		MockPart description = new MockPart("description", "ItemDescription".getBytes(UTF_8));
		MockPart visibility = new MockPart("visibility", "false".getBytes(UTF_8));
		MockPart thumbnail_index = new MockPart("thumbnail_index", "0".getBytes(UTF_8));

		mvc.perform(multipart("/products/")
				.file(image)
				.file(image2)
				.part(name)
				.part(description)
				.part(visibility)
				.part(thumbnail_index)
				.contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
				.andExpect(status().isCreated())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value("Item"))
				.andExpect(jsonPath("$.description").value("ItemDescription"))
				.andExpect(jsonPath("$.visibility").value(false))
				.andExpect(jsonPath("$.thumbnail_image_id").isNotEmpty());
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void productListTest() throws Exception {
		createProduct();
		mvc.perform(get("/products/").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].name").value("Item"))
				.andExpect(jsonPath("$[0].description").value("ItemDescription"))
				.andExpect(jsonPath("$[0].visibility").value(false))
				.andExpect(jsonPath("$[0].image_ids").isNotEmpty());
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void productFindByIdTest() throws Exception {
		createProduct();
		mvc.perform(get("/products/1/")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value("Item"))
				.andExpect(jsonPath("$.description").value("ItemDescription"))
				.andExpect(jsonPath("$.visibility").value(false))
				.andExpect(jsonPath("$.image_ids").isNotEmpty());
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void productUpdateTest() throws Exception {
		createProduct();
		String testItem = "{\"name\": \"NuevoNombre\"}";
		mvc.perform(patch("/products/1/")
						.content(testItem)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.name").value("NuevoNombre"));
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void deleteProductTest() throws Exception {
		createProduct();
		long itemsCount = productRepository.count();
		mvc.perform(delete("/products/1/")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		assert productRepository.count() == itemsCount - 1;
	}

	void createOrder() throws Exception {
		String order = "{\"email\": \"email\", \"cartItems\": [{\"product_id\": 1, \"amount\": 1}]}";
		mvc.perform(post("/order/guest")
				.content(order)
				.contentType(MediaType.APPLICATION_JSON));
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void createOrderTest() throws Exception {
		long count =orderRepository.count();
		createProduct();
		String order = "{\"email\": \"email\", \"cartItems\": [{\"product_id\": 1, \"amount\": 1}]}";
		mvc.perform(post("/order/guest")
			.content(order)
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.email").value("email"));
		assert orderRepository.count() == count + 1;
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void indexOrderTest() throws Exception {
		createProduct();
		createOrder();
		mvc.perform(get("/order/")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].email").value("email"));
	}
	@Test
	@WithMockUser(authorities="SCOPE_ADMIN")
	void updateOrderTest() throws Exception {
		String orderUpdate = "{\"email\": \"nuevoEmail\"}";
		createProduct();
		createOrder();
		mvc.perform(patch("/order/1")
						.content(orderUpdate)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		mvc.perform(get("/order/1")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.email").value("nuevoEmail"));
	}
}


