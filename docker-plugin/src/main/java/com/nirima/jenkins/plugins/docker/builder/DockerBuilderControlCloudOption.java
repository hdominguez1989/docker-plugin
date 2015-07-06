package com.nirima.jenkins.plugins.docker.builder;

import hudson.FilePath;
import shaded.com.google.common.base.Strings;
import hudson.model.Run;

import hudson.model.Computer;
import com.github.dockerjava.api.DockerClient;
import com.nirima.jenkins.plugins.docker.DockerCloud;
import com.nirima.jenkins.plugins.docker.DockerSlave;
import hudson.model.AbstractBuild;
import hudson.model.Node;
import jenkins.model.Jenkins;

/**
 * Created by magnayn on 30/01/2014.
 */
public abstract class DockerBuilderControlCloudOption extends DockerBuilderControlOption {
    public final String cloudName;

    protected DockerBuilderControlCloudOption(String cloudName) {
        this.cloudName = cloudName;
    }
    
    public String getCloudName() {
        return cloudName;
    }

    protected DockerCloud getCloud(Run<?, ?> build) {
        DockerCloud cloud = null;
        Node node =((AbstractBuild)build).getBuiltOn();

        if( node instanceof DockerSlave) {
            DockerSlave dockerSlave = (DockerSlave)node;
            cloud = dockerSlave.getCloud();
        }

        if( !Strings.isNullOrEmpty(cloudName) ) {
            cloud = (DockerCloud) Jenkins.getInstance().getCloud(cloudName);
        }

        if( cloud == null ) {
            throw new RuntimeException("Cannot list cloud for docker action");
        }

        return cloud;
    }

    protected DockerClient getClient(Run<?, ?> build) {
        DockerCloud cloud = getCloud(build);

        return cloud.connect();
    }
}
