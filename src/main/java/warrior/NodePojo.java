package warrior;

import lombok.Data;

@Data
class NodePojo {

  private Long id;
  private String code;
  private String description;
  private String detail;
  private Long parentId;
  private boolean hasChildren;

  NodePojo(Node node) {
    this.id = node.getId();
    this.code = node.getCode();
    this.description = node.getDescription();
    this.detail = node.getDetail();
    this.parentId = node.getParentId();
    this.hasChildren = node.getHasChildren();
  }
}