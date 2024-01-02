package hexlet.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.AppApplication;
import hexlet.code.model.Task;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.project.util.ModelGenerator;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Set;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = AppApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TaskTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    private ModelGenerator modelGenerator = new ModelGenerator();

    private Task testTask;

    @BeforeEach
    public void beforeEach() {
        var taskStatus = taskStatusRepository.findBySlug("draft").get();
        var label = labelRepository.findByName("feature").get();
        testTask = Instancio.of(modelGenerator.getTaskModel())
                .set(Select.field(Task::getAssignee), null)
                .create();
        testTask.setTaskStatus(taskStatus);
        testTask.setLabels(Set.of(label));
        taskRepository.save(testTask);
    }

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/api/tasks").with(jwt()))
                .andExpect(status().isOk());
    }

    @Test
    public void testShow() throws Exception {
        var request = get("/api/tasks/" + testTask.getId()).with(jwt());
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    public void testCreate() throws Exception {
        var taskStatus = taskStatusRepository.findBySlug("draft").get();
        Set<Long> labels = Set.of();
        var name = "New Task Name";
        var content = "Task Content";
        var data = new HashMap<String, Object>();
        data.put("title", name);
        data.put("content", content);
        data.put("status", taskStatus.getSlug());
        data.put("taskLabelIds", labels);

        var request = post("/api/tasks").with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        var result = mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("id").isPresent(),
                v -> v.node("content").isPresent(),
                v -> v.node("title").isPresent(),
                v -> v.node("status").isEqualTo(data.get("status")));

        var task = taskRepository.findByName(name).get();
        assertNotNull(task);
    }

    @Test
    public void testUpdate() throws Exception {
        var data = new HashMap<String, String>();
        var name = "New Task Name";
        data.put("name", name);

        var request = put("/api/tasks/" + testTask.getId()).with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));
        mockMvc.perform(request)
                .andExpect(status().isOk());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(jwt()))
                .andExpect(status().isNoContent());
    }
}
