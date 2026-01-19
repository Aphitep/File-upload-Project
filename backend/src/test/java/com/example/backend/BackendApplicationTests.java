package com.example.backend;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.controller.TestController;
import com.example.backend.entity.ExelBody;
import com.example.backend.services.FileService;

@WebMvcTest(TestController.class)
class BackendApplicationTests {

	@Autowired
	MockMvc mockMvc;

	@MockitoBean
	FileService fileService;

	@Test
	void testHello() throws Exception {
		mockMvc.perform(get("/hello"))
				.andExpect(status().isOk())
				.andExpect(content().string("Hello User"));
	}

	@Test
	void testUpload() throws Exception {

		MockMultipartFile mockFile = new MockMultipartFile(
				"file",
				"test.xlsx",
				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
				"content".getBytes());

		Mockito.doNothing().when(fileService).uploadFile(any(MultipartFile.class));

		mockMvc.perform(multipart("/upload").file(mockFile))
				.andExpect(status().isCreated());
	}

	@Test
	void testDownload() throws Exception {
		byte[] content = "test content".getBytes();
		ByteArrayResource resource = new ByteArrayResource(content);

		Mockito.when(fileService.download("test.xlsx"))
				.thenReturn(resource);

		mockMvc.perform(get("/download/{filename}", "test.xlsx"))
				.andExpect(status().isOk())
				.andExpect(header().exists(HttpHeaders.CONTENT_DISPOSITION))
				.andExpect(content().bytes(content));
	}

	@Test
	void testRead() throws Exception {
		ExelBody exel = new ExelBody();

		exel.setFilename("test.xlsx");

		when(fileService.readExcel("test.xlsx")).thenReturn(exel);

		mockMvc.perform(get("/read")
				.param("filename", "test.xlsx"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.filename").value("test.xlsx"));
	}

	@Test
	void testWrite() throws Exception {

		String json = """
				{
				  "filename": "test.xlsx",
				  "rows": []
				}
				""";

		mockMvc.perform(post("/write")
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
				.andExpect(status().isOk());

		verify(fileService).writeExcel(any());
	}
}
