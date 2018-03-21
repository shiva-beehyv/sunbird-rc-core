package io.opensaber.registry.model;

import io.opensaber.registry.exception.AuditFailedException;
import io.opensaber.registry.sink.DatabaseProvider;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.List;

public class AuditRecord {
    private String subject;
    private String predicate;
    private Object oldObject;
    private Object newObject;

    public AuditRecord subject(String label) {
        this.subject = label+"-AUDIT";
        return this;
    }

    public AuditRecord predicate(String key) {
        this.predicate = key;
        return this;
    }

    public AuditRecord oldObject(Object oldValue) {
        this.oldObject = oldValue;
        return this;
    }

    public AuditRecord newObject(Object newValue) {
        this.newObject = newValue;
        return this;
    }

    @Override
    public String toString() {
        return "AuditRecord{" +
                "subject='" + subject + '\'' +
                ", predicate='" + predicate + '\'' +
                ", oldObject=" + oldObject +
                ", newObject=" + newObject +
                '}';
    }

    public boolean record(DatabaseProvider provider) throws AuditFailedException {
        System.out.println("AUDITING as "+subject);
        GraphTraversalSource _source = provider.getGraphStore().traversal().clone();
        boolean rootNodeExists = _source.V().hasLabel(subject).hasNext();
        Vertex rootVertex;
        if(!rootNodeExists){
            System.out.println("AUDIT ROOT NOT FOUND - CREATING");
            rootVertex = _source.addV(subject).next();
        } else {
            System.out.println("AUDIT ROOT FOUND - NOT CREATING");
            rootVertex = _source.V().hasLabel(subject).next();
            rootVertex.property("@audit","true");
        }
        Vertex recordVertex = _source.addV("auditRecord").next();
        recordVertex.property("predicate",this.predicate);
        recordVertex.property("oldObject",this.oldObject);
        recordVertex.property("newObject",this.newObject);
        recordVertex.property("@audit",true);
        recordVertex.property("@auditRecord",true);
        rootVertex.addEdge("audit",recordVertex).property("@audit",true);
//        System.out.println("AUDIT "+subject+" | "+predicate+" | "+oldObject+" > "+newObject);
        return false;
    }

    public String getPredicate() {
        return predicate;
    }

    public Object getOldObject() {
        return oldObject;
    }

    public Object getNewObject() {
        return newObject;
    }

    public String getSubject() {
        return subject;
    }
}

