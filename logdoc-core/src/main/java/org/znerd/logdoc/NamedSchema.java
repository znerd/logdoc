// See the COPYRIGHT file for copyright and license information
package org.znerd.logdoc;

import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

class NamedSchema {
    private final String name;
    private final Schema schema;

    public NamedSchema(String name, Schema schema) {
        this.name = name;
        this.schema = schema;
    }

    public String getName() {
        return name;
    }

    public Schema getSchema() {
        return schema;
    }

    public Validator newValidator() {
        return schema.newValidator();
    }
}
