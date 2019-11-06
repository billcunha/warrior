package warrior;

import java.util.List;
import java.util.Optional;
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
    if(newNode.getParentId() != null) {
      if(!repository.existsById(newNode.getParentId())) {
        throw new NodeNotFoundException(newNode.getParentId());
      }
    }

    return repository.save(newNode);
  }

  @GetMapping("/node/{id}")
  NodePojo one(@PathVariable Long id) {

    Node node = repository.findById(id)
      .orElseThrow(() -> new NodeNotFoundException(id));

    return new NodePojo(node);
  }

  @PutMapping("/node/{id}")
  NodePojo replaceNode(@RequestBody Node newNode, @PathVariable Long id) {

    return repository.findById(id)
      .map(node -> {
        if(newNode.getDescription() != null) {
          node.setDescription(newNode.getDescription());
        }
        if(newNode.getDetail() != null) {
          node.setDetail(newNode.getDetail());
        }
        if(newNode.getParentId() != null) {
          if(!repository.existsById(newNode.getParentId())) {
            throw new NodeNotFoundException(newNode.getParentId());
          }
          node.setParent(newNode.getParent());
        }
        Node savedNode = repository.save(node);
        return new NodePojo(savedNode);
      })
      .orElseGet(() -> {
        newNode.setId(id);
        Node savedNode = repository.save(newNode);
        return new NodePojo(savedNode);
      });
  }

  @DeleteMapping("/node/{id}")
  void deleteNode(@PathVariable Long id) {
    repository.deleteById(id);
  }
}