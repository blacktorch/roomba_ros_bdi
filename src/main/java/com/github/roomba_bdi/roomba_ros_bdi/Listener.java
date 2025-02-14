/*
 * Copyright (C) 2014 pi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.github.rosjava.roomba_bdi.roomba_ros_bdi;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.NodeMain;
import org.ros.node.topic.Subscriber;

import jason.asSyntax.*;

/**
 * A simple {@link Subscriber} {@link NodeMain}.
 */
public class Listener extends AbstractNodeMain {

    private SyncAgentState agentState;
    private SaviAgentArch agent;

    /**
     * Provide name of this node when requested.
     * @return
     */
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava/listener");
    }


    /**
     * Start method for the node (can think of this as the 'main()' method.
     * @param connectedNode
     */
    @Override
    public void onStart(ConnectedNode connectedNode) {

        // Initialize the agent
        this.agent = new SaviAgentArch(connectedNode);
        this.agent.startAgents();

        final Log log = connectedNode.getLog();
        Subscriber<std_msgs.String> subscriber = connectedNode.newSubscriber("perceptions", std_msgs.String._TYPE);
        subscriber.addMessageListener(new MessageListener<std_msgs.String>() {
            @Override
            public void onNewMessage(std_msgs.String message) {
                // Interpret the message as a literal
                Literal rxLiteral = Literal.parseLiteral(message.getData());

                // Handle the message
                SyncAgentState agentState = SyncAgentState.getSyncAgentState();
                agentState.setPerceptions(rxLiteral.toString());

                //log.info("I heard: \"" + rxLiteral.toString() + "\"");
            }
        });
    }
}
