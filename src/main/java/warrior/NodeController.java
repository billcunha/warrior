package warrior;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class NodeController {

  private final NodeRepository repository;

  NodeController(NodeRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/node")
  List<Node> all() {
    List<Node> nodes = repository.findAll();

    return nodes.stream()
      .filter(node->node.getParent() == null)
      .collect(Collectors.toList());
  }

  @PostMapping("/node")
  Node newNode(@RequestBody Node newNode) {
    return repository.save(newNode);
  }

  @GetMapping("/node/{id}")
  Node one(@PathVariable Long id) {

    return repository.findById(id)
      .orElseThrow(() -> new NodeNotFoundException(id));
  }

  @PutMapping("/node/{id}")
  Node replaceNode(@RequestBody Node newNode, @PathVariable Long id) {

    return repository.findById(id)
      .map(node -> {
        if(newNode.getDescription() != null) {
          node.setDescription(newNode.getDescription());
        }
        if(newNode.getDetail() != null) {
          node.setDetail(newNode.getDetail());
        }
        if(newNode.getParent() != null) {
          node.setParent(newNode.getParent());
        }
        return repository.save(node);
      })
      .orElseGet(() -> {
        newNode.setId(id);
        return repository.save(newNode);
      });
  }

  @DeleteMapping("/node/{id}")
  void deleteNode(@PathVariable Long id) {
    repository.deleteById(id);
  }
}