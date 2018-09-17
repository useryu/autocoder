package cn.agilecode.common.web.support;

import java.util.List;

public class ProjectBean {
	
	private List<Nodes> nodes;

	private List<Edges> edges;

	public void setNodes(List<Nodes> nodes) {
		this.nodes = nodes;
	}

	public List<Nodes> getNodes() {
		return this.nodes;
	}

	public void setEdges(List<Edges> edges) {
		this.edges = edges;
	}

	public List<Edges> getEdges() {
		return this.edges;
	}

	@Override
	public String toString() {
		return "ProjectBean [nodes=" + nodes + ", edges=" + edges + "]";
	}
}