package org.esa.snap.dataio;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.esa.snap.framework.datamodel.MetadataAttribute;
import org.esa.snap.framework.datamodel.MetadataElement;

/**
 * @author Marco Peters
 *
 */
public class ExpectedMetadata {

    @JsonProperty(required = true)
    private String path;

    @JsonProperty(required = true)
    private String value;

    // needed by json engine tb 2013-08-19
    public ExpectedMetadata() {
    }

    ExpectedMetadata(MetadataAttribute attribute) {
        final MetadataElement metadataRoot = attribute.getProduct().getMetadataRoot();
        MetadataElement currentElement = attribute.getParentElement();
        final MetadataAttribute[] attributes = currentElement.getAttributes();
        int index = 0;
        for (MetadataAttribute attrib : attributes) {
            if(attrib.getName().equals(attribute.getName())) {
                index++;
                if(attrib == attribute) {
                    break;
                }
            }
        }

        final StringBuilder sb = new StringBuilder();
        sb.append(escapeSlashes(attribute.getName()));
        if(index > 1) {
            sb.append("[").append(index).append("]");
        }
        while(true) {
            MetadataElement nextElement = currentElement.getParentElement();
            final MetadataElement[] elems = nextElement.getElements();
            int elemIndex = 0;
            for (MetadataElement elem : elems) {
                if(elem.getName().equals(currentElement.getName())) {
                    elemIndex++;
                    if(elem == currentElement) {
                        break;
                    }
                }
            }
            sb.insert(0, "/");
            if(elemIndex > 1) {
                sb.insert(0, "[" + elemIndex + "]");
            }
            sb.insert(0, escapeSlashes(currentElement.getName()));
            if(metadataRoot == nextElement) {
                break;
            }
            currentElement = nextElement;
        }
        this.path = sb.toString();
        this.value = attribute.getData().getElemString();
    }

    private String escapeSlashes(String name) {
        return name.replace("/", "//");
    }


    String getPath() {
        return path;
    }

    String getValue() {
        return value;
    }

    // for testing
    public void setPath(String path) {
        this.path = path;
    }

    // for testing
    public void setValue(String value) {
        this.value = value;
    }
}
