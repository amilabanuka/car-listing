package lk.amila.cars.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lk.amila.cars.model.ModelEntity;
import lk.amila.cars.service.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Controller to accept raw JSON for adding models.
 * POST /api/models/add consumes application/json and expects a raw JSON body string.
 */
@RestController
@RequestMapping("/api/models")
@CrossOrigin(origins = "http://localhost:3000")
public class ModelsController {
    private static final Logger logger = LoggerFactory.getLogger(ModelsController.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final ModelService modelService;

    public ModelsController(ModelService modelService) {
        this.modelService = modelService;
    }

    @PostMapping(value = "/add", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonNode> addModel(@RequestBody String json) {
        logger.info("Received addModel request");
        try {
            JsonNode payload = mapper.readTree(json);

            // Extract models -> *.values arrays
            ObjectNode resp = mapper.createObjectNode();
            ArrayNode out = mapper.createArrayNode();

            List<ModelEntity> entities = new ArrayList<>();

            JsonNode modelsNode = payload.path("models");
            if (!modelsNode.isMissingNode() && modelsNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> fields = modelsNode.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> f = fields.next();
                    JsonNode section = f.getValue();
                    JsonNode values = section.path("values");
                    if (values.isArray()) {
                        for (JsonNode item : values) {
                            ObjectNode o = mapper.createObjectNode();
                            // Use path() to avoid NPEs; user said values won't be null
                            long id = item.path("id").asLong();
                            String name = item.path("name").asText(null);
                            Long makeId = item.has("makeId") && !item.path("makeId").isNull() ? item.path("makeId").asLong() : null;
                            String vehicleTypeId = item.has("vehicleTypeId") && !item.path("vehicleTypeId").isNull() ? item.path("vehicleTypeId").asText() : null;

                            // prepare response entry
                            o.put("id", id);
                            o.put("name", name);
                            if (makeId != null) o.put("makeId", makeId); else o.putNull("makeId");
                            if (vehicleTypeId != null) o.put("vehicleTypeId", vehicleTypeId); else o.putNull("vehicleTypeId");
                            out.add(o);

                            // prepare entity for persistence
                            ModelEntity me = new ModelEntity(id, name, makeId, vehicleTypeId);
                            entities.add(me);
                        }
                    }
                }
            }

            int inserted = 0;
            if (!entities.isEmpty()) {
                inserted = modelService.saveNewModels(entities);
            }

            resp.put("status", "ok");
            resp.put("count", out.size());
            resp.put("inserted", inserted);
            resp.put("ignored", out.size() - inserted);
            resp.set("models", out);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            logger.error("Invalid JSON payload in addModel", e);
            ObjectNode err = mapper.createObjectNode();
            err.put("status", "error");
            err.put("message", "Invalid JSON payload: " + e.getMessage());
            return ResponseEntity.badRequest().body(err);
        }
    }
}
