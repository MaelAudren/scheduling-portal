/*
 * ################################################################
 *
 * ProActive Parallel Suite(TM): The Java(TM) library for
 *    Parallel, Distributed, Multi-Core Computing for
 *    Enterprise Grids & Clouds
 *
 * Copyright (C) 1997-2015 INRIA/University of
 *                 Nice-Sophia Antipolis/ActiveEon
 * Contact: proactive@ow2.org or contact@activeeon.com
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; version 3 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * If needed, contact us to obtain a release under GPL Version 2 or 3
 * or a different license than the AGPL.
 *
 *  Initial developer(s):               The ProActive Team
 *                        http://proactive.inria.fr/team_members.htm
 *  Contributor(s):
 *
 * ################################################################
 * $$PROACTIVE_INITIAL_DEV$$
 */
package org.ow2.proactive_grid_cloud_portal.rm.client;

import java.util.HashMap;
import java.util.Map;

import org.ow2.proactive_grid_cloud_portal.rm.client.NodeSource.Host.Node;


/**
 * Hierarchically store Nodes data as received from the REST api
 * <p>
 *
 * 
 * @author mschnoor
 *
 */
public class NodeSource {

    /** nodes are grouped per host, each nodesource can hold nodes on different hosts */
    private Map<String, Host> hosts;
    /** currently _deploying_ nodes are not yet on any host */
    private Map<String, Node> deploying;

    /** Unique name of the nodesource */
    private String sourceName;
    /** describes the infrastructure and policy used */
    private String sourceDescription;
    /** login of the user that created the NS */
    private String nodeSourceAdmin;

    private String nodeSourceInstances;

    NodeSource(String sourceName, String sourceDescription, String nodeSourceAdmin, String nodeSourceInstances) {
        this.sourceDescription = sourceDescription;
        this.sourceName = sourceName;
        this.nodeSourceAdmin = nodeSourceAdmin;
        this.nodeSourceInstances = nodeSourceInstances;
        this.hosts = new HashMap<String, Host>();
        this.deploying = new HashMap<String, Node>();
    }

    NodeSource(NodeSource t) {
        this.sourceDescription = t.sourceDescription;
        this.sourceName =t. sourceName;
        this.nodeSourceAdmin = t.nodeSourceAdmin;
        this.hosts = new HashMap<String, Host>();
        for (String hostid: t.hosts.keySet()) {
            Host h = t.hosts.get(hostid);
            this.hosts.put(hostid, new Host(h));
        }
        this.deploying = new HashMap<String, Node>();
        for (String nodeid: t.deploying.keySet()) {
        	Node n = t.deploying.get(nodeid);
        	this.deploying.put(nodeid, new Node(n));
        }
    }
    
    public Map<String, Host> getHosts() {
        return hosts;
    }

    public Map<String, Node> getDeploying() {
        return deploying;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getSourceDescription() {
        return sourceDescription;
    }

    public String getNodeSourceAdmin() {
        return nodeSourceAdmin;
    }

    public String getNodeSourceInstances() {
        return nodeSourceInstances;
    }

    public static class Host {

        /** all nodes deployed on this host for one specific nodesource*/
        private Map<String, Node> nodes;
        /** name of the host ; not unique ! */
        private String hostName;
        /** parent nodesource name */
        private String sourceName;
        /** true if one of the contained nodes contains 'VIRT' in its URL */
        private boolean virtual = false;

        Host(String hostName, String sourceName) {
            this.hostName = hostName;
            this.nodes = new HashMap<String, Node>();
            this.sourceName = sourceName;
        }
        
        Host(Host t) {
            this.hostName = t.hostName;
            this.sourceName = t.sourceName;
            this.virtual = t.virtual;
            this.nodes = new HashMap<String, Node>();
            for (String nodeid: t.nodes.keySet()) {
            	Node clone = new Node(t.nodes.get(nodeid));
            	this.nodes.put(nodeid, clone);
            }
        }

        public Map<String, Node> getNodes() {
            return nodes;
        }

        public String getHostName() {
            return hostName;
        }

        public String getSourceName() {
            return sourceName;
        }

        public boolean isVirtual() {
            return virtual;
        }

        public void setVirtual(boolean virtual) {
            this.virtual = virtual;
        }

        /**
         * @return a unique ID to differentiate each NS+host couple,
         * 		 since several NS can deploy on the same node
         */
        public String getId() {
            return sourceName + "-host-" + hostName;
        }

        public static class Node {

            /** deployed node URL */
            private String nodeUrl;
            /** current state of the node */
            private NodeState nodeState;
            /** multiline String describing the node */
            private String nodeInfo;
            /** timestamp */
            private long timeStamp;
            /** time when the node changed to this nodeState*/
            private String timeStampFormatted;
            /** user that created the node */
            private String nodeProvider;
            /** user currently using the node */
            private String nodeOwner;
            /** name of the source containing this node */
            private String sourceName;
            /** name of the host containing this node */
            private String hostName;
            /** name of the JVM running this node */
            private String vmName;
            /** toString() of the remote RMNode */
            private String description;
            /** default node JMX url */
            private String defaultJMXUrl;
            /** proactive node JMX url */
            private String proactiveJMXUrl;

            Node(String nodeUrl, String nodeState, String nodeInfo, long timeStamp,
                    String timeStampFormatted, String nodeProvider, String nodeOwner, String sourceName,
                    String hostName, String vmName, String description, String defaultJMXUrl,
                    String proactiveJMXUrl) {
                this.nodeUrl = nodeUrl;
                this.nodeState = NodeState.parse(nodeState);
                this.nodeInfo = nodeInfo;
                this.timeStampFormatted = timeStampFormatted;
                this.nodeProvider = nodeProvider;
                this.nodeOwner = nodeOwner;
                this.sourceName = sourceName;
                this.timeStamp = timeStamp;
                this.hostName = hostName;
                this.vmName = vmName;
                this.description = description;
                this.defaultJMXUrl = defaultJMXUrl;
                this.proactiveJMXUrl = proactiveJMXUrl;
            }

            Node(Node t) {
                this.nodeUrl = t.nodeUrl;
                this.nodeState = t.nodeState;
                this.nodeInfo = t.nodeInfo;
                this.timeStampFormatted = t.timeStampFormatted;
                this.nodeProvider = t.nodeProvider;
                this.nodeOwner = t.nodeOwner;
                this.sourceName = t.sourceName;
                this.timeStamp = t.timeStamp;
                this.hostName = t.hostName;
                this.vmName = t.vmName;
                this.description = t.description;
                this.defaultJMXUrl = t.defaultJMXUrl;
                this.proactiveJMXUrl = t.proactiveJMXUrl;
            }
            
            public String getNodeUrl() {
                return nodeUrl;
            }

            public NodeState getNodeState() {
                return nodeState;
            }

            public String getNodeInfo() {
                return nodeInfo;
            }

            public long getTimeStamp() {
                return timeStamp;
            }

            public String getTimeStampFormatted() {
                return timeStampFormatted;
            }

            public String getNodeProvider() {
                return nodeProvider;
            }

            public String getNodeOwner() {
                return nodeOwner;
            }

            public String getSourceName() {
                return sourceName;
            }

            public String getHostName() {
                return hostName;
            }

            public String getVmName() {
                return vmName;
            }

            public String getDescription() {
                return description;
            }

            public String getDefaultJMXUrl() {
                return defaultJMXUrl;
            }

            public String getProactiveJMXUrl() {
                return proactiveJMXUrl;
            }
        }
    }
}
