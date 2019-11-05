package warrior;

import lombok.Data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
@Entity
class Node {

  private @Id @GeneratedValue Long id;
  private String code;
  private String description;
  private String detail;

  @ManyToOne
  @JsonIgnore
  private Node parent;

  @OneToMany(mappedBy = "parent")
  private List<Node> childrens;

  Node() {}

  Node(String code, String description, String detail, Node parent, List<Node> childrens) {
    this.code = code;
    this.description = description;
    this.detail = detail;
    this.parent = parent;
    this.childrens = childrens;
  }

  public Long getParentId() {
    return this.parent != null ? this.parent.id : null;
  }

  public boolean getHasChildren() {
    if(this.childrens == null){
      return false;
    }

    return this.childrens.size() > 0 ? true : false;
  }
}